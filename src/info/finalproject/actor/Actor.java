package info.finalproject.actor;

import java.awt.*;
import javax.swing.*;

public class Actor 
{
	private int myHeight;
	private int myWidth;
	private double myX;
	private double myY;
	private double myDegrees;
	private boolean visible;
	private Image myImage;
	
	public Actor(double x, double y, double degrees, String imageName)
	{
		loadImage(imageName);
		myHeight = myImage.getHeight(null);
		myWidth = myImage.getWidth(null);
		myX = x;
		myY = y;
		myDegrees = degrees;
		visible = true;
	}
	
	public Actor(double x, double y, int w, int h, String imageName)
	{
		loadImage(imageName);
		myHeight = h;
		myWidth = w;
		myX = x;
		myY = y;
		visible = true;
		
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
	
    
    public double getX() 
    {
        return myX;
    }

    public double getY() 
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
    
    public void setX(double x)
    {
    	myX = x;
    }
    
    public void setY(double y)
    {
    	myY = y;
    }
    
    public void setDirection(double degrees)
    {
    	myDegrees = Math.toRadians(degrees);
    }
    
    public double getDirection()
    {
    	return myDegrees;
    }
    public boolean isVisible()
    {
    	return visible;
    }
    
    public void setVisible(boolean vis)
    {
    	visible = vis;
    }
    
    public void rotateLeft()
    {
        myDegrees = myDegrees - 5;
    }
    public void rotateRight()
    {
        myDegrees = myDegrees + 5;
    }
    
    public Rectangle getBounds()
    {
    	return new Rectangle((int) myX, (int) myY, myWidth, myHeight);
    }
    
    public String toString()
    {
    	return myX + "," + myY;
    }
   
}
    
	
	
	
	


