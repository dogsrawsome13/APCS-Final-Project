package info.finalproject.gui;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import info.finalproject.actor.Actor;
import info.finalproject.actor.Player;
import info.finalproject.actor.Powerup;
import info.finalproject.weapon.MachineGun;
import info.finalproject.weapon.Pistol;
import info.finalproject.weapon.RPG;
import info.finalproject.weapon.Weapon;

public class Board extends JPanel implements Runnable {
	public static Weapon bullet;

	private Thread loop; // the loop
	private Player player1, player2;
	private Powerup powerup;
	private Rock rock, rock1, rock2, rock3;
	private Wall wall, wall1, wall2, wall3;
	private ArrayList<Weapon> bullets, bullets2;
	private int tmpAngle, tmpAngle2, sx, sy, reload, numToShoot, spread;
	private boolean moveForward, canForward, canBackward, moveBackward, left, right, fire, special;
	private boolean moveForward2, canForward2, canBackward2, moveBackward2, left2, right2, fire2, special2;
	private boolean gameRunning;
	private boolean isExplosion1;

	public Board() {
		initBoard();
		addKeyListener(new Controll());
		setFocusable(true);
		setBackground(Color.WHITE);
		setDoubleBuffered(true);
		setFocusable(true);

	}

	private void initBoard() {
		player1 = new Player(400, 300, 0, 50, 50, "images/Shooter.png", 30,
				new Pistol(400, 300, 0, 50, 50, "images/missile.png", this), this);
		player2 = new Player(800, 700, 0, 50, 50, "images/Shooter.png", 30,
				new Pistol(400, 300, 0, 50, 50, "images/missile.png", this), this);
		powerup = new Powerup(500, 500, 0, 20, 20, "images/crate.png", this);
		rock = new Rock(600, 600, 100, 100, "images/Rock.png", this);
		rock1 = new Rock(1000, 400, 100, 100, "images/Rock.png", this);
		rock2 = new Rock(1550, 780, 100, 100, "images/Rock.png", this);
		rock3 = new Rock(710, 110, 100, 100, "images/Rock.png", this);
		wall = new Wall(700, 700, 0, 5, 1000, "images/Wall.png", this);
		tmpAngle = 0;
		tmpAngle2 = 0;
		special = fire = left = right = moveForward = moveBackward = false;
		special2 = fire2 = left2 = right2 = moveForward2 = moveBackward2 = false;
		canForward = canBackward = true;
		canForward2 = canBackward2 = true;
		sx = sy = 2;
		bullet = new Weapon(0, 0, 0, 0, 0, "images/missile.png", null);
		bullets = player1.getBullets();
		bullets2 = player2.getBullets();
		reload = 30;
		numToShoot = 1;
		spread = 0;
		gameRunning = true;
		isExplosion1 = false;

		loop = new Thread(this);
		loop.start();
	}
	

	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		AffineTransform old = g2d.getTransform();

		if (rock.isVisible())
			g2d.drawImage(rock.getImage(), (int) rock.getX(), (int) rock.getY(), rock.getWidth(), rock.getHeight(),
					this);
		if (rock1.isVisible())
			g2d.drawImage(rock1.getImage(), (int) rock1.getX(), (int) rock1.getY(), rock1.getWidth(), rock1.getHeight(),
					this);
		if (rock2.isVisible())
			g2d.drawImage(rock2.getImage(), (int) rock2.getX(), (int) rock2.getY(), rock2.getWidth(), rock2.getHeight(),
					this);
		if (rock3.isVisible())
			g2d.drawImage(rock3.getImage(), (int) rock3.getX(), (int) rock3.getY(), rock3.getWidth(), rock3.getHeight(),
					this);

		if (wall.isVisible()) {
			g2d.drawImage(wall.getImage(), (int) wall.getX(), (int) wall.getY(), wall.getWidth(), wall.getHeight(),
					this);
		}

