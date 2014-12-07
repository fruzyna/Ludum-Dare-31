package com.snake.ld31;

public class Camera
{
	public float x,y;
	public float scale;
	
	private float aX, aY;
	private float aScale;
	
	public Camera( )
	{
		x = 0.0f;
		y = 0.0f;
		scale = 1.0f;
		
		aX = 0.0f;
		aY = 0.0f;
		aScale = 1.0f;
	}
	
	public void update( float timestep )
	{
		aX += (x - aX) * timestep * 5;
		aY += (y - aY) * timestep * 5;
		
		aScale += (scale - aScale) * timestep * 5;
	}
	
	public void set( )
	{
		aX = x;
		aY = y;
		aScale = scale;
	}
		
	public float getScale( )
	{
		return aScale;
	}
	
	public float getX( )
	{
		return aX;
	}
	
	public float getY( )
	{
		return aY;
	}
	
	public float width( )
	{
		return (float)Main.instance.getScrWidth( );
	}
	
	public float height( )
	{
		return (float)Main.instance.getScrHeight( );
	}
	
	public float getRasterX( float worldX )
	{	
		return (worldX - aX) * aScale;
	}
	
	public float getRasterY( float worldY )
	{
		return (worldY - aY) * aScale;
	}
	
	public float getWorldX( float screenX )
	{
		return aX + (screenX / aScale);
	}
	
	public float getWorldY( float screenY )
	{
		return aY + (screenY / aScale);
	}
	
	public void setCameraRect( float centerX, float centerY, float width, float height )
	{
		this.x = centerX + (128 * getScale())/2;
		this.y = centerY;
		
		float w = width( ) / width;
		float h = height( ) / height;
		
		float m = Math.min( h, w );
		
		scale = m;
	}
}