package info.finalproject.actor;

import info.finalproject.gui.*;
import info.finalproject.weapon.*;

public class Powerup extends Actor {

	private double powerup;

	public Powerup(double x, double y, double direction, int w, int h, String imageName, Board board) {
		super(x, y, direction, w, h, imageName, board);
		powerup = (Math.random() * 10);

	}

	public Actor givePowerup(Player player) {
		// if(powerup <= 1)
		// return new Pistol(this.getX(), this.getY(), this.getDirection(),
		// this.getWidth(), this.getHeight(), "Pistol.png", getBoard());
		// else
		if (powerup <= 3.33)
			return new MachineGun(this.getX(), this.getY(), this.getDirection(), this.getWidth(), this.getHeight(),
					"images/missile.png", getBoard());
		else if (powerup <= 6.66)
			return new RPG(this.getX(), this.getY(), this.getDirection(), this.getWidth(), this.getHeight(),
					"images/missile.png", getBoard());
		else if (powerup <= 10) {
			giveHealth(player);
			return player.getWeapon();
		}
		else
			return null;
	}
	private void giveHealth(Player player) {
		double health = Math.random() * 25 + 1;
		if (player.getHealth() + health > 100)
			player.setHealth(100);
		else
			player.setHealth(player.getHealth() + (int) health);
	}

	public Powerup removeSelf() {
		setVisible(false);
		return this;
	}
	

}
