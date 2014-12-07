package com.snake.ld31;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.snake.ld31.views.Game;
import com.snake.ld31.views.Logo;
import com.snake.ld31.views.MainMenu;
import com.snake.ld31.views.OpenSave;

public class Main implements KeyListener, Runnable, MouseListener, MouseMotionListener
{
	public static Main instance;
	public static View currentView;
	public static ImageLoader imgLoader;
	public static Camera camera;
	public static int mouseX, mouseY;
	public static float ticks;
	
	private long deltaTime;
	
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
		return 1024;
	}

	public int getScrHeight( )
	{
		return 768;
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
		frame = new JFrame("Ludum Dare 31: Tower Game");
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
				IO.saveSave();
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
		
		instance = this;
		imgLoader = new ImageLoader( );
		
		camera = new Camera( );
		
		goToLogo();
		paint.addMouseListener(this);
		paint.addMouseMotionListener( this );
		
		thread = new Thread(this);
		thread.start();
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
			Thread.sleep(5);
		} 
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
		
		}
		
		goToMain();
		
		while (running)
		{
			
			deltaTime = System.currentTimeMillis() - lastTickTime;
			lastTickTime = System.currentTimeMillis();
			
			float deltaTimeFloat = ((float)deltaTime / 1000.0f);
			
			ticks += deltaTimeFloat;
			
			camera.update( deltaTimeFloat );
			
			onTick( deltaTimeFloat );
			paint.repaint();
			
			try 
			{
				Thread.sleep(5);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		
		cleanup( );
	}

	public void startGame()
	{
		currentView = new Game();
	}

	public void goToMain()
	{
		currentView = new MainMenu();
	}
	
	public void saveLoad()
	{
		currentView = new OpenSave();
	}
	
	public void goToLogo()
	{
		currentView = new Logo();
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
			
			draw.setColor(Color.YELLOW);
			draw.setFont(new Font("Arial", Font.PLAIN, 40));
			//draw.drawString(String.valueOf( (float)(32 - deltaTime) * 1.875f ), 10, 40);
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
	public void keyTyped(KeyEvent arg0){}

	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		currentView.mouseClick(arg0);
	}

	@Override
	public void mouseEntered(MouseEvent arg0){ }

	@Override
	public void mouseExited(MouseEvent e){ }

	@Override
	public void mousePressed(MouseEvent e){ }

	@Override
	public void mouseReleased(MouseEvent e){ }

	@Override
	public void mouseDragged(MouseEvent e)
	{
		mouseX = e.getX( );
		mouseY = e.getY( );
	}

	@Override
	public void mouseMoved(MouseEvent e) 
	{
		mouseX = e.getX();
		mouseY = e.getY();
	}
}
