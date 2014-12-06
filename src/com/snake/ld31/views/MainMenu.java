package com.snake.ld31.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.snake.ld31.View;
import com.snake.ld31.Main;

public class MainMenu extends View
{
	@Override
	public void draw(Graphics2D draw)
	{
		draw.setColor( Color.BLACK );
		
		String gameName = "PUT GAME NAME HERE!";
		String tell = "Press Enter to Play!";
		draw.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		draw.drawString(gameName, Main.instance.centerText(gameName, draw), 50);
		draw.drawString(tell, Main.instance.centerText(tell, draw), 250);
	}

	@Override
	public void run( float deltaTime )
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
