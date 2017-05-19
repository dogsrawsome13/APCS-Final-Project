package info.finalproject.actor;

import info.finalproject.gui.*;
import info.finalproject.weapon.*;

public class Powerup extends Actor {
	
	private double powerup;
	
	public Powerup(double x, double y, double direction, int w, int h, String imageName, Board board) {
		super(x, y, direction, w, h, imageName, board);
		powerup = (Math.random() * 3);
		
	}
	public Actor givePowerup() {
		//if(powerup <= 1)
			//return new Pistol(this.getX(), this.getY(), this.getDirection(), this.getWidth(), this.getHeight(), "Pistol.png", getBoard());
		//else
		if(powerup <= 2)
			return new MachineGun(this.getX(), this.getY(), this.getDirection(), this.getWidth(), this.getHeight(), "Pistol.png", getBoard());
		else if(powerup <= 3)
			return new RPG(this.getX(), this.getY(), this.getDirection(), this.getWidth(), this.getHeight(), "Pistol.png", getBoard());
		else
			return null;
	}
	public Powerup removeSelf() {
		setVisible(false);
		return this;
	}
}
