package com.snake.ld31;

public class RoomDatas
{
	public static int getRoomValue(RoomType room)
	{
		//these values are all temp
		switch(room)
		{
		case ROOM_AIR:
			return 0;
		case ROOM_GRASS:
			return 0;
		case ROOM_LOBBYBASE:
			return 0;
		case ROOM_LOBBYEXT:
			return 1;
		case ROOM_ELEVATOR:
			return 5;
		case ROOM_RESTURANT:
			return 25;
		case ROOM_HOTEL:
			return 25;
		case ROOM_THEATER:
			return 75;
		case ROOM_THEATRE:
			return 100;
		case ROOM_SHOP:
			return 50;
		case ROOM_DRENTH:
			return 5;
		case ROOM_PLUMBING:
			return 5;
		case ROOM_PENTHOUSE:
			return 100;
		default:
			return 0;
		}
	}

	public static int getRoomIncome(RoomType room)
	{
		//income per customer
		//these values are all temp
		switch(room)
		{
		case ROOM_AIR:
			return 0;
		case ROOM_GRASS:
			return 0;
		case ROOM_LOBBYBASE:
			return 0;
		case ROOM_LOBBYEXT:
			return 0;
		case ROOM_ELEVATOR:
			return 0;
		case ROOM_RESTURANT:
			return 25;
		case ROOM_HOTEL:
			return 75;
		case ROOM_THEATER:
			return 5;
		case ROOM_THEATRE:
			return 50;
		case ROOM_SHOP:
			return 5;
		case ROOM_DRENTH:
			return 0;
		case ROOM_PLUMBING:
			return 0;
		case ROOM_PENTHOUSE:
			return 1000;
		default:
			return 0;
		}
	}
	
	public static int getRoomCost(RoomType room)
	{
		//cost per hour
		//these values are all temp
		switch(room)
		{
		case ROOM_AIR:
			return 0;
		case ROOM_GRASS:
			return 0;
		case ROOM_LOBBYBASE:
			return 1;
		case ROOM_LOBBYEXT:
			return 1;
		case ROOM_ELEVATOR:
			return 2;
		case ROOM_RESTURANT:
			return 5;
		case ROOM_HOTEL:
			return 5;
		case ROOM_THEATER:
			return 10;
		case ROOM_THEATRE:
			return 5;
		case ROOM_SHOP:
			return 5;
		case ROOM_DRENTH:
			return 2;
		case ROOM_PLUMBING:
			return 1;
		case ROOM_PENTHOUSE:
			return 10;
		default:
			return 0;
		}
	}
}
