package com.snake.ld31.views;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.snake.ld31.View;
import com.snake.ld31.Main;

public class MainMenu extends View
{
	Main main;
	
	@Override
	public void draw(Graphics2D draw)
	{
		draw.setColor( Color.BLACK );
		
		String gameName = "PUT GAME NAME HERE!";
		String tell = "Press Enter to Play!";
		draw.drawString(gameName, main.centerText(gameName), 50);
		draw.drawString(tell,  main.centerText(tell), 250);
	}

	@Override
	public void run( float deltaTime )
	{
		main = Main.instance;
	}

	@Override
	public void init()
	{
		main = Main.instance;
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
