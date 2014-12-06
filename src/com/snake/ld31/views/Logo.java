package com.snake.ld31.views;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
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
		draw.drawImage(img, 0, 0, 500, 500, null);
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

}
