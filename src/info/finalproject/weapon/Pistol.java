package info.finalproject.weapon;

import java.awt.Image;

public class Pistol extends Weapon{
	
	private int myAttack;
    private int mySpeed;

    public Pistol(double x, double y, double direction, int w, int h, String imageName){
    	super(x, y, direction, w, h, imageName);
    	myAttack = 10;
    	mySpeed = 10;
    	
    	super.setHeight(10);
    	super.setWidth(10);
    }
    
	public void move(){
		super.setX(super.getX() + (int) (Math.cos(super.getDirection()) * mySpeed));
	    super.setY(super.getY() + (int) (Math.sin(super.getDirection()) * mySpeed));
	}
<<<<<<< HEAD
	
=======
>>>>>>> 77c951c58e3fc8409c58f643b2c3cf2bfe83c86c
}
