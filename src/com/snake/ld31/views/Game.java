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

//sky colour - 142 255 253

@SuppressWarnings("unused")
public class Game extends View
{
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
			//disregard this ugly code plz
			int y = 60 + i * 50;
			int x = (int)(Main.ticks * 10) + (int)(Math.sin(i) * Main.instance.getScrWidth( ));
			x -= Math.floor(x / (Main.instance.getScrWidth( ) + cloud1.getWidth( ))) * (Main.instance.getScrWidth( ) + cloud1.getWidth( ));
			x -= cloud1.getWidth( );
			
			BufferedImage cloud = null;
			if (i % 2 == 0)
				cloud = cloud1;
			else
				cloud = cloud2;
			
			
			draw.drawImage( cloud, x, (int)( y + Math.sin(Main.ticks/5 + i) * 6), null );
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
				
				int scale = (int)Math.ceil((Main.camera.scale * 128.0f));
				
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
	}
	
	@Override
	public void run( float deltaTime ){ }

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
		
		skyColor = new Color( 142, 255, 253 );
		
		Main.camera.y = 8192 - Main.instance.getScrHeight( );
		Main.camera.x = 2112 - Main.instance.getScrWidth( ) / 2;
		Main.camera.scale = 1.0f;
		
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
        if(e.isPopupTrigger())
        {
            Main.instance.createPopup(e.getComponent(), e.getX(), e.getY());
        }
	}
}
