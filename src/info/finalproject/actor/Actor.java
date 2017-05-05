package info.finalproject.actor;

import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.*;

import info.finalproject.map.*;

public class Actor 
{
	private int myHeight;
	private int myWidth;
	private int myDirection;
	private int x;
	private int y;
	private int dx;
	private int dy;
	private Image myImage;
	
	public Actor()
	{
		myHeight = 50;
		myWidth = 50;
		myDirection = 0;
		x = 40;
		y = 60;
	    ImageIcon ii = new ImageIcon("images/shooter.png");
	    myImage = ii.getImage();
	}
	
    public void move()
    {
        x += dx;
        y += dy;
    }
    
    public int getX() 
    {
        return x;
    }

    public int getY() 
    {
        return y;
    }

    public Image getImage()
    {
        return myImage;
    }
    
    public void keyPressed(KeyEvent e)
    {

        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT)
        {
            dx = -1;
        }

        if (key == KeyEvent.VK_RIGHT)
        {
            dx = 1;
        }

        if (key == KeyEvent.VK_UP) 
        {
            dy = -1;
        }

        if (key == KeyEvent.VK_DOWN)
        {
            dy = 1;
        }
    }
    
    public void keyReleased(KeyEvent e)
    {
        
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_UP) {
            dy = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
    }
}
    
	
	
	
	


