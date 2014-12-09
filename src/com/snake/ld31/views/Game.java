package com.snake.ld31.views;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import com.snake.ld31.DataContainer;
import com.snake.ld31.Main;
import com.snake.ld31.Room;
import com.snake.ld31.RoomType;
import com.snake.ld31.View;
import com.snake.ld31.Guest;

@SuppressWarnings("unused")
public class Game extends View
{
	private final int numIcons = 13;
	private final int numRestaurant = 2;
	private final int numShop = 1;
	
	private BufferedImage grass;
	private BufferedImage dirt;
	private BufferedImage roof;
	
	private BufferedImage cloud1;
	private BufferedImage cloud2;
	
	private BufferedImage lobbyBase;
	private BufferedImage lobbyEmpty;
	private BufferedImage lobbyCouch;
	
	private BufferedImage elevator;
	private BufferedImage hotel;
	private BufferedImage theater;
	private BufferedImage theatre;
	private BufferedImage shop;
	private BufferedImage drenth;
	private BufferedImage plumbing;
	private BufferedImage penthouse;
	private BufferedImage office;
	
	private BufferedImage restaurant1;
	private BufferedImage restaurant2;

	private BufferedImage beams;
	
	private BufferedImage icons[ ];
	private float iconScale[ ];
	private int selectedIcon;
	
	private Vector<Guest> guests;
	private Vector<Room> openHotels;
	private int guestTarget;
	private long lastGuestAdded = 0;
	
	private float targetScale = 1.0f;
		  
	private long lastTime;
	
