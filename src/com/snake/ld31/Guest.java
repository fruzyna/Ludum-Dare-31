package com.snake.ld31;

import java.awt.Color;
import java.awt.Graphics2D;

public class Guest
{
	private float x;
	private int floor;
	private boolean hasCheckedIn;
	
	private Room hotelRoom;
	private Room target;
	private Room elevator;
	
	private int hunger, entertainment, sleep;
	
	private long checkInTime;
	private float feet;
	
	private Color face, top, bottom;
	
	public Guest( Room hotelRoom )
	{
		x = DataContainer.worldWidth * 128;
		floor = 0;
		
		hasCheckedIn = false;
		checkInTime = 0;
		
		this.hotelRoom = hotelRoom;
		target = null;
		elevator = null;
		
		hunger = Main.rnd.nextInt(100);
		entertainment = Main.rnd.nextInt(100);
		sleep = Main.rnd.nextInt(100);
		
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
				hasCheckedIn = true;
		}
		else
			x += ((Main.rnd.nextFloat() * 100.0f) - 50.0f) * delta;
		
		feet += (x - oldX) * (Main.rnd.nextFloat() * 0.1 + 0.1);
	}
	
	private int getY( )
	{
		return (60 - floor);
	}
	
	private Room findNearestElevator( )
	{
		int startX = (int)(x/128.0f);
		int startY = getY( );
		
		return null;
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