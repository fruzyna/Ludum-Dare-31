package com.snake.ld31.views;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import com.snake.ld31.Main;
import com.snake.ld31.View;

//sky colour - 142 255 253

public class Game extends View
{
	private BufferedImage grass;
	private BufferedImage arrowLeft;
	private BufferedImage arrowRight;
	private BufferedImage arrowUp;
	
	private BufferedImage cloud1;
	private BufferedImage cloud2;
	
	private Color skyColor;
	
	@Override
	public void draw(Graphics2D draw)
	{
		draw.setColor( skyColor );
		draw.fillRect( 0, 0, Main.instance.getScrWidth(), Main.instance.getScrHeight() );
		
		for (int i=0;i < 10;++i)
		{
			int y = 60 + i * 40;
			int x = (int)(Main.ticks * 10) + (int)(Math.sin(i) * Main.instance.getScrWidth( ));
			x -= Math.floor(x / (Main.instance.getScrWidth( ) + cloud1.getWidth( ))) * (Main.instance.getScrWidth( ) + cloud1.getWidth( ));
			x -= cloud1.getWidth( );
			
			BufferedImage cloud = null;
			if (i % 2 == 0)
				cloud = cloud1;
			else
				cloud = cloud2;
			
			
			draw.drawImage( cloud, x, (int)( y + Math.sin(Main.ticks/5 + i) * 6), null );
		}
	}

	@Override
	public void run( float deltaTime )
	{
		
	}

	@Override
	public void init()
	{
		grass =			Main.imgLoader.load("grass.png");
		arrowLeft = 	Main.imgLoader.load("arrow_left.png");
		arrowRight = 	Main.imgLoader.load("arrow_right.png");
		arrowUp	=		Main.imgLoader.load("arrow_up.png");
		
		cloud1 =		Main.imgLoader.load("cloud.png");
		cloud2 =		Main.imgLoader.load("cloud2.png");
		
		skyColor = new Color( 142, 255, 253 );
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			Main.instance.goToMain();
		}
	}

}
