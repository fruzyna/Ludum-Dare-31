package com.snake.ld31.views;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.snake.ld31.View;
import com.snake.ld31.Main;

public class MainMenu extends View
{

	@Override
	public void draw(Graphics2D draw)
	{
		draw.drawString("PUT GAME NAME HERE!", 200, 50);
		draw.drawString("Press Enter to Play", 200, 250);
	}

	@Override
	public void run()
	{
		
	}

	@Override
	public void init()
	{
		
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			Main.startGame();
		}
	}
	
}
