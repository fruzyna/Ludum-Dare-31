package com.snake.ld31;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Vector;

public class Guest
{
	public float x;
	private int floor;
	public boolean hasCheckedIn;
	private boolean leaving;
	
	public Room hotelRoom;
	private Room target;
	private Room elevator;
	
	private long checkInTime;
	private float feet;
	
	private Color face, top, bottom;
	
	public boolean delete;
	
	public Guest( Room hotelRoom )
	{
		x = DataContainer.worldWidth * 128;
		floor = 0;
		
		hasCheckedIn = false;
		checkInTime = 0;
		
		this.hotelRoom = hotelRoom;
		target = null;
		elevator = null;
		
		face = new Color( Color.HSBtoRGB( (Main.rnd.nextFloat() * 0.12f) + 0.12f , (Main.rnd.nextFloat() * 0.6f) + 0.4f, (Main.rnd.nextFloat( ) * 0.7f) + 0.3f ) );
		top = new Color( Main.rnd.nextInt(255), Main.rnd.nextInt(255), Main.rnd.nextInt(255) );
		bottom = new Color( Main.rnd.nextInt(255), Main.rnd.nextInt(255), Main.rnd.nextInt(255) );
	}
	
	public void onTick( float delta )
	{
		//feet += delta + Main.rnd.nextFloat() * 0.1;
		float oldX = x;
		
		if (!hasCheckedIn)
		{
			if (checkInTime == 0)
				x -= delta * 70;
			
			if (Math.abs( x - (16.5f * 128.0f) ) < 5 && checkInTime == 0)
				checkInTime = System.currentTimeMillis( );
			
			if (checkInTime != 0 && System.currentTimeMillis( ) > checkInTime+1500)
			{
				hasCheckedIn = true; 
				goToHotelRoom( );
			}
		}
		else
		{
			double time = DataContainer.hours;
			DataContainer.money += .001;
			if ((hotelRoom.getRoomType() != RoomType.ROOM_HOTEL || !hotelRoom.hasUtilities()) && !leaving)
			{
				leave( );
			}
			
			if (target != null)
			{
				if (elevator != null && getY() != target.getY())
				{
					x += (Math.signum((elevator.getX( ) * 128 + 64 ) - x) * delta * 70);
					if (Math.abs((elevator.getX()*128 + 64) - x) < 5.0f)
						setY( target.getY() );
				}
				else
				{
					if (Math.abs((target.getX()*128 + 64) - x) < 5.0f)
					{
						if (leaving)
						{
							delete = true;
						}
						else if (target.getRoomType() == RoomType.ROOM_HOTEL)
						{
							if((time > 11 && time < 13.5) || (time > 6 && time < 9) || (time > 17 && time < 20) )
							{
								//1 in 10,000 chance to eat during appropriate times
								int rand = (int)(Math.random()*10000);
								if (rand == 69)
								{
									goToRestaurant( );
									//you make a random amount of money up to $40
									int income = (int)(Math.random()*40);
									DataContainer.money += income;
								}
							}
							else if(time > 9)
							{
								//1 in 50,000 chance to shop after breakfast
								int rand = (int)(Math.random()*50000);
								if (rand == 69)
								{
									goToShop( );
									//you make a random amount of money from $10 to $90
									int income = (int)(Math.random()*80);
									income += 10;
									DataContainer.money += income;
								}
							}
						}
						else if(target.getRoomType() == RoomType.ROOM_RESTURANT)
						{
							if(time < 6 || time > 20 || (time > 9 && time < 11) || (time > 13.5 && time < 17))
							{
								goToHotelRoom();
							}
						}
						else if(target.getRoomType() == RoomType.ROOM_SHOP)
						{
							//1 in 1,000 chance to leave the store
							int rand = (int)(Math.random()*1000);
							if (rand == 69)
								goToHotelRoom( );
						}
					}
					else
					{
						x += (Math.signum((target.getX( ) * 128 + 64 ) - x) * delta * 70);
					}
				}
			}
			
			if(findRestaurant() == null)
			{
				//1 in 50,000 to leave if there is no food
				int rand = (int)(Math.random()*50000);
				if (rand == 69)
					leave( );
			}
			
			if(findShop() == null)
			{
				//1 in 100,000 to leave if there is no shop
				int rand = (int)(Math.random()*100000);
				if (rand == 69)
					leave( );
			}
			
			if(time > 22)
			{
				goToHotelRoom();
			}
		}
		
		feet += (x - oldX) * (Main.rnd.nextFloat() * 0.1 + 0.1);
	}
	
	public int getY( )
	{
		return (60 - floor);
	}
	
	public int getX( )
	{
		return (int)x;
	}
	
	public void setY( int y )
	{
		floor = 60 - y;
	}
	
	public void leave( )
	{
		target = DataContainer.rooms[DataContainer.worldWidth-1][60];
		leaving = true;
	}
	
	public void goToHotelRoom( )
	{
		elevator = findNearestElevator( hotelRoom.getY( ) );
		target = hotelRoom;
	}

