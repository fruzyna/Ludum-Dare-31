package com.snake.ld31.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import com.snake.ld31.Main;
import com.snake.ld31.View;

public class Logo extends View
{
	private BufferedImage img;
	private long createTime = System.currentTimeMillis( );
	
	@Override
	public void draw(Graphics2D draw)
	{
		draw.setColor( Color.red );
		draw.fillRect( 0, 0, Main.instance.getScrWidth( ), Main.instance.getScrHeight( ) );
		
		draw.drawImage(img, Main.instance.getScrWidth( )/2 - img.getWidth( )/2, Main.instance.getScrHeight( )/2 - img.getHeight( )/2, null );
		draw.setFont(new Font("TimesRoman", Font.PLAIN, 40));
		draw.drawString("Presents", Main.instance.centerText("Presents", draw), 100);
	}

	@Override
	public void run( float deltaTime )
	{
		if (System.currentTimeMillis() > createTime + 2500)
			Main.currentView = new MainMenu( );
	}

	@Override
	public void init()
	{
		img = Main.imgLoader.load( "logo.png" );
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		
	}

	@Override
	public void mouseClick(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

}
