package info.finalproject.actor;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import info.finalproject.gui.Board;
import info.finalproject.weapon.MachineGun;
import info.finalproject.weapon.Pistol;
import info.finalproject.weapon.Weapon;

public class Player extends Actor
{
	private int myHealth;
	private int mySpeed;
	private int tmpLoad;
	private Weapon myWeapon;
	private ArrayList bullets;
	private boolean moveForward, canForward, canBackward, moveBackward, left,
    right, fire, special;
	
	public Player(int x, int y, double degrees, int width, int height, String imageName, 
			int speed){
		super(x, y, degrees, width, height, imageName);
		myHealth = 100;
		mySpeed = speed;
		myWeapon = new Pistol(super.getX(), super.getY(), super.getDirection(),
				super.getWidth(), super.getHeight(), null);
		bullets = new ArrayList();
		tmpLoad = 0;
	}
	
	public int getHealth(){
		return myHealth;
	}
	
	public int getSpeed(){
		return mySpeed;
	}
	
	public Weapon getWeapon(){
		return myWeapon;
	}
	

    public ArrayList<Weapon> getBullets(){
    	return bullets;
    }
	
    public void moveForward(int sx, int sy){
        super.setX(super.getX() + Math.cos(super.getDirection()) * sx);
        super.setY(super.getY() + Math.sin(super.getDirection()) * sy);
    }
    public void moveBackward(int sx, int sy){
    	super.setX(super.getX() - Math.cos(super.getDirection()) * sx);
        super.setY(super.getY() - Math.sin(super.getDirection()) * sy);
    }

    
    
    public void fire(int load, int number, int spread){
        
        // if reloading time is done
        if (tmpLoad == 0){ 

           for (int i = 0; i < number; i++){
              // setting the bullet
              Board.bullet.setX(super.getX() + super.getWidth());
              Board.bullet.setY(super.getY() + super.getHeight() / 2);
              Board.bullet.setDirection(Math.toDegrees(super.getDirection()));
              Board.bullet.setWidth(10);
              Board.bullet.setHeight(10 );
              
              // adding the bullet to the array list
              if (myWeapon instanceof Pistol){
                  bullets.add(new Pistol(Board.bullet.getX(),
                          Board.bullet.getY(), Board.bullet.getDirection(), 
                          Board.bullet.getWidth(), Board.bullet.getHeight(), "images/missile.png"));
              }
              if (myWeapon instanceof MachineGun){
                  bullets.add(new MachineGun(Board.bullet.getX(),
                          Board.bullet.getY(), Board.bullet.getDirection(), 
                          Board.bullet.getWidth(), Board.bullet.getHeight(), "images/missile.png"));
              }
           }


           //reset the reload time 
           tmpLoad = load;
        }
        else 
        	tmpLoad -= 1;  
        
       

    }
    public void updateWeapon(Weapon weapon){
    	myWeapon = weapon;
    }
    
    public String typeOfWeapon(){
    	if (myWeapon instanceof Pistol)
    		return "pistol";
    	else
    		return "machinegun";
    }


}
