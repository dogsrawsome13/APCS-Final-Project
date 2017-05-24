package info.finalproject.weapon;

import info.finalproject.gui.*;

public class MachineGun extends Weapon {

	private int mySpeed;

	public MachineGun() {
		super(0, 0, 0, 0, 0, null, null);
		super.setAttack(8);
		mySpeed = 14;
	}

	public MachineGun(double x, double y, double direction, int width, int height, String imageName, Board board) {
		super(x, y, direction, width, height, imageName, board);
		super.setAttack(8);
		mySpeed = 14;
	}

	public int getSpeed() {
		return mySpeed;
	}

	public String toString() {
		return "MachineGun";
	}

}