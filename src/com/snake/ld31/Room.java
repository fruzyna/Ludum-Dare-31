package com.snake.ld31;

public class Room
{
	private RoomType roomType;
	private int x,y;
	
	public Room( int x, int y )
	{
		roomType = RoomType.ROOM_AIR;
		this.x = x;
		this.y = y;
	}
	
	public int getX( )
	{
		return x;
	}
	
	public int getY( )
	{
		return y;
	}
	
	public Room( RoomType type, int x, int y  )
	{
		roomType = type;
		this.x = x;
		this.y = y;
	}
	
	public void setType( RoomType type )
	{
		roomType = type;
	}
	
	public RoomType getRoomType( )
	{
		return roomType;
	}
	
	public boolean hasUtilities( )
	{
		if (DataContainer.drenthlist.size( ) == 0)
			return false;
		
		if (DataContainer.plumbinglist.size( ) == 0)
			return false;
		
		boolean water, electricity;
		water = false;
		electricity = false;
		
		for (int i=0;i < DataContainer.drenthlist.size( );++i)
		{
			int x,y;
			x = DataContainer.drenthlist.get(i).getX( );
			y = DataContainer.drenthlist.get(i).getY( );
			
			if (getX() >= x-5 && getX() <= x+5 && getY() >= y-5 && getY() <= y+5)
			{
				electricity = true;
				break;
			}
		}
		
		for (int i=0;i < DataContainer.plumbinglist.size( );++i)
		{
			int x,y;
			x = DataContainer.plumbinglist.get(i).getX( );
			y = DataContainer.plumbinglist.get(i).getY( );
			
			if (getX() >= x-2 && getX() <= x+2 && getY() >= y-2 && getY() <= y+2)
			{
				water = true;
				break;
			}
		}
		
		return (water && electricity);
	}
}