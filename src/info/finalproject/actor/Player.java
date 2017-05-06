package info.finalproject.actor;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class Player extends Actor
{
	private int myHealth;
	private int myAmmo;
	private int mySpeed;
	private double myDegrees;
	
	public Player(int health, int ammo, int speed, double degrees)
	{
		super();
		myHealth = health;
		myAmmo = ammo;
		mySpeed = speed;
		myDegrees = degrees;
	}
	
	public int getHealth()
	{
		return myHealth;
	}
	
	public int getAmmo()
	{
		return myAmmo;
	}
	public int getSpeed()
	{
		return mySpeed;
	}
	public double getDegrees()
	{
		return myDegrees;
	}
	
    public void moveForward()
    {
        super.setX(super.getX() + (int) (Math.sin(myDegrees * (Math.PI/180)) * mySpeed));
        super.setY(super.getY() + (int) (Math.cos(myDegrees * (Math.PI/180)) * -mySpeed));
    }
    public void moveBackward()
    {
        super.setX(super.getX() - (int) (Math.sin(myDegrees * (Math.PI/180)) * mySpeed));
        super.setY(super.getY() - (int) (Math.cos(myDegrees * (Math.PI/180)) * -mySpeed));
    }
    public void rotateLeft()
    {
        myDegrees = myDegrees - 5;
    }
    public void rotateRight()
    {
        myDegrees = myDegrees + 5;
    }
	
    public void keyPressed(KeyEvent e)
    {

        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT)
        {
        	rotateLeft();

        }

        if (key == KeyEvent.VK_RIGHT)
        {
            rotateRight();
        }

        if (key == KeyEvent.VK_UP) 
        {
            moveForward();
        }

        if (key == KeyEvent.VK_DOWN)
        {
            moveBackward();
        }
    }
    
   
	


}
