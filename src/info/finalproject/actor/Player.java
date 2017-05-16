package info.finalproject.actor;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import info.finalproject.gui.Board;
import info.finalproject.weapon.Pistol;
import info.finalproject.weapon.Weapon;

public class Player extends Actor {
	private int myHealth;
	private int mySpeed;
	private int tmpLoad;
	private Weapon myWeapon;
	private ArrayList<Weapon> bullets;
	private boolean moveForward, canForward, canBackward, moveBackward, left,
    right, fire, special;
	
	public Player(int x, int y, double degrees, int width, int height, String imageName, 
			int speed, Weapon weapon) {
		super(x, y, degrees, width, height, imageName);
		myHealth = 100;
		mySpeed = speed;
		bullets = new ArrayList<Weapon>();
		tmpLoad = 0;
		myWeapon = weapon;
	}
	
	public int getHealth() {
		return myHealth;
	}
	
	public int getSpeed() {
		return mySpeed;
	}
	
	public Weapon getWeapon() {
		return myWeapon;
	}
	
	public void setWeapon(Weapon weapon) {
		myWeapon = weapon;
	}
    public ArrayList<Weapon> getBullets() {
    	return bullets;
    }
	
    public void moveForward(int sx, int sy) {
        super.setX(super.getX() + Math.cos(super.getDirection()) * sx);
        super.setY(super.getY() + Math.sin(super.getDirection()) * sy);
    }
    public void moveBackward(int sx, int sy) {
    	super.setX(super.getX() - Math.cos(super.getDirection()) * sx);
        super.setY(super.getY() - Math.sin(super.getDirection()) * sy);
    }

    
    
    public void fire(int load, int number, int spread) {
        
        // if reloading time is done
        if (tmpLoad == 0) { 
           for (int i = 0; i < number; i++) {
              // setting the bullet
              Board.bullet.setX(super.getX() + super.getWidth());
              Board.bullet.setY(super.getY() + super.getHeight() / 2);
              Board.bullet.setDirection(Math.toDegrees(super.getDirection()));
              Board.bullet.setWidth(10);
              Board.bullet.setHeight(10);
              // adding the bullet to the array list
              bullets.add(new Weapon(Board.bullet.getX(), Board.bullet.getY(), Board.bullet.getDirection(), 
                      Board.bullet.getWidth(), Board.bullet.getHeight(), "images/missile.png"));
           }
           //reset the reload time 
           tmpLoad = load;
        }
        else
           tmpLoad -= 1;  
    }
    public String toString() {
    	return super.toString() + ", " + myWeapon;
    }
}
