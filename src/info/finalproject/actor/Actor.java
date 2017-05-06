package info.finalproject.actor;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.*;

import info.finalproject.map.*;

public class Actor 
{
	private int myHeight;
	private int myWidth;
	private int myX;
	private int myY;
	private boolean visible;
	private Image myImage;
	
	public Actor()
	{
		myHeight = 50;
		myWidth = 50;
		myX = 100;
		myY = 100;
		visible = true;
		loadImage("");
	}
	
	public void loadImage(String imageName)
	{
	    ImageIcon ii = new ImageIcon(imageName);
	    myImage = ii.getImage();
	}
	
	public int getHeight()
	{
		return myHeight;
	}
	
	public int getWidth()
	{
		return myWidth;
	}
	
    
    public int getX() 
    {
        return myX;
    }

    public int getY() 
    {
        return myY;
    }
    
    public Image getImage()
    {
        return myImage;
    }
    
    public void setHeight(int height)
    {
    	myHeight = height;
    }
    
    public void setWidth(int width)
    {
    	myWidth = width;
    }
    
    public void setX(int x)
    {
    	myX = x;
    }
    
    public void setY(int y)
    {
    	myY = y;
    }
    
    public boolean isVisible()
    {
    	return visible;
    }
    
    public void setVisible(boolean vis)
    {
    	visible = vis;
    }
   
}
    
	
	
	
	