	public void goToRestaurant( )
	{
		Room r = findRestaurant( );
		
		if (r == null)
			return;
		
		elevator = findNearestElevator( r.getY( ) );
		target = r;
	}
	
	public void goToShop( )
	{
		Room r = findShop( );
		
		if (r == null)
			return;
		
		elevator = findNearestElevator( r.getY( ) );
		target = r;
	}

	//gets a random restaurant
	private Room findRestaurant( )
	{
		Vector<Room> restaurants = new Vector<Room>( );
		
		for (int x=0;x < DataContainer.worldWidth;++x)
		{
			for (int y=0;y < DataContainer.worldHeight;++y)
			{
				if (DataContainer.rooms[x][y].getRoomType() == RoomType.ROOM_RESTURANT && DataContainer.rooms[x][y].hasUtilities())
					restaurants.add( DataContainer.rooms[x][y] );
			}
		}
		
		if (restaurants.isEmpty())
			return null;
		
		return restaurants.elementAt( Main.rnd.nextInt( restaurants.size() ) );
	}
	
	//gets a random shop
	private Room findShop( )
	{
		Vector<Room> shops = new Vector<Room>( );
		
		for (int x=0;x < DataContainer.worldWidth;++x)
		{
			for (int y=0;y < DataContainer.worldHeight;++y)
			{
				if (DataContainer.rooms[x][y].getRoomType() == RoomType.ROOM_SHOP && DataContainer.rooms[x][y].hasUtilities())
					shops.add( DataContainer.rooms[x][y] );
			}
		}
		
		if (shops.isEmpty())
			return null;
		
		return shops.elementAt( Main.rnd.nextInt( shops.size() ) );
	}
	
	private Room findNearestElevator( int targetFloor )
	{
		int startX = (int)(x/128.0f);
		int startY = getY( );
		
		int closest = -1;
		Room e = null;
		
		for (int i=startX;i < DataContainer.worldWidth;++i)
		{
			if (DataContainer.rooms[i][startY].getRoomType( ) == RoomType.ROOM_ELEVATOR && ((e == null) || (e != null && ((i - startX) < closest) ) ) && (DataContainer.rooms[i][targetFloor].getRoomType() == RoomType.ROOM_ELEVATOR))
			{
				e = DataContainer.rooms[i][startY];
				closest = i-startX;
			}
		}
		
		for (int i=startX;i > 0;--i)
		{
			if (DataContainer.rooms[i][startY].getRoomType( ) == RoomType.ROOM_ELEVATOR && ((e == null) || (e != null && ((startX - i) < closest) ) ) && (DataContainer.rooms[i][targetFloor].getRoomType() == RoomType.ROOM_ELEVATOR))
			{
				e = DataContainer.rooms[i][startY];
				closest = startX - i;
			}
		}
			
		return e;
	}
	
	public void onDraw( Graphics2D draw )
	{
		float scale = Main.camera.getScale( );
		
		int y_ = (61 - floor)*128 - 16;
		int x_ = (int)x;
		
		float lfoot = (float)Math.abs( Math.sin( (double)feet ) ) * 14.0f;
		float rfoot = (float)Math.abs( Math.sin( (double)feet - Math.PI/2 ) ) * 14.0f;
		
		draw.setColor( bottom );
		draw.fillRect( (int)Main.camera.getRasterX(x_ - 10), (int)Main.camera.getRasterY(y_ - 28), (int)(4.0f * scale), (int)((28.0f - lfoot) * scale) );
		draw.fillRect( (int)Main.camera.getRasterX(x_ + 6), (int)Main.camera.getRasterY(y_ - 28), (int)(4.0f * scale), (int)((28.0f - rfoot) * scale) );
		draw.fillRect( (int)Main.camera.getRasterX(x_ - 10), (int)Main.camera.getRasterY(y_ - 32), (int)(20.0f * scale), (int)(5.0f * scale));
		
		draw.setColor( top );
		draw.fillRect( (int)Main.camera.getRasterX(x_ - 10), (int)Main.camera.getRasterY(y_ - 52), (int)(20.0f * scale), (int)(20.0f * scale) );
		draw.fillRect( (int)Main.camera.getRasterX(x_ - 6), (int)Main.camera.getRasterY(y_ - 56), (int)(12.0f * scale), (int)(20.0f * scale) );
		draw.fillRect( (int)Main.camera.getRasterX(x_ - 14), (int)Main.camera.getRasterY(y_ - 52), (int)(28.0f * scale), (int)(4.0f * scale) );
		
		draw.fillRect( (int)Main.camera.getRasterX(x_ - 18), (int)Main.camera.getRasterY(y_ - 52), (int)(4.0f * scale), (int)(20.0f * scale) );
		draw.fillRect( (int)Main.camera.getRasterX(x_ + 14), (int)Main.camera.getRasterY(y_ - 52), (int)(4.0f * scale), (int)(20.0f * scale) );
		
		draw.setColor( face );
		draw.fillRect( (int)Main.camera.getRasterX(x_ - 10), (int)Main.camera.getRasterY(y_ - 76), (int)(20.0f * scale), (int)(20.0f * scale) );
	}
}
