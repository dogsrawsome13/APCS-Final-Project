package info.finalproject.weapon;

import java.awt.*;

import info.finalproject.actor.*;
import info.finalproject.gui.*;

public class RPG extends Weapon {

	private int mySpeed;

	public RPG() {
		super(0, 0, 0, 0, 0, null, null);
		super.setAttack(20);
		mySpeed = 5;
	}

	public RPG(double x, double y, double direction, int width, int height, String imageName, Board board) {
		super(x, y, direction, width, height, imageName, board);
		super.setAttack(20);
		mySpeed = 5;
	}

	public int getSpeed() {
		return mySpeed;
	}

	public String toString() {
		return "RPG" + super.getX() + " " + super.getY();
	}

	public void explode(Player player) {

		for (Actor actor : getBoard().getActors()) {

			if (getBoard().checkCollisions(player, actor)) {
				hit(player);
			}
		}
	}
}
