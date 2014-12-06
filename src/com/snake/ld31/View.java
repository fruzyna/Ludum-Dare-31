package com.snake.ld31;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public abstract class View
{
	public abstract void draw(Graphics2D draw);
	
	public abstract void run( float deltaTime );
	
	public abstract void init();
	
	public abstract void keyPressed(KeyEvent e);

	public abstract void keyReleased(KeyEvent e);
	
	public abstract void mouseClick(MouseEvent e);
	
	public View()
	{
		init();
	}
}
