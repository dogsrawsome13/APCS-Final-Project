package info.finalproject.actor;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import info.finalproject.gui.Board;
import info.finalproject.weapon.MachineGun;
import info.finalproject.weapon.Pistol;
import info.finalproject.weapon.RPG;
import info.finalproject.weapon.Weapon;

public class Player extends Actor {
	private int myHealth;
	private int mySpeed;
	private int tmpLoad;
	private Weapon myWeapon;
	private ArrayList<Pistol> pistols;
	private ArrayList<MachineGun> machineGuns;
	private ArrayList<RPG> rpgs;

	private boolean moveForward, canForward, canBackward, moveBackward, left, right, fire, special;

	public Player(int x, int y, double degrees, int width, int height, String imageName, int speed, Weapon weapon,
			Board board) {
		super(x, y, degrees, width, height, imageName, board);
		myHealth = 1000;
		mySpeed = speed;
		pistols = new ArrayList<Pistol>();
		machineGuns = new ArrayList<MachineGun>();
		rpgs = new ArrayList<RPG>();
		tmpLoad = 0;
		myWeapon = weapon;
	}

	public int getHealth() {
		return myHealth;
	}

	public void loseHealth(int weaponType) {
		myHealth = myHealth - weaponType;
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

	public ArrayList getBullets() {
		if (myWeapon instanceof Pistol)
			return pistols;
		if (myWeapon instanceof MachineGun)
			return machineGuns;
		else
			return rpgs;
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
				if (myWeapon instanceof Pistol)
					pistols.add(new Pistol(Board.bullet.getX(), Board.bullet.getY(), Board.bullet.getDirection(),
							Board.bullet.getWidth(), Board.bullet.getHeight(), "images/missile.png", getBoard()));
				if (myWeapon instanceof MachineGun)
					machineGuns.add(new MachineGun(Board.bullet.getX(), Board.bullet.getY(),
							Board.bullet.getDirection(), Board.bullet.getWidth(), Board.bullet.getHeight(),
							"images/missile.png", getBoard()));
				else
					rpgs.add(new RPG(Board.bullet.getX(), Board.bullet.getY(), Board.bullet.getDirection(),
							Board.bullet.getWidth(), Board.bullet.getHeight(), "images/missile.png", getBoard()));
			}
			// reset the reload time
			tmpLoad = load;
		} else
			tmpLoad -= 1;
	}

	public String toString() {
		return super.toString() + ", " + myWeapon;
	}
}
