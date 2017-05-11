package info.finalproject.weapon;

import java.awt.Image;

public class Pistol extends Weapon
{
	private int myAttack;
    private int mySpeed;
    private double x;
    private double y;
  
    
    public Pistol(double x, double y, double direction, int w, int h, String imageName)
    {
    	super(x, y, direction, w, h, imageName);
    	myAttack = 10;
    	mySpeed = 20;
    	super.setHeight(50);
    	super.setWidth(50);
    }
    
	public void move()
	{
		super.setX(super.getX() + (int) (Math.cos(super.getDirection()) * mySpeed));
	    super.setY(super.getY() + (int) (Math.sin(super.getDirection()) * mySpeed));
	}
	


}
