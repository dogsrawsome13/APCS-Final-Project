package info.finalproject.weapon;

import java.awt.Image;

import info.finalproject.gui.*;

public class Pistol extends Weapon {
	
	private int myAttack;
    private int mySpeed;

    public Pistol(double x, double y, double direction, int w, int h, String imageName, Board board) {
    	super(x, y, direction, w, h, imageName, board);
    	myAttack = 10;
    	mySpeed = 10;
    	super.setHeight(10);
    	super.setWidth(10);
    }
    
	public void move() {
		super.setX(super.getX() + (int) (Math.cos(super.getDirection()) * mySpeed));
	    super.setY(super.getY() + (int) (Math.sin(super.getDirection()) * mySpeed));
	}
	
	public String toString() {
		return "Pistol";
	}
}
