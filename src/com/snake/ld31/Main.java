package com.snake.ld31;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.snake.ld31.views.Logo;
import com.snake.ld31.views.Mainmenu;

class Main implements KeyListener, Runnable
{
	JFrame frame;
	Paint paint;
	Thread thread;
	View currentView;
	Boolean running = true;
	
	public static void main( String[] args )
	{
		new Main();
	}

	public Main()
	{
		frame = new JFrame("Ludum Dare 31");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(this);
		paint = new Paint();
		paint.setPreferredSize(new Dimension(500, 500));
		frame.setResizable(false);
		frame.add(paint);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		currentView = new Logo();
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run()
	{
		paint.repaint();
		try
		{
			Thread.sleep(2500);
		} catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}
		currentView = new Mainmenu();
		while(running)
		{
			paint.repaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public class Paint extends JPanel
	{
		private static final long serialVersionUID = -762335263483583884L;

		public void paint(Graphics g)
		{	
			Graphics2D draw = (Graphics2D)g;
			draw.setColor(Color.WHITE);
			draw.fillRect(0,0,500,500);
			currentView.draw(draw);
		}
	}
	
	@Override
	public void keyPressed(KeyEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
}