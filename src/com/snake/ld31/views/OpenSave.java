package com.snake.ld31.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;

import com.snake.ld31.DataContainer;
import com.snake.ld31.IO;
import com.snake.ld31.Main;
import com.snake.ld31.View;

public class OpenSave extends View
{
	File[] files;
	Graphics2D draw; 
	
	@Override
	public void draw(Graphics2D draw)
	{
		if(files != null && files.length > 0)
		{
			draw.setColor(Color.ORANGE);
			//replace with some kind of folder that takes up most the screen with a piece of paper on it
			draw.fillRect(0, 0, Main.instance.getScrWidth(), Main.instance.getScrHeight());
			draw.setColor(Color.WHITE);
			draw.fillRect((int) (Main.instance.getScrWidth()*.05), (int) (Main.instance.getScrHeight()*.05), (int) (Main.instance.getScrWidth() - Main.instance.getScrWidth()*.1), (int) (Main.instance.getScrHeight() - Main.instance.getScrHeight()*.1));
			draw.setColor(Color.BLACK);
			draw.setFont(new Font("Arial", Font.PLAIN, 20));
			this.draw = draw;
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm");
			for(int i = 0; i < files.length; i++)
			{
				draw.drawString(files[i].getName().toUpperCase(), (int)(Main.instance.getScrWidth()*.1), (int)(Main.instance.getScrHeight()*.1 + (i+1)*10 + i*(draw.getFontMetrics(draw.getFont()).getHeight())));
				draw.drawString(sdf.format(files[i].lastModified()), (int)(Main.instance.getScrWidth()*.6), (int)(Main.instance.getScrHeight()*.1 + (i+1)*10 + i*(draw.getFontMetrics(draw.getFont()).getHeight())));
			}
		}
		else
		{
			DataContainer.worldHeight = 10;
			DataContainer.worldWidth = 10;
			DataContainer.worldName = "temp name";
			Main.instance.startGame();
		}
	}

	@Override
	public void run(float deltaTime)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init()
	{
		files = IO.getSaves();
		
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClick(MouseEvent e)
	{
		if(e.getX() > Main.instance.getScrWidth()*.1 && e.getX() < Main.instance.getScrWidth()*.9)
		{
			for(int i = 0; i < files.length; i++)
			{
				if(e.getY() > (int)(Main.instance.getScrHeight()*.1 + (i-1)*10 - 5 + i*(draw.getFontMetrics(draw.getFont()).getHeight())) &&
						e.getY() < (int)(Main.instance.getScrHeight()*.1 + (i)*10 - 5 + (i+1)*(draw.getFontMetrics(draw.getFont()).getHeight())))
				{
					IO.loadSave(files[i]);
				}
			}
		}
		
	}

}
