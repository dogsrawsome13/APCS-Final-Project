package info.finalproject.weapon;

import java.awt.Rectangle;

import info.finalproject.actor.Actor;
import info.finalproject.actor.Player;
import info.finalproject.actor.Powerup;
import info.finalproject.gui.*;

public class Weapon extends Powerup {
	private int boardHeight;
	private int boardWidth;
	private int myAttack;

	public Weapon(double x, double y, double direction, int w, int h, String imageName, Board board) {
		super(x, y, direction, w, h, imageName, board);

		boardHeight = 2000;
		boardWidth = 2000;
		myAttack = 1;
	}

	public int getBoardWidth() {
		return boardWidth;
	}

	public double getBoardHeight() {
		return boardHeight;
	}

	public void setAttack(int attack) {
		myAttack = attack;
	}

	public void move(int speed) {
		super.setX(super.getX() + (int) (Math.cos(super.getDirection()) * speed));
		super.setY(super.getY() + (int) (Math.sin(super.getDirection()) * speed));
	}
	
	public boolean isHit(Actor actor) {
		Rectangle r1 = actor.getBounds();
		if (getBounds().intersects(r1))
			return true;
		else
			return false;
	}

	public void hit(Player player) {
		player.loseHealth(myAttack);
	}
}
