package com.snake.ld31;

public class Room
{
	private RoomType roomType;
	
	public Room( )
	{
		roomType = RoomType.ROOM_AIR;
	}
	
	public Room( RoomType type )
	{
		roomType = type;
	}
	
	public void setType( RoomType type )
	{
		roomType = type;
	}
	
	public RoomType getRoomType( )
	{
		return roomType;
	}
	
	public int getEnergyUsage( )
	{
		return 0;
	}
	
	public int getWaterUsage( )
	{
		return 0;
	}
}