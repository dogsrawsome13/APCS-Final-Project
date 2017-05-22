package info.finalproject.weapon;

import info.finalproject.actor.Player;
import info.finalproject.actor.Powerup;
import info.finalproject.gui.*;

public class Weapon extends Powerup {
	private int boardHeight;
    private int boardWidth;
    private int mySpeed;
    private int myAttack;

	public Weapon(double x, double y, double direction, int w, int h, String imageName, Board board) {
		super(x, y, direction, w, h, imageName, board);

		boardHeight = 2000;
		boardWidth = 2000;
		mySpeed = 10;
		myAttack = 0;
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
	public void move() {
		super.setX(super.getX() + (int) (Math.cos(super.getDirection()) * mySpeed));
	    super.setY(super.getY() + (int) (Math.sin(super.getDirection()) * mySpeed));
	}
	public void hit(Player player) {
		player.loseHealth(myAttack);
	}
}
