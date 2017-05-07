package info.finalproject.actor;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import info.finalproject.weapon.Pistol;
import info.finalproject.weapon.Weapon;

public class Player extends Actor
{
	private int myHealth;
	private int mySpeed;
	private double myDegrees;
	Weapon myWeapon;
	private ArrayList<Weapon> myAmmo;
	
	public Player(int x, int y, String imageName, int health, int ammo, 
			int speed, double degrees, Weapon weapon)
	{
		super(x, y, imageName);
		myHealth = health;
		mySpeed = speed;
		myDegrees = degrees;
		myAmmo = new ArrayList();
		
		if (weapon instanceof Pistol)
			myWeapon = new Pistol(super.getX(), super.getY(), "missiles.png", myDegrees);
			
	}
	
	public int getHealth()
	{
		return myHealth;
	}
	
	public int getSpeed()
	{
		return mySpeed;
	}
	public double getDegrees()
	{
		return myDegrees;
	}
	
	public Weapon getWeapon()
	{
		return myWeapon;
	}
    public ArrayList getAmmo()
    {
    	return myAmmo;
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
    
    
    public void fire()
    {
		if (myWeapon instanceof Pistol)
			myAmmo.add(new Pistol(super.getX() + super.getImage().getWidth(null) / 2,
					super.getY() + super.getImage().getHeight(null) / 2,
					"images/missile.png", myDegrees));
    }
    
	
    public void keyPressed(KeyEvent e)
    {

        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_SPACE)
        {
            fire();
        }
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
