package info.finalproject.weapon;

import java.awt.Image;

import info.finalproject.gui.*;

public class Pistol extends Weapon {

	private int mySpeed;

	public Pistol(double x, double y, double direction, int w, int h, String imageName, Board board) {
		super(x, y, direction, w, h, imageName, board);
		super.setAttack(10);
		super.setHeight(10);
		super.setWidth(10);
		mySpeed = 8;

	}

	public int getSpeed() {
		return mySpeed;
	}

	public String toString() {
		return "Pistol";
	}
}
