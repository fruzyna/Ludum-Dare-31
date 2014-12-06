package com.snake.ld31;

public class Camera
{
	public float x,y;
	public float scale;
	
	public Camera( )
	{
		x = 0.0f;
		y = 0.0f;
		scale = 1.0f;
	}
	
	public float getRasterX( float worldX )
	{
		return (worldX - x) * scale;
	}
	
	public float getRasterY( float worldY )
	{
		return (worldY - y) * scale;
	}
	
	public void center( float x, float y )
	{
		this.x = x - Main.instance.getScrWidth( )/2;
		this.y = y - Main.instance.getScrHeight( )/2;
	}
}