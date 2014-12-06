package com.snake.ld31;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.snake.ld31.views.Game;
import com.snake.ld31.views.Logo;
import com.snake.ld31.views.MainMenu;

public class Main implements KeyListener, Runnable
{
	public static Main instance;
	public static View currentView;
	
	private JFrame frame;
	private Paint paint;
	private Thread thread;
	private boolean running = true;
	
	private long lastTickTime = System.currentTimeMillis();
	
	public static void main( String[] args )
	{
		new Main();
	}
	
	public int getScrWidth( )
	{
		return 500;
	}

	public int getScrHeight( )
	{
		return 500;
	}
	
	public int getStringWidth(String s, Graphics2D draw)
	{
		FontMetrics metrics = paint.getFontMetrics(draw.getFont());
		return metrics.stringWidth(s);
	}
	
	public int centerText(String s, Graphics2D draw)
	{
		return getScrWidth()/2 - (getStringWidth(s, draw)/2);
	}
	
	public Main()
	{
		frame = new JFrame("Ludum Dare 31");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addKeyListener(this);
		
		frame.addWindowListener( new WindowListener()
		{
			@Override
			public void windowActivated(WindowEvent e) { }

			@Override
			public void windowClosed(WindowEvent e){ }

			@Override
			public void windowClosing(WindowEvent e)
			{
				running = false;
			}

			@Override
			public void windowDeactivated(WindowEvent e) { }

			@Override
			public void windowDeiconified(WindowEvent e) { }

			@Override
			public void windowIconified(WindowEvent e) { }

			@Override
			public void windowOpened(WindowEvent e) { }
		});
		
		paint = new Paint();
		paint.setPreferredSize(new Dimension(getScrWidth( ), getScrHeight( )));
		
		frame.setResizable(false);
		frame.add(paint);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		currentView = new Logo();
		
		thread = new Thread(this);
		thread.start();
		
		instance = this;
	}
	
	private void cleanup( )
	{
		frame.setVisible( false );
		frame.dispose( );
	}
	
	public void onTick( float deltaTime )
	{
		if (currentView != null)
			currentView.run( deltaTime );
	}
	
	@Override
	public void run()
	{
		paint.repaint();
		
		try
		{
			Thread.sleep(2500);
		} 
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
		
		}
		
		currentView = new MainMenu(); 
		
		while (running)
		{
			long deltaTime = System.currentTimeMillis() - lastTickTime;
			lastTickTime = System.currentTimeMillis();
			
			float deltaTimeFloat = ((float)deltaTime / 1000.0f);
			
			onTick( deltaTimeFloat );
			paint.repaint();
			
			try 
			{
				Thread.sleep(1);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		
		cleanup( );
	}

	public static void startGame()
	{
		currentView = new Game();
	}
	
	public class Paint extends JPanel
	{
		private static final long serialVersionUID = -762335263483583884L;

		public void paint(Graphics g)
		{	
			Graphics2D draw = (Graphics2D)g;
			draw.setColor(Color.WHITE);
			draw.fillRect( 0, 0, getScrWidth( ), getScrHeight( ) );
			
			if (currentView != null)
				currentView.draw(draw);
		}
	}
	
	@Override
	public void keyPressed(KeyEvent arg0)
	{
		currentView.keyPressed(arg0);
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{
		currentView.keyReleased(arg0);
	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
}