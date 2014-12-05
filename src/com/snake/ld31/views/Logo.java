package com.snake.ld31.views;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.snake.ld31.Main;
import com.snake.ld31.View;

public class Logo extends View
{
	private BufferedImage img;
	private long createTime = System.currentTimeMillis( );
	
	@Override
	public void draw(Graphics2D draw)
	{
		draw.drawImage(img, 0, 0, 500, 500, null);
		//TODO find the string length and center the text plus make it much bigger
		draw.drawString("Presents", 225, 100);
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
	    try
		{
			img = ImageIO.read(new File("res/logo.png"));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		
	}

}