		if (powerup.isVisible())
			g2d.drawImage(powerup.getImage(), (int) powerup.getX(), (int) powerup.getY(), powerup.getWidth(),
					powerup.getHeight(), this);

		g2d.drawString("" + player1.getHealth(), (int) player1.getX() + 10, (int) player1.getY() - 50);

		// rotating player1, rotation point is the middle of the square
		g2d.rotate(player1.getDirection(), player1.getX() + player1.getWidth() / 2,
				player1.getY() + player1.getHeight() / 2);

		// draw the image
		g2d.drawImage(player1.getImage(), (int) player1.getX(), (int) player1.getY(), player1.getWidth(),
				player1.getHeight(), this);
		g2d.setTransform(old);

		g2d.drawString("" + player2.getHealth(), (int) player2.getX() + 10, (int) player2.getY() - 50);

		// rotating player2, rotation point is the middle of the square
		g2d.rotate(player2.getDirection(), player2.getX() + player2.getWidth() / 2,
				player2.getY() + player2.getHeight() / 2);

		// draw the image
		g2d.drawImage(player2.getImage(), (int) player2.getX(), (int) player2.getY(), player2.getWidth(),
				player2.getHeight(), this);
		g2d.setTransform(old);

		// drawing player1 Pistols
		if (player1.getWeapon() instanceof Pistol) {
			ArrayList<Pistol> bullets = player1.getBullets();
			for (int i = 0; i < bullets.size(); i++) {
				Pistol tmpB = bullets.get(i);
				// playing with bullet colors
				if (i % 2 == 0)
					g2d.setColor(new Color(150, 130, 100));
				else
					g2d.setColor(new Color(60, 20, 120));
				g2d.fillRect((int) tmpB.getX(), (int) tmpB.getY(), tmpB.getWidth(), tmpB.getHeight());
			}
			// in case you have other things to rotate
			g2d.setTransform(old);
		}

		// drawing player1 MachineGuns
		if (player1.getWeapon() instanceof MachineGun) {
			ArrayList<MachineGun> bullets = player1.getBullets();
			for (int i = 0; i < bullets.size(); i++) {
				MachineGun tmpB = bullets.get(i);
				// playing with bullet colors
				if (i % 2 == 0)
					g2d.setColor(new Color(150, 130, 100));
				else
					g2d.setColor(new Color(60, 20, 120));
				g2d.fillRect((int) tmpB.getX(), (int) tmpB.getY(), tmpB.getWidth(), tmpB.getHeight());
			}
			// in case you have other things to rotate
			g2d.setTransform(old);
		}

		// drawing player1 RPGs
		if (player1.getWeapon() instanceof RPG) {
			ArrayList<RPG> bullets = player1.getBullets();
			for (int i = 0; i < bullets.size(); i++) {
				RPG tmpB = bullets.get(i);
				// playing with bullet colors
				if (i % 2 == 0)
					g2d.setColor(new Color(150, 130, 100));
				else
					g2d.setColor(new Color(60, 20, 120));
				g2d.fillRect((int) tmpB.getX(), (int) tmpB.getY(), tmpB.getWidth(), tmpB.getHeight());
					
			}
			// in case you have other things to rotate
			g2d.setTransform(old);
		}

		// drawing player2 Pistols
		if (player2.getWeapon() instanceof Pistol) {
			ArrayList<Pistol> bullets = player2.getBullets();
			for (int i = 0; i < bullets.size(); i++) {
				Pistol tmpB = bullets.get(i);
				// playing with bullet colors
				if (i % 2 == 0)
					g2d.setColor(new Color(150, 130, 100));
				else
					g2d.setColor(new Color(60, 20, 120));
				g2d.fillRect((int) tmpB.getX(), (int) tmpB.getY(), tmpB.getWidth(), tmpB.getHeight());
			}
			// in case you have other things to rotate
			g2d.setTransform(old);
		}

