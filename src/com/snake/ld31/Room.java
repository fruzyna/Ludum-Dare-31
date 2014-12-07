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
}