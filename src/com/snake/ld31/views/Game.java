package com.snake.ld31.views;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import com.snake.ld31.DataContainer;
import com.snake.ld31.Main;
import com.snake.ld31.Room;
import com.snake.ld31.RoomType;
import com.snake.ld31.View;

@SuppressWarnings("unused")
public class Game extends View
{
	private final int numIcons = 2;
	
	private BufferedImage grass;
	private BufferedImage dirt;
	private BufferedImage arrowLeft;
	private BufferedImage arrowRight;
	private BufferedImage arrowUp;
	
	private BufferedImage cloud1;
	private BufferedImage cloud2;
	
	private BufferedImage lobbyBase;
	private BufferedImage lobbyEmpty;
	private BufferedImage lobbyCouch;
	
	private BufferedImage elevator;
	
	private BufferedImage icons[ ];
	private float iconScale[ ];
	private int selectedIcon;
	
	private float targetScale = 1.0f;
	
	private Color skyColor;
	private Boolean menuOpen = false;
		  
	@Override
	public void draw(Graphics2D draw)
	{				
		//Draw sky
		draw.setColor( skyColor );
		draw.fillRect( 0, 0, Main.instance.getScrWidth(), Main.instance.getScrHeight() );
		
		//Draw clouds
		for (int i=0;i < 10;++i)
		{
			float scale = Main.camera.getScale( );
			
			//disregard this ugly code plz
			int y = 60 + i * 50;
			int x = (int)(Main.ticks * 10) + (int)(Math.sin(i) * Main.instance.getScrWidth( ));
			x -= Math.floor(x / (Main.instance.getScrWidth( ) + (cloud1.getWidth( ) * scale))) * (Main.instance.getScrWidth( ) + (cloud1.getWidth( ) * scale));
			x -= cloud1.getWidth( );
			
			BufferedImage cloud = null;
			if (i % 2 == 0)
				cloud = cloud1;
			else
				cloud = cloud2;
			
			draw.drawImage( cloud, x, (int)( y + Math.sin(Main.ticks/5 + i) * 6), (int)(256.0f * scale), (int)(128.0f * scale), null );
		}
		
		//Draw grid
		for (int x=0;x < DataContainer.worldWidth;++x)
		{
			for (int y=0;y < DataContainer.worldHeight;++y)
			{
				RoomType t = DataContainer.rooms[x][y].getRoomType();
				
				if (t == RoomType.ROOM_AIR && y != 60)
					continue;
				
				int drawX = (int)Main.camera.getRasterX( x * 128 );
				int drawY = (int)Main.camera.getRasterY( y * 128 );
				
				int scale = (int)Math.ceil((Main.camera.getScale( ) * 128.0f));
				
				BufferedImage i = null;
				
				switch (t)
				{
				case ROOM_GRASS:
					i = dirt;
					break;
				case ROOM_LOBBYBASE:
					i = lobbyBase;
					break;
				case ROOM_LOBBYEXT:
					if (x % 2 == 0)
						i = lobbyEmpty;
					else
						i = lobbyCouch;
					break;
				default:
					i = null;
				}
				
				if (y == 60 && t == RoomType.ROOM_AIR)
					i = grass;
				
				if (i != null)
					draw.drawImage( i, drawX, drawY, scale, scale, null );
				
				if (t == RoomType.ROOM_AIR)
					continue;
				
				if ((x != 0 && DataContainer.rooms[x-1][y].getRoomType( ) == RoomType.ROOM_AIR) || x == 0)
				{
					draw.setColor( Color.black );
					draw.fillRect( drawX, drawY, scale/32 , scale );
				}
				
				if ((x != DataContainer.worldWidth-1 && DataContainer.rooms[x+1][y].getRoomType( ) == RoomType.ROOM_AIR) || x == DataContainer.worldWidth-1)
				{
					draw.setColor( Color.black );
					draw.fillRect( drawX + scale - scale/32, drawY, scale/32 , scale );
				}
			}
		}
		
		if (selectedIcon != -1)
		{
			int localX, localY;
			int gridX, gridY;
			
			localX = (int)Main.camera.getWorldX( Main.mouseX );
			localY = (int)Main.camera.getWorldY( Main.mouseY );
			
			gridX = (localX / 128) * 128;
			gridY = (localY / 128) * 128;
			
			Color c = new Color(255,255,255, (int)(Math.sin(Main.ticks * 7) * 70.0) + 128 );

			draw.setColor( c );
			draw.fillRect( (int)Main.camera.getRasterX(gridX), (int)Main.camera.getRasterY(gridY), (int)(128.0f * Main.camera.getScale( )), (int)(128.0f * Main.camera.getScale( )) );
		}
		
		for (int i=0;i < numIcons;++i)
		{
			BufferedImage j;
			
			if (selectedIcon == i)
				j = icons[i*2 + 1];
			else
				j = icons[i*2];
			
			draw.drawImage( j, 80*i + 47 - (int)(iconScale[i] * 64.0f)/2, 47 - (int)(iconScale[i] * 64.0f)/2, (int)(iconScale[i] * 64.0f), (int)(iconScale[i] * 64.0f), null );
		}
	}
	
	public void updateViewBounds( )
	{
		int minX, minY;
		int maxX, maxY;
		
		minX = maxX = 16;
		minY = 60;
		maxY = 61;
		
		for (int x=0;x < DataContainer.worldWidth;++x)
		{
			for (int y=0;y < DataContainer.worldHeight;++y)
			{
				RoomType t = DataContainer.rooms[x][y].getRoomType( );
				
				if (t == RoomType.ROOM_AIR || t == RoomType.ROOM_GRASS)
					continue;
				
				if (x < minX)
					minX = x;
				
				if (x > maxX)
					maxX = x;
				
				if (y < minY)
					minY = y;
				
				if (y > maxY)
					maxY = y;
			}
		}
		
		minX--;
		maxX++;
		minY--;
		maxY++;
			
		float middleX = ((float)minX + (float)maxX)/2;
		float middleY = ((float)minY + (float)maxY)/2;
		
		float width = (maxX - middleX)*2;
		float height = (maxY - middleY)*2;
		
		float scale = Math.max( width, height );
		
		Main.camera.setCameraRect( (middleX - scale/2 - 0.5f)*128, (middleY - scale/2)*128, scale*128, scale*128 );		
	}
	
