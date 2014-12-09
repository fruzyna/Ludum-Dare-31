package com.snake.ld31;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;

import javax.imageio.ImageIO;

public class ImageLoader
{
	private final String baseDirectory = "/res/";
	private Vector<BitmapNode> BufferedImages = new Vector<BitmapNode>();
	
	private int fileIsLoaded( String filename )
	{
		for (int i=0;i < BufferedImages.size();i++)
		{
			if (BufferedImages.get(i).is(baseDirectory + filename)){ return i; }
		}
		
		return -1;
	}
	
	private void loadBufferedImage( String filename )
	{
		int num = fileIsLoaded(filename);
		
		if (num == -1)
		{
			BufferedImages.add( new BitmapNode(baseDirectory + filename) );
		}
	}
	
	public BufferedImage load( String filename )
	{
		int num = fileIsLoaded(filename);
		
		if (num == -1)
		{
			loadBufferedImage(filename);
			num = BufferedImages.size()-1;
		}
		
		return BufferedImages.get(num).getData();
	}
	
	private class BitmapNode
	{
		private BufferedImage imgData;
		private String filename;
		
		public BitmapNode( String filename )
		{
			this.filename = filename;
			
			try 
			{		
				imgData = javax.imageio.ImageIO.read( Main.class.getResource(filename) );
			} 
			catch (Exception e) 
			{
				System.out.println("Error loading " + filename);
			}
			
		}
		
		public boolean is( String otherfile )
		{
			if (filename.equals(otherfile)){ return true; }
			return false;
		}
		
		public BufferedImage getData()
		{
			return imgData;
		}
	}
}