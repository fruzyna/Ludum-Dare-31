package com.snake.ld31.views;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.snake.ld31.DataContainer;
import com.snake.ld31.Main;
import com.snake.ld31.View;

public class Game extends View
{

	@Override
	public void draw(Graphics2D draw)
	{
		int width = DataContainer.worldWidth;
		int scrWidth = Main.instance.getScrWidth();
		int height = DataContainer.worldHeight;
		int scrHeight = Main.instance.getScrHeight();

		draw.setColor(Color.LIGHT_GRAY);
		for(int i = 0; i < width; i++)
		{
			draw.drawLine((scrWidth/width)*i, 0, (scrWidth/width)*i, scrHeight);
		}
		for(int i = 0; i < width; i++)
		{
			draw.drawLine(0, (scrHeight/height)*i, scrWidth, (scrHeight/height)*i);
		}
	}

	@Override
	public void run( float deltaTime )
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init()
	{
		// TODO Auto-generated method stub
		
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

	@Override
	public void mouseClick(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

}