	@Override
	public void run( float deltaTime )
	{ 
		for (int i=0;i < numIcons;++i)
		{
			if (Main.mouseX >= (15 + 80*i) && Main.mouseX <= (79 + 80*i) && Main.mouseY >= 15 && Main.mouseY <= 79)
				iconScale[i] += (1.2 - iconScale[i]) * deltaTime * 16;
			else
				iconScale[i] += (1.0 - iconScale[i]) * deltaTime * 16;
		}
	}

	@Override
	public void init()
	{
		grass =			Main.imgLoader.load("grass.png");
		dirt = 			Main.imgLoader.load("dirt.png");
		arrowLeft = 	Main.imgLoader.load("arrow_left.png");
		arrowRight = 	Main.imgLoader.load("arrow_right.png");
		arrowUp	=		Main.imgLoader.load("arrow_up.png");
		
		cloud1 =		Main.imgLoader.load("cloud.png");
		cloud2 =		Main.imgLoader.load("cloud2.png");
		
		lobbyBase =		Main.imgLoader.load("lobby.png");
		lobbyEmpty =	Main.imgLoader.load("lobby_empty.png");
		lobbyCouch =	Main.imgLoader.load("lobby_couch.png");
		
		elevator =		Main.imgLoader.load("elevator.png");
		
		icons =			new BufferedImage[numIcons*2];
		icons[0] = 		Main.imgLoader.load("icons/icon_delete.png");
		icons[1] = 		Main.imgLoader.load("icons/icon_delete_selected.png");
		icons[2] = 		Main.imgLoader.load("icons/icon_lobby.png");
		icons[3] =		Main.imgLoader.load("icons/icon_lobby_selected.png");
		
		skyColor = new Color( 142, 255, 253 );
		
		selectedIcon = -1;
		iconScale = new float[numIcons];
		
		for (int i=0;i < numIcons;++i){ iconScale[i] = 1.0f; }
		
		Main.camera.y = 8192 - Main.instance.getScrHeight( );
		Main.camera.x = 2112 - Main.instance.getScrWidth( ) / 2;
		Main.camera.scale = 1.0f;
		
		if (!DataContainer.loaded)
		{
			DataContainer.worldWidth = 31;
			DataContainer.worldHeight = 64;
			DataContainer.rooms = new Room[31][64];
			DataContainer.worldName = "world";
			
			for (int x = 0;x < DataContainer.worldWidth;++x)
			{
				for (int y = 0;y < DataContainer.worldHeight;++y)
				{
					RoomType t = RoomType.ROOM_AIR;
					
					if (y > 60)
						t = RoomType.ROOM_GRASS;
					
					if (x == 16 && y == 60)
						t = RoomType.ROOM_LOBBYBASE;
					
					if (x == 15 && y == 60)
						t = RoomType.ROOM_LOBBYEXT;
					
					if (x == 17 && y == 60)
						t = RoomType.ROOM_LOBBYEXT;
					
					DataContainer.rooms[x][y] = new Room( t );
				}
			}
		
			DataContainer.loaded = true;
		}
		
		updateViewBounds( );
		Main.camera.set( );
	}

	@Override
	public void keyPressed(KeyEvent e){}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			Main.instance.goToMain();
		}
	}

	@Override
	public void mouseClick(MouseEvent e)
	{
		// this doesnt do anything on linux
		// |
		// V
        if(e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3)
        {
        	System.out.println("Right Click!");
            Main.instance.createPopup(e.getComponent(), e.getX(), e.getY());
        }
        
        if (e.getY( ) >= 15 && e.getY( ) <= 79)
        {        	
        	for (int i=0;i < numIcons;++i)
        	{
        		if (Main.mouseX >= (15 + 80*i) && Main.mouseX <= (79 + 80*i))
        		{	
        			iconScale[i] = 0.0f;
        			
        			if (selectedIcon != i)
        				selectedIcon = i;
        			else
        				selectedIcon = -1;
        			
        			return;
        		}
        	}
        }
        
        if (selectedIcon != -1)
        {
			int localX, localY;
			int gridX, gridY;
			
			localX = (int)Main.camera.getWorldX( Main.mouseX );
			localY = (int)Main.camera.getWorldY( Main.mouseY );
			
			gridX = (localX / 128);
			gridY = (localY / 128);
			
			switch (selectedIcon)
			{
			case 0: //delete
				{	
					RoomType t = DataContainer.rooms[gridX][gridY].getRoomType( );
					
					if (t != RoomType.ROOM_GRASS && t != RoomType.ROOM_LOBBYBASE )
					{
						if (gridY > 60)
							DataContainer.rooms[gridX][gridY].setType( RoomType.ROOM_GRASS );
						else
							DataContainer.rooms[gridX][gridY].setType( RoomType.ROOM_AIR );
					}
				}
				
				updateViewBounds( );
				break;
			case 1: //lobby
				if (DataContainer.rooms[gridX][gridY].getRoomType() == RoomType.ROOM_AIR && gridY == 60)
					DataContainer.rooms[gridX][gridY].setType( RoomType.ROOM_LOBBYEXT );
				
				updateViewBounds( );
				break;
			default:
				break;
			}
        }
	}
}