	@Override
	public void draw(Graphics2D draw)
	{				
		//Draw sky
		Color skyMixed = new Color( (int)( 142.0f * getDayNight() ), (int)( 255.0f * getDayNight() ), (int)( 253.0f * getDayNight() ) );
		
		draw.setColor( skyMixed );
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
			
            draw.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getDayNight( ) ));
			draw.drawImage( cloud, x, (int)( y + Math.sin(Main.ticks/5 + i) * 6), (int)(256.0f * scale), (int)(128.0f * scale), null );
			
			draw.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 1.0f ) );
		}
		
		//Draw grid
		for (int x=0;x < DataContainer.worldWidth;++x)
		{
			for (int y=0;y < DataContainer.worldHeight;++y)
			{
				RoomType t = DataContainer.rooms[x][y].getRoomType();
								
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
				case ROOM_ELEVATOR:
					i = elevator;
					break;
				case ROOM_HOTEL:
					i = hotel;
					break;
				case ROOM_RESTURANT:
					{
						int n = (y * DataContainer.worldWidth + x) % numRestaurant;
						
						if (n == 0)
							i = restaurant1;
						else if (n == 1)
							i = restaurant2;
					}
					break;
				case ROOM_THEATER:
					i = theater;
					break;
				case ROOM_THEATRE:
					i = theatre;
					break;
				case ROOM_SHOP:
				{
					int n = (y * DataContainer.worldWidth + x) % numShop;
					
					if (n == 0)
						i = shop;
				}
				break;
				case ROOM_DRENTH:
					i = drenth;
					break;
				case ROOM_PLUMBING:
					i = plumbing;
					break;
				case ROOM_PENTHOUSE:
					i = penthouse;
					break;
				case ROOM_OFFICE:
					i = office;
					break;
				case ROOM_BEAMS:
					i = beams;
					break;
				default:
					i = null;
				}
				
				if (y == 60 && t == RoomType.ROOM_AIR)
					i = grass;
				
				if (y < 60 && t == RoomType.ROOM_AIR && DataContainer.rooms[x][y+1].getRoomType() != RoomType.ROOM_AIR && DataContainer.rooms[x][y+1].getRoomType() != RoomType.ROOM_BEAMS)
					i = roof;
				
				if (i != null)
					draw.drawImage( i, drawX, drawY, scale, scale, null );
				
				if (t == RoomType.ROOM_AIR || t == RoomType.ROOM_BEAMS)
					continue;
				
				if ((x != 0 && (DataContainer.rooms[x-1][y].getRoomType( ) == RoomType.ROOM_AIR || DataContainer.rooms[x-1][y].getRoomType( ) == RoomType.ROOM_BEAMS)) || x == 0)
				{
					draw.setColor( Color.black );
					draw.fillRect( drawX, drawY, scale/32 , scale );
				}
				
				if ((x != DataContainer.worldWidth-1 && (DataContainer.rooms[x+1][y].getRoomType( ) == RoomType.ROOM_AIR || DataContainer.rooms[x+1][y].getRoomType( ) == RoomType.ROOM_BEAMS)) || x == DataContainer.worldWidth-1)
				{
					draw.setColor( Color.black );
					draw.fillRect( drawX + scale - scale/32, drawY, scale/32 , scale );
				}
			}
		}
		
		for (int i=0;i < guests.size( );++i)
		{
			guests.get(i).onDraw( draw );
		}
		
		if (selectedIcon == 8)
		{
			for (int i=0;i < DataContainer.drenthlist.size( );++i)
			{
				Color c = new Color(255, 255, 0, 128);
				Room r = DataContainer.drenthlist.get( i );
				
				draw.setColor( c );
				draw.fillRect( (int)Main.camera.getRasterX( (r.getX( ) - 2 ) * 128 ), (int)Main.camera.getRasterY( (r.getY( ) - 2)* 128 ), (int)(640.0f * Main.camera.getScale( )), (int)(640.0f * Main.camera.getScale( )) );
			}
		}
		else if (selectedIcon == 9)
		{
			for (int i=0;i < DataContainer.plumbinglist.size( );++i)
			{
				Color c = new Color(0, 0, 255, 128);
				Room r = DataContainer.plumbinglist.get( i );
				
				draw.setColor( c );
				draw.fillRect( (int)Main.camera.getRasterX( (r.getX( ) - 2 ) * 128 ), (int)Main.camera.getRasterY( (r.getY( ) - 2)* 128 ), (int)(640.0f * Main.camera.getScale( )), (int)(640.0f * Main.camera.getScale( )) );
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

		draw.setFont(new Font("Arial", Font.PLAIN, 25));
		draw.setColor(Color.YELLOW);
		String m = String.format("%.0f", DataContainer.money);
		draw.drawString("$" + m, 10, DataContainer.yres - 40);
		draw.setColor(Color.WHITE);
		//String h = String.format("%.2f", DataContainer.hours);
		
		int hour = (int)DataContainer.hours;
		int minutes = (int)( (DataContainer.hours - (double)hour) * 60.0 );
		
		String h = hour + ":";
		if (minutes < 10)
			h = h + "0" + minutes;
		else
			h = h + minutes;
		
		draw.drawString( h, 10, DataContainer.yres - 10);
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
		minY -= 2;
		maxY++;
			
		float middleX = ((float)minX + (float)maxX)/2;
		float middleY = ((float)minY + (float)maxY)/2;
		
		float width = Math.max( (maxX - middleX)*2.0f, (middleX - minX)*2.0f );
		float height = Math.max( (maxY - middleY)*2.0f, (middleY - minY)*2.0f );
		
		float scale = Math.max( width, height );
		
		float xAdd = 0;
		float yAdd = 0;
		
		if (width > height)
			xAdd = (width - height) * 32;
		else
			yAdd = (height - width) * 32;
		
		Main.camera.setCameraRect( middleX*128.0f - (scale * 64.0f) - xAdd, (middleY - scale/2.0f)*128 - yAdd, scale*128, scale*128 );
	}
	
	public void addGuest( Room hotel )
	{
		if (openHotels.contains(hotel))
		{
			System.out.println("room is already in list of open hotels");
			return;
		}
			
		openHotels.add( hotel );
		guestTarget++;
	}
	
	public float getDayNight( )
	{
		if (DataContainer.hours >= 6 && DataContainer.hours <= 7)
			return (float)DataContainer.hours - 6.0f;
		
		if (DataContainer.hours >= 7 && DataContainer.hours <= 19)
			return 1;
		
		if (DataContainer.hours >= 19 && DataContainer.hours <= 20)
			return (float)(1.0f - (DataContainer.hours - 19.0f));
		
		return 0;
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
		
		if (guests.size() < guestTarget && !openHotels.isEmpty() && System.currentTimeMillis() > lastGuestAdded + 3500)
		{
			guests.addElement( new Guest( openHotels.remove( openHotels.size()-1 ) ) );
			lastGuestAdded = System.currentTimeMillis() - Main.rnd.nextInt( 200 );
		}
		
		for (int i=0;i < guests.size( );++i)
		{
			if (guests.get(i).delete)
			{
				if (guests.get(i).hotelRoom.getRoomType( ) == RoomType.ROOM_HOTEL)
					addGuest( guests.get(i).hotelRoom );
				
				guests.get(i).delete = false;
				guests.remove(i);
				
				--i;
				continue;
			}
			
			guests.get(i).onTick( deltaTime );
		}
		
		DataContainer.hours += (float)(System.currentTimeMillis() - lastTime)/60000;
		lastTime = System.currentTimeMillis();
		
		if (DataContainer.hours > 24)
			DataContainer.hours -= 24;
	}
	

	@Override
	public void init()
	{
		grass =			Main.imgLoader.load("grass.png");
		dirt = 			Main.imgLoader.load("dirt.png");
		roof = 			Main.imgLoader.load("roof.png");
		
		cloud1 =		Main.imgLoader.load("cloud.png");
		cloud2 =		Main.imgLoader.load("cloud2.png");
		
		lobbyBase =		Main.imgLoader.load("lobby.png");
		lobbyEmpty =	Main.imgLoader.load("lobby_empty.png");
		lobbyCouch =	Main.imgLoader.load("lobby_couch.png");
		
		restaurant1 = 	Main.imgLoader.load("restaurant1.png");
		restaurant2	=	Main.imgLoader.load("restaurant2.png");
		
		elevator =		Main.imgLoader.load("elevator.png");
		hotel =			Main.imgLoader.load("hotel.png");
		theater =		Main.imgLoader.load("theater.png");
		theatre =		Main.imgLoader.load("theatre.png");
		shop =			Main.imgLoader.load("shop.png");
		drenth =		Main.imgLoader.load("electrical_closet.png");
		plumbing =		Main.imgLoader.load("plumbing.png");
		penthouse =		Main.imgLoader.load("penthouse.png");
		office =		Main.imgLoader.load("office.png");
		
		beams =			Main.imgLoader.load("beams.png");
		
		icons =			new BufferedImage[numIcons*2];
		icons[0] = 		Main.imgLoader.load("icons/icon_delete.png");
		icons[1] = 		Main.imgLoader.load("icons/icon_delete_selected.png");
		icons[2] = 		Main.imgLoader.load("icons/icon_lobby.png");
		icons[3] =		Main.imgLoader.load("icons/icon_lobby_selected.png");
		icons[4] = 		Main.imgLoader.load("icons/icon_elevator.png");
		icons[5] =		Main.imgLoader.load("icons/icon_elevator_selected.png");
		icons[6] =		Main.imgLoader.load("icons/icon_hotel.png");
		icons[7] = 		Main.imgLoader.load("icons/icon_hotel_selected.png");
		icons[8] = 		Main.imgLoader.load("icons/icon_restaurant.png");
		icons[9] = 		Main.imgLoader.load("icons/icon_restaurant_selected.png");
		icons[10] = 	Main.imgLoader.load("icons/icon_theater.png");
		icons[11] = 	Main.imgLoader.load("icons/icon_theater_selected.png");
		icons[12] = 	Main.imgLoader.load("icons/icon_theatre.png");
		icons[13] = 	Main.imgLoader.load("icons/icon_theatre_selected.png");
		icons[14] = 	Main.imgLoader.load("icons/icon_shop.png");
		icons[15] = 	Main.imgLoader.load("icons/icon_shop_selected.png");
		icons[16] = 	Main.imgLoader.load("icons/icon_drenth.png");
		icons[17] = 	Main.imgLoader.load("icons/icon_drenth_selected.png");
		icons[18] = 	Main.imgLoader.load("icons/icon_plumbing.png");
		icons[19] = 	Main.imgLoader.load("icons/icon_plumbing_selected.png");
		icons[20] = 	Main.imgLoader.load("icons/icon_penthouse.png");
		icons[21] = 	Main.imgLoader.load("icons/icon_penthouse_selected.png");
		icons[22] = 	Main.imgLoader.load("icons/icon_office.png");
		icons[23] = 	Main.imgLoader.load("icons/icon_office_selected.png");
		icons[24] = 	Main.imgLoader.load("icons/icon_beams.png");
		icons[25] = 	Main.imgLoader.load("icons/icon_beams_selected.png");
		
		selectedIcon = -1;
		iconScale = new float[numIcons];
		
		for (int i=0;i < numIcons;++i){ iconScale[i] = 1.0f; }
		
		Main.camera.y = 8192 - Main.instance.getScrHeight( );
		Main.camera.x = 2112 - Main.instance.getScrWidth( ) / 2;
		Main.camera.scale = 1.0f;
		
		openHotels = new Vector<Room>( );
		DataContainer.drenthlist = new Vector<Room>( );
		DataContainer.plumbinglist = new Vector<Room>( );
		
		if (!DataContainer.loaded)
		{
			DataContainer.worldWidth = 31;
			DataContainer.worldHeight = 64;
			DataContainer.rooms = new Room[31][64];
			DataContainer.worldName = "world";
		
			DataContainer.guests = new Vector<Guest>( );
			
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
					
					DataContainer.rooms[x][y] = new Room( t, x, y );
				}
			}
		
			DataContainer.hours = 7;
			
			DataContainer.loaded = true;
		}
		else
		{
			guestTarget = 0;
			
			for (int x = 0;x < DataContainer.worldWidth;++x)
			{
				for (int y=0;y < DataContainer.worldHeight;++y)
				{
					RoomType t = DataContainer.rooms[x][y].getRoomType( );
					
					if (t == RoomType.ROOM_DRENTH)
						DataContainer.drenthlist.add( DataContainer.rooms[x][y] );
					else if (t == RoomType.ROOM_PLUMBING)
						DataContainer.plumbinglist.add( DataContainer.rooms[x][y]);
					else if (t == RoomType.ROOM_HOTEL)
						guestTarget++;
					else
						continue;
					
					boolean guestHasThis = false;
					
					for (int g = 0;g < DataContainer.guests.size( );++g)
					{
						Guest gu = DataContainer.guests.get(g);
						if (gu.hotelRoom.equals( DataContainer.rooms[x][y] ))
						{
							guestHasThis = true;
							
							break;
						}
					}
					
					if (!guestHasThis)
						openHotels.add( DataContainer.rooms[x][y] );
				}
			}
		}
		
		guests = DataContainer.guests;
		lastGuestAdded = 0;
		
		updateViewBounds( );
		Main.camera.set( );
		
		lastTime = System.currentTimeMillis();
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
        
        if (e.getY( ) >= 15 && e.getY( ) <= 79)
        {        	
        	for (int i=0;i < numIcons;++i)
        	{
        		if (Main.mouseX >= (15 + 80*i) && Main.mouseX <= (79 + 80*i) && (icons[i*2] != null))
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
						{
							if (t == RoomType.ROOM_DRENTH)
								DataContainer.drenthlist.remove( DataContainer.rooms[gridX][gridY] );
							else if (t == RoomType.ROOM_PLUMBING)
								DataContainer.plumbinglist.remove( DataContainer.rooms[gridX][gridY] );
							
							if (gridY != 0 && DataContainer.rooms[gridX][gridY-1].getRoomType( ) != RoomType.ROOM_AIR)
								DataContainer.rooms[gridX][gridY].setType( RoomType.ROOM_BEAMS );
							else
								DataContainer.rooms[gridX][gridY].setType( RoomType.ROOM_AIR );
						}
					}
				}
				
				updateViewBounds( );
				break;
			case 1: //lobby
				if ((DataContainer.rooms[gridX][gridY].getRoomType() == RoomType.ROOM_AIR || DataContainer.rooms[gridX][gridY].getRoomType() == RoomType.ROOM_BEAMS) && gridY == 60 && DataContainer.money >= 50)
				{
					DataContainer.rooms[gridX][gridY].setType( RoomType.ROOM_LOBBYEXT );
					DataContainer.money -= 50;
				}
				
				updateViewBounds( );
				break;
			case 2: //elevator
				if( canBeRoom(gridX, gridY) && DataContainer.money >= 75)
				{
					DataContainer.rooms[gridX][gridY].setType(RoomType.ROOM_ELEVATOR);
					DataContainer.money -= 75;
				}
				updateViewBounds();
				break;
			case 3: //hotel
				if ( canBeRoom(gridX, gridY) && DataContainer.money >= 200)
				{
					DataContainer.rooms[gridX][gridY].setType( RoomType.ROOM_HOTEL );
					DataContainer.money -= 200;
				}
				
				updateViewBounds( );
				addGuest( DataContainer.rooms[gridX][gridY] );
				break;
			case 4: //resturant
				if ( canBeRoom(gridX, gridY) && DataContainer.money >= 250)
				{
					DataContainer.rooms[gridX][gridY].setType( RoomType.ROOM_RESTURANT );
					DataContainer.money -= 250;
				}
				
				updateViewBounds( );
				break;
			case 5: //theater
				if ( canBeRoom(gridX, gridY) && DataContainer.money >= 500 )
				{
					DataContainer.rooms[gridX][gridY].setType( RoomType.ROOM_THEATER);
					DataContainer.money -= 500;
				}
				
				updateViewBounds( );
				break;
			case 6: //theatre
				if ( canBeRoom(gridX, gridY) )
					DataContainer.rooms[gridX][gridY].setType( RoomType.ROOM_THEATRE );
				updateViewBounds( );
				break;
			case 7: //shop
				if ( canBeRoom(gridX, gridY) && DataContainer.money >= 300)
				{
					DataContainer.rooms[gridX][gridY].setType( RoomType.ROOM_SHOP );
					DataContainer.money -= 300;
				}
				
				updateViewBounds( );
				break;
			case 8: //drenth
				if ( canBeRoom(gridX, gridY) && DataContainer.money >= 100)
				{
					DataContainer.rooms[gridX][gridY].setType( RoomType.ROOM_DRENTH );
					DataContainer.money -= 100;
					
					DataContainer.drenthlist.add( DataContainer.rooms[gridX][gridY] );
				}
				
				updateViewBounds( );
				break;
			case 9: //plumbing
				if ( canBeRoom(gridX, gridY) && DataContainer.money >= 100)
				{
					DataContainer.rooms[gridX][gridY].setType( RoomType.ROOM_PLUMBING );
					DataContainer.money -= 100;
					
					DataContainer.plumbinglist.add( DataContainer.rooms[gridX][gridY] );
				}
				
				updateViewBounds( );
				break;
			case 10: //penthouse
				if ( canBeRoom(gridX, gridY)  )
					DataContainer.rooms[gridX][gridY].setType( RoomType.ROOM_PENTHOUSE );
				
				updateViewBounds( );
				break;
			case 11: //office
				if ( canBeRoom(gridX, gridY) )
					DataContainer.rooms[gridX][gridY].setType( RoomType.ROOM_OFFICE );
				
				updateViewBounds( );
				break;
			case 12: //beams
				if (DataContainer.rooms[gridX][gridY].getRoomType() == RoomType.ROOM_AIR)
				{
					if ( DataContainer.rooms[gridX][gridY + 1].getRoomType() != RoomType.ROOM_AIR )
						
						DataContainer.rooms[gridX][gridY].setType( RoomType.ROOM_BEAMS );
				
					if ( gridX != DataContainer.worldWidth && DataContainer.rooms[gridX+1][gridY].getRoomType() != RoomType.ROOM_AIR )
						DataContainer.rooms[gridX][gridY].setType( RoomType.ROOM_BEAMS );
					
					if ( gridX != 0 && DataContainer.rooms[gridX-1][gridY].getRoomType() != RoomType.ROOM_AIR )
						DataContainer.rooms[gridX][gridY].setType( RoomType.ROOM_BEAMS );
				}
				updateViewBounds( );
				break;
			default:
				break;
			}
        }
	}
	
	private boolean canBeRoom(int gridX, int gridY)
	{
		if (DataContainer.rooms[gridX][gridY].getRoomType() == RoomType.ROOM_BEAMS)
			return true;
		
		if (DataContainer.rooms[gridX][gridY].getRoomType() != RoomType.ROOM_AIR)
			return false;
		
		if (gridY != DataContainer.worldHeight && DataContainer.rooms[gridX][gridY+1].getRoomType() != RoomType.ROOM_AIR)
			return true;
			
		return false;
	}
}
