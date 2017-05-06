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
	private int myDx;
	private int myDy;
	private Image myImage;
	
	public Actor()
	{
		myHeight = 50;
		myWidth = 50;
		myX = 100;
		myY = 100;
	    ImageIcon ii = new ImageIcon("images/shooter.png");
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
    
    public int getDx()
    {
    	return myDx;
    }
    
    public int getDy()
    {
    	return myDy;
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
    
    public void setDx(int dx)
    {
    	myDx = dx; 
    }
    
    public void setDy(int dy)
    {
    	myDy = dy;
    }
    
    
    public void move()
    {
        myX += myDx;
        myY += myDy;
    }
    




    

}
    
	
	
	
	


