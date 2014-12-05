package com.snake.ld31;

import java.awt.Graphics2D;

public abstract class View
{
	public abstract void draw(Graphics2D draw);
	
	public abstract void run();
	
	public abstract void init();
	
	public View()
	{
		init();
	}
}