		// drawing player2 MachineGuns
		if (player2.getWeapon() instanceof MachineGun) {

			ArrayList<MachineGun> bullets = player2.getBullets();
			for (int i = 0; i < bullets.size(); i++) {
				MachineGun tmpB = bullets.get(i);
				// playing with bullet colors
				if (i % 2 == 0)
					g2d.setColor(new Color(150, 130, 100));
				else
					g2d.setColor(new Color(60, 20, 120));
				g2d.fillRect((int) tmpB.getX(), (int) tmpB.getY(), tmpB.getWidth(), tmpB.getHeight());
			}
			// in case you have other things to rotate
			g2d.setTransform(old);
		}

		// drawing player2 RPGs
		if (player2.getWeapon() instanceof RPG) {
			ArrayList<RPG> bullets = player2.getBullets();
			for (int i = 0; i < bullets.size(); i++) {
				RPG tmpB = bullets.get(i);
				// playing with bullet colors
				if (i % 2 == 0)
					g2d.setColor(new Color(150, 130, 100));
				else
					g2d.setColor(new Color(60, 20, 120));
				g2d.fillRect((int) tmpB.getX(), (int) tmpB.getY(), tmpB.getWidth(), tmpB.getHeight());
			}
			// in case you have other things to rotate
			g2d.setTransform(old);
		}

	}

	public boolean checkCollisions(Actor a1, Actor a2) {
		Rectangle r1 = a1.getBounds();
		Rectangle r2 = a2.getBounds();

		if (r1.intersects(r2)) {
			Actor powerupItem = null;
			if (!(a1 instanceof Player) && a1 instanceof Powerup)
				powerupItem = ((Powerup) a1).givePowerup();
			if (!(a2 instanceof Player) && a2 instanceof Powerup)
				powerupItem = ((Powerup) a2).givePowerup();
			if (powerupItem != null && powerupItem instanceof Weapon && a1.equals(player1))
				player1.setWeapon((Weapon) powerupItem);
			if (powerupItem != null && powerupItem instanceof Weapon && a1.equals(player2))
				player2.setWeapon((Weapon) powerupItem);
			if (a1 instanceof Player && a2 instanceof Player)
				return false;
			if ((a1 instanceof Powerup && a2 instanceof Weapon) || (a1 instanceof Weapon && a2 instanceof Powerup))
				return false;
			if (a1 instanceof Weapon && a2 instanceof Weapon)
				return false;
			return true;
		}
		return false;
	}

	public void play() {

		// if player1 get off the screen
		// we make it appear from the opposite side of the screen
		if (player1.getX() > 3000)
			player1.setX(0);
		else if (player1.getX() < -100)
			player1.setX(3000);

		if (player1.getY() > 2000)
			player1.setY(0);
		else if (player1.getY() < -100)
			player1.setY(2000);

		// if player2 get off the screen
		// we make it appear from the opposite side of the screen
		if (player2.getX() > 3000)
			player2.setX(0);
		else if (player2.getX() < -100)
			player2.setX(3000);

		if (player2.getY() > 2000)
			player2.setY(0);
		else if (player2.getY() < -100)
			player2.setY(2000);

		// moving player1 pistol
		if (player1.getWeapon() instanceof Pistol) {

			ArrayList<Pistol> tmpWs = player1.getBullets();
			for (int i = 0; i < tmpWs.size(); i++) {
				Pistol tmpW = tmpWs.get(i);
				tmpW.move(tmpW.getSpeed());
				if (checkCollisions(tmpW, rock) || checkCollisions(tmpW, wall))
					tmpWs.remove(i);
				else if (tmpW.isHit(player2)) {
					tmpW.hit(player2);
					tmpWs.remove(i);
				}
				if (tmpW.getX() > 3500 || tmpW.getX() < 0 || tmpW.getY() > 2000 || tmpW.getY() < 0)
					tmpWs.remove(i);
			}
		}

		// moving player1 MachineGun
		if (player1.getWeapon() instanceof MachineGun) {

			ArrayList<MachineGun> tmpWs = player1.getBullets();
			for (int i = 0; i < tmpWs.size(); i++) {
				MachineGun tmpW1 = tmpWs.get(i);
				tmpW1.move(tmpW1.getSpeed());
				if (checkCollisions(tmpW1, rock) || checkCollisions(tmpW1, wall))
					tmpWs.remove(i);
				else if (tmpW1.isHit(player2)) {
					tmpW1.hit(player2);
					tmpWs.remove(i);
				}
				if (tmpW1.getX() > 3500 || tmpW1.getX() < 0 || tmpW1.getY() > 2000 || tmpW1.getY() < 0)
					tmpWs.remove(i);
			}
		}

		// moving player1 RPG
		if (player1.getWeapon() instanceof RPG) {

			ArrayList<RPG> tmpWs = player1.getBullets();
			for (int i = 0; i < tmpWs.size(); i++) {
				RPG tmpW2 = tmpWs.get(i);
				tmpW2.move(tmpW2.getSpeed());
				if (tmpW2.isHit(player2) || tmpW2.isHit(wall) || tmpW2.isHit(rock)) {
					isExplosion1 = true;
					tmpW2.explode();
					tmpWs.remove(i);
				}
				if (tmpW2.getX() > 3500 || tmpW2.getX() < 0 || tmpW2.getY() > 2000 || tmpW2.getY() < 0)
					tmpWs.remove(i);
			}
		}

		// moving player2 pistol
		if (player2.getWeapon() instanceof Pistol) {

			ArrayList<Pistol> tmpWs2 = player2.getBullets();
			for (int i = 0; i < tmpWs2.size(); i++) {
				Pistol tmpW = tmpWs2.get(i);
				tmpW.move(tmpW.getSpeed());
				if (checkCollisions(tmpW, rock) || checkCollisions(tmpW, wall))
					tmpWs2.remove(i);

				else if (tmpW.isHit(player1)) {
					tmpW.hit(player1);
					tmpWs2.remove(i);
				}
				if (tmpW.getX() > 3500 || tmpW.getX() < 0 || tmpW.getY() > 2000 || tmpW.getY() < 0)
					tmpWs2.remove(i);
			}
		}

		// moving player2 machineGun
		if (player2.getWeapon() instanceof MachineGun) {
			ArrayList<MachineGun> tmpWs2 = player2.getBullets();
			for (int i = 0; i < tmpWs2.size(); i++) {
				MachineGun tmpW = tmpWs2.get(i);
				tmpW.move(tmpW.getSpeed());
				if (checkCollisions(tmpW, rock) || checkCollisions(tmpW, wall))
					tmpWs2.remove(i);
				else if (tmpW.isHit(player1)) {
					tmpW.hit(player1);
					tmpWs2.remove(i);
				}
				if (tmpW.getX() > 3500 || tmpW.getX() < 0 || tmpW.getY() > 2000 || tmpW.getY() < 0)
					tmpWs2.remove(i);

			}
		}

		// moving player2 RPG
		if (player2.getWeapon() instanceof RPG) {
			ArrayList<RPG> tmpWs2 = player2.getBullets();
			for (int i = 0; i < tmpWs2.size(); i++) {
				RPG tmpW = tmpWs2.get(i);
				tmpW.move(tmpW.getSpeed());
				if (tmpW.isHit(player1) || tmpW.isHit(wall) || tmpW.isHit(rock)) {
					tmpW.explode();
					tmpWs2.remove(i);
				}
				if (tmpW.getX() > 3500 || tmpW.getX() < 0 || tmpW.getY() > 2000 || tmpW.getY() < 0)
					tmpWs2.remove(i);
			}
		}

		// check if player1 is shooting
		if (fire)
			player1.fire(reload, numToShoot, spread);
		if (special)
			player1.fire(5, 3, 2);

		// changing player1 angle
		if (left)
			tmpAngle -= 1;
		if (right)
			tmpAngle += 1;

		// setting player1 angle
		player1.setDirection(tmpAngle);

		// this is just to keep the angle between 0 and 360
		if (tmpAngle > 360)
			tmpAngle = 0;
		else if (tmpAngle < 0)
			tmpAngle = 360;

		// moving player1
		if (moveForward)
			if (canForward)
				player1.moveForward(sx, sy);

		if (moveBackward)
			if (canBackward)
				player1.moveBackward(sx, sy);

		// check if player2 shooting
		if (fire2)
			player2.fire(reload, numToShoot, spread);
		if (special2)
			player2.fire(5, 3, 2);

		// changing the player2 angle
		if (left2)
			tmpAngle2 -= 1;
		if (right2)
			tmpAngle2 += 1;

		// setting player2 angle
		player2.setDirection(tmpAngle2);

		// this is just to keep the angle between 0 and 360
		if (tmpAngle2 > 360)
			tmpAngle2 = 0;
		else if (tmpAngle2 < 0)
			tmpAngle2 = 360;

		// moving player2
		if (moveForward2)
			if (canForward2)
				player2.moveForward(sx, sy);

		if (moveBackward2)
			if (canBackward2)
				player2.moveBackward(sx, sy);

		if (checkCollisions(player1, powerup)) {
			Actor powerupItem = powerup.givePowerup();
			if (powerupItem instanceof Weapon)
				player1.setWeapon((Weapon) powerupItem);
			powerup.removeSelf();
		}

		if (checkCollisions(player2, powerup)) {
			Actor powerupItem = powerup.givePowerup();
			if (powerupItem instanceof Weapon)
				player2.setWeapon((Weapon) powerupItem);
			powerup.removeSelf();
		}

		checkGameRunning(player1);
		checkGameRunning(player2);

	}

	public ArrayList<Actor> getActors() {

		ArrayList<Actor> actors = new ArrayList<Actor>();
		actors.add(player1);
		actors.add(player2);
		actors.add(wall);
		actors.add(rock);
		actors.add(powerup);
		for (Weapon bullet : bullets) {
			actors.add(bullet);
		}
		return actors;

	}

	public void checkGameRunning(Player player) {
		if (player.getHealth() <= 0)
			gameRunning = false;
	}

	// game key controll

	private class Controll extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == e.VK_SPACE)
				fire = true;
			if (e.getKeyCode() == e.VK_UP)
				moveForward = true;
			if (e.getKeyCode() == e.VK_DOWN)
				moveBackward = true;
			if (e.getKeyCode() == e.VK_LEFT)
				left = true;
			if (e.getKeyCode() == e.VK_RIGHT)
				right = true;

			if (e.getKeyCode() == e.VK_T)
				fire2 = true;
			if (e.getKeyCode() == e.VK_W)
				moveForward2 = true;
			if (e.getKeyCode() == e.VK_S)
				moveBackward2 = true;
			if (e.getKeyCode() == e.VK_A)
				left2 = true;
			if (e.getKeyCode() == e.VK_D)
				right2 = true;
		}

		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == e.VK_SPACE)
				fire = false;
			if (e.getKeyCode() == e.VK_UP)
				moveForward = false;
			if (e.getKeyCode() == e.VK_DOWN)
				moveBackward = false;
			if (e.getKeyCode() == e.VK_LEFT)
				left = false;
			if (e.getKeyCode() == e.VK_RIGHT)
				right = false;

			if (e.getKeyCode() == e.VK_T)
				fire2 = false;
			if (e.getKeyCode() == e.VK_W)
				moveForward2 = false;
			if (e.getKeyCode() == e.VK_S)
				moveBackward2 = false;
			if (e.getKeyCode() == e.VK_A)
				left2 = false;
			if (e.getKeyCode() == e.VK_D)
				right2 = false;
		}

	} // end private class

	@Override
	public void run() {

		while (gameRunning) {
			repaint();
			play();
			try {
				Thread.sleep(6);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
