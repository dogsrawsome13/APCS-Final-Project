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
	private Powerup powerup, powerup1;
	
	//2 powerups randomly spawn
	private Powerup randomPowUp1, randomPowUp2;
	private int respawntime;
	
	//check if random power ups exists
	private boolean randomPowUp1Exist, randomPowUp2Exist;
	
	//all rocks on the map 
	private ArrayList<Rock> allRocks;
	
	// rocks on the left border
	private ArrayList<Rock> leftRockBorder; 
	
	// rocks on the top border
	private ArrayList<Rock> topRockBorder;
	
	// rocks on the right border 
	private ArrayList<Rock> rightRockBorder;
	
	//rocks on the bottom border
	private ArrayList<Rock> bottomRockBorder;
	
	//rocks randomly placed in the center
	private ArrayList<Rock> randomRocks;
	
	private ArrayList<Weapon> bullets, bullets2;
	private int tmpAngle, tmpAngle2, sx1, sy1, sx2, sy2, reload, numToShoot, spread;
	private boolean moveForward, canForward, canBackward, moveBackward, left, right, fire, special;
	private boolean moveForward2, canForward2, canBackward2, moveBackward2, left2, right2, fire2, special2;
	private boolean gameRunning;
	private boolean isExplosion1;
	
	private MainMenu mainMenu;
	private Player1Win player1Win;
	private Player2Win player2Win;
	
	public static enum STATE {
		MENU,
		GAME,
		PLAYER1WIN,
		PLAYER2WIN
	};
	public static STATE State = STATE.MENU;

	public Board() {

		initBoard();
		addKeyListener(new Controll());
		setFocusable(true);
		setBackground(Color.WHITE);
		setDoubleBuffered(true);
		setFocusable(true);

	}

	private void initBoard() {
		player1 = new Player(400, 1000, 0, 60, 60, "images/Shooter.png", 30,
				new Pistol(400, 300, 0, 50, 50, "images/missile.png", this), this);
		player2 = new Player(2200, 600, 0, 60, 60, "images/Shooter.png", 30,
				new Pistol(400, 300, 0, 50, 50, "images/missile.png", this), this);
		powerup = new Powerup(1000, 1000, 0, 20, 20, "images/crate.png", this);
		powerup1 = new Powerup(1600, 1200, 0, 20, 20, "images/crate.png", this);
		
		randomPowUp1 = null;
		randomPowUp2 = null;
		
		randomPowUp1Exist = randomPowUp2Exist = false;
		
		respawntime = 5000;
		
		allRocks = new ArrayList<Rock>();
		leftRockBorder = new ArrayList<Rock>();
		topRockBorder = new ArrayList<Rock>();
		rightRockBorder = new ArrayList<Rock>();
		bottomRockBorder = new ArrayList<Rock>();
		randomRocks = new ArrayList<Rock>();
		
		//creating the left rock border 
		int yPos = 100;
		for (int i = 0; i < 5; i++) {
			Rock rock = new Rock(100, yPos, 100, 100, "images/Rock.png", this);
			yPos += 400; 
			leftRockBorder.add(rock);
			allRocks.add(rock);
		}
		
		
		//creating the top rock border 
		int xPos = 550;
		for (int i = 0; i < 5; i++) {
			Rock rock = new Rock(xPos, 100, 100, 100, "images/Rock.png", this);
			xPos += 550; 
			leftRockBorder.add(rock);
			allRocks.add(rock);
		}
		
		//creating the right rock border 
		yPos = 500;
		for (int i = 0; i < 5; i++) {
			Rock rock = new Rock(2750, yPos, 100, 100, "images/Rock.png", this);
			yPos += 400; 
			leftRockBorder.add(rock);
			allRocks.add(rock);
		}
		
		//creating the bottom rock border 
		xPos = 550;
		for (int i = 0; i < 5; i++) {
			Rock rock = new Rock(xPos, 1700, 100, 100, "images/Rock.png", this);
			xPos += 550; 
			leftRockBorder.add(rock);
			allRocks.add(rock);
		}
		
		//creating randomly placed rocks
		for (int i = 0; i < 5; i++) {
			Rock rock = new Rock(Math.random() * 2000 + 500, Math.random() * 1000 + 300, 100, 100, "images/Rock.png", this);
			for (Rock rock1: allRocks) {
				while (rock.getBounds().intersects(rock1.getBounds())
						|| rock.getBounds().intersects(player1.getBounds())
						|| rock.getBounds().intersects(player2.getBounds())) {
					rock.setX(Math.random() * 1000 + 500);
					rock.setY(Math.random() * 700 + 300);
				}
				
			}
			randomRocks.add(rock);
			allRocks.add(rock);

		}
		
		tmpAngle = 0;
		tmpAngle2 = 0;
		special = fire = left = right = moveForward = moveBackward = false;
		special2 = fire2 = left2 = right2 = moveForward2 = moveBackward2 = false;
		canForward = canBackward = true;
		canForward2 = canBackward2 = true;
		sx1 = sy1 = 2;
		sx2 = sy2 = 2;
		bullet = new Weapon(0, 0, 0, 0, 0, "images/missile.png", null);
		bullets = player1.getBullets();
		bullets2 = player2.getBullets();
		reload = 30;
		numToShoot = 1;
		spread = 0;
		gameRunning = true;
		isExplosion1 = false;
		
		mainMenu = new MainMenu();
		player1Win = new Player1Win();
		player2Win = new Player2Win();
		this.addMouseListener(new MouseInput());

		loop = new Thread(this);
		loop.start();
	}

	

	public boolean checkCollisions(Actor a1, Actor a2) {
		Rectangle r1 = a1.getBounds();
		Rectangle r2 = a2.getBounds();

		if (r1.intersects(r2)) {
			Actor powerupItem = null;
			if ((a1 instanceof Powerup) && a2 instanceof Player)
				powerupItem = ((Powerup) a1).givePowerup((Player)a2);
			if ((a2 instanceof Powerup) && a1 instanceof Player)
				powerupItem = ((Powerup) a2).givePowerup((Player)a1);
			
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
		
		if (State == STATE.GAME) {
			
			
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
					
					for (Rock rock: allRocks) {
						if (checkCollisions(tmpW, rock))
							tmpWs.remove(i);
						}
					
					if (tmpW.isHit(player2)) {
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
					MachineGun tmpW = tmpWs.get(i);
					tmpW.move(tmpW.getSpeed());
					
					for (Rock rock: allRocks) {
						if (checkCollisions(tmpW, rock))
							tmpWs.remove(i);
						
						}
					
					if (tmpW.isHit(player2)) {
						tmpW.hit(player2);
						tmpWs.remove(i);
						}

					if (tmpW.getX() > 3500 || tmpW.getX() < 0 || tmpW.getY() > 2000 || tmpW.getY() < 0)
						tmpWs.remove(i);
				}

			}

			// moving player1 RPG
			if (player1.getWeapon() instanceof RPG) {

				ArrayList<RPG> tmpWs = player1.getBullets();
				for (int i = 0; i < tmpWs.size(); i++) {
					RPG tmpW = tmpWs.get(i);
					tmpW.move(tmpW.getSpeed());
					
					for (Rock rock: allRocks) {
						if (checkCollisions(tmpW, rock)) {
							isExplosion1 = true;
							tmpW.explode();
							tmpWs.remove(i);
						}
					}
					
					if (tmpW.isHit(player2)) {
						isExplosion1 = true;
						tmpW.explode();
						tmpWs.remove(i);
					}
					if (tmpW.getX() > 3500 || tmpW.getX() < 0 || tmpW.getY() > 2000 || tmpW.getY() < 0)
						tmpWs.remove(i);
				}
			}

			// moving player2 pistol
			if (player2.getWeapon() instanceof Pistol) {

				ArrayList<Pistol> tmpWs2 = player2.getBullets();
				for (int i = 0; i < tmpWs2.size(); i++) {
					Pistol tmpW = tmpWs2.get(i);
					tmpW.move(tmpW.getSpeed());
					
					for (Rock rock: allRocks) {
						if (checkCollisions(tmpW, rock)) 
							tmpWs2.remove(i);
						
						}
					
					if (tmpW.isHit(player1)) {
						tmpW.hit(player2);
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
					
					for (Rock rock: allRocks) {
						if (checkCollisions(tmpW, rock)) 
							tmpWs2.remove(i);
					}
					
					if (tmpW.isHit(player1)) {
						tmpW.hit(player2);
						tmpWs2.remove(i);
					}

					if (tmpW.getX() > 3500 || tmpW.getX() < 0 || tmpW.getY() > 2000 || tmpW.getY() < 0)
						tmpWs2.remove(i);
				}
			}


			// moving player2 RPG
			if (player2.getWeapon() instanceof RPG) {

				ArrayList<RPG> tmpWs = player2.getBullets();
				for (int i = 0; i < tmpWs.size(); i++) {
					RPG tmpW = tmpWs.get(i);
					tmpW.move(tmpW.getSpeed());
					
					for (Rock rock: allRocks) {
						if (checkCollisions(tmpW, rock)) {
							isExplosion1 = true;
							tmpW.explode();
							tmpWs.remove(i);
						}
						
					}
					if (tmpW.isHit(player1)) {
						isExplosion1 = true;
						tmpW.explode();
						tmpWs.remove(i);
					}
					if (tmpW.getX() > 3500 || tmpW.getX() < 0 || tmpW.getY() > 2000 || tmpW.getY() < 0)
						tmpWs.remove(i);
				}
			}
			
			spawnPowerUp();

			// check if player1 is shooting
			if (fire)
				if (player1.getWeapon() instanceof Pistol)
					player1.fire(150, 1, 1);
				else if (player1.getWeapon() instanceof MachineGun)
					player1.fire(10, 1, 1);
				else
					player1.fire(100, 1, 1);

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
					player1.moveForward(sx1, sy1);

			if (moveBackward)
				if (canBackward)
					player1.moveBackward(sx1, sy1);

			// check if player2 shooting
			if (fire2)
				if (player2.getWeapon() instanceof Pistol)
					player2.fire(150, 1, 1);
				else if (player2.getWeapon() instanceof MachineGun)
					player2.fire(10, 1, 1);
				else
					player2.fire(100, 1, 1);

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
					player2.moveForward(sx2, sy2);

			if (moveBackward2)
				if (canBackward2)
					player2.moveBackward(sx2, sy2);

			if (checkCollisions(player1, powerup)) {
				Actor powerupItem = powerup.givePowerup(player1);
				if (powerupItem instanceof Weapon)
					player1.setWeapon((Weapon) powerupItem);
				powerup.removeSelf();
			}
			
			if (checkCollisions(player1, powerup1)) {
				Actor powerupItem = powerup1.givePowerup(player1);
				if (powerupItem instanceof Weapon)
					player1.setWeapon((Weapon) powerupItem);
				powerup1.removeSelf();
			}
			
			if (randomPowUp1Exist)
				if (checkCollisions(player1, randomPowUp1)) {
					Actor powerupItem = randomPowUp1.givePowerup(player1);
					if (powerupItem instanceof Weapon) {
						player1.setWeapon((Weapon) powerupItem);
						randomPowUp1.removeSelf();
						randomPowUp1Exist = false;
					}

			}
			
			if (randomPowUp2Exist)
				if (checkCollisions(player1, randomPowUp2)) {
					Actor powerupItem = randomPowUp2.givePowerup(player1);
					if (powerupItem instanceof Weapon) {
						player1.setWeapon((Weapon) powerupItem);
						randomPowUp2.removeSelf();
						randomPowUp2Exist = false;
					}

			}


			if (checkCollisions(player2, powerup)) {
				Actor powerupItem = powerup.givePowerup(player2);
				if (powerupItem instanceof Weapon)
					player2.setWeapon((Weapon) powerupItem);
				powerup.removeSelf();
			}
			
			if (checkCollisions(player2, powerup1)) {
				Actor powerupItem = powerup1.givePowerup(player2);
				if (powerupItem instanceof Weapon)
					player2.setWeapon((Weapon) powerupItem);
				powerup1.removeSelf();
			}
			
			if (randomPowUp1Exist)
				if (checkCollisions(player2, randomPowUp1)) {
					Actor powerupItem = randomPowUp1.givePowerup(player2);
					if (powerupItem instanceof Weapon)
						player2.setWeapon((Weapon) powerupItem);
					randomPowUp1.removeSelf();
					randomPowUp1Exist = false;
			}
			
			if (randomPowUp2Exist)
				if (checkCollisions(player2, randomPowUp2)) {
					Actor powerupItem = randomPowUp2.givePowerup(player2);
					if (powerupItem instanceof Weapon)
						player2.setWeapon((Weapon) powerupItem);
					randomPowUp2.removeSelf();
					randomPowUp2Exist = false;

			}
			
			for (Rock rock: allRocks) {
				if (checkCollisions(player1, rock)) {
					Rectangle overlap = player1.getBounds().intersection(rock.getBounds());
					if (overlap.width < overlap.height && sy1 != 0)
						sx1 = 0;
					else if (overlap.width > overlap.height && sx1 != 0)
						sy1 = 0;
					else
						sx1 = sy1 = 2;
				
				}
			}
			for (Rock rock: allRocks) {

				if (checkCollisions(player2, rock)) {
					Rectangle overlap = player2.getBounds().intersection(rock.getBounds());
					if (overlap.width < overlap.height && sy2 != 0)
						sx2 = 0;
					else if (overlap.width > overlap.height && sx2 != 0)
						sy2 = 0;
					else
						sx2 = sy2 = 2;
				
				}
			}
			
			if (isPlayerDead(player1)) {
				State = State.PLAYER2WIN;
			}
			
			if (isPlayerDead(player2)) {
				State = State.PLAYER1WIN;
			}
			
		}
		
	}
	public void paint(Graphics g) {
		
		if (State == STATE.GAME) {
			super.paint(g);

			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			AffineTransform old = g2d.getTransform();
			
			for (Rock rock: allRocks) {
				if (rock.isVisible())
					g2d.drawImage(rock.getImage(), (int) rock.getX(), (int) rock.getY(), rock.getWidth(), rock.getHeight(),
							this);
			}


			// drawing powerups 
			
			if (powerup.isVisible())
				g2d.drawImage(powerup.getImage(), (int) powerup.getX(), (int) powerup.getY(), powerup.getWidth(),
						powerup.getHeight(), this);
			if (powerup1.isVisible())
				g2d.drawImage(powerup1.getImage(), (int) powerup1.getX(), (int) powerup1.getY(), powerup1.getWidth(),
						powerup1.getHeight(), this);
			
			if (randomPowUp1Exist)
				if (randomPowUp1.isVisible())
					g2d.drawImage(randomPowUp1.getImage(), (int) randomPowUp1.getX(), (int) randomPowUp1.getY(), randomPowUp1.getWidth(),
						randomPowUp1.getHeight(), this);
			
			if (randomPowUp2Exist)
				if (randomPowUp2.isVisible())
					g2d.drawImage(randomPowUp2.getImage(), (int) randomPowUp2.getX(), (int) randomPowUp2.getY(), randomPowUp2.getWidth(),
						randomPowUp2.getHeight(), this);
			

			// draw player1 health on the screen
			Font p1Fnt = new Font ("arial", Font.BOLD, 20);
			g2d.setFont(p1Fnt);
			g2d.setColor(Color.red);
			g2d.drawString("" + player1.getHealth(), (int) player1.getX() + 10, (int) player1.getY() - 5);
			
			// rotating player1, rotation point is the middle of the square
			g2d.rotate(player1.getDirection(), player1.getX() + player1.getWidth() / 2,
					player1.getY() + player1.getHeight() / 2);

			// draw the image
			g2d.drawImage(player1.getImage(), (int) player1.getX(), (int) player1.getY(), player1.getWidth(),
					player1.getHeight(), this);
			g2d.setTransform(old);

			//draw player2 health on the screen
			g2d.drawString("" + player2.getHealth(), (int) player2.getX() + 10, (int) player2.getY() - 5);

			// rotating player2, rotation point is the middle of the square
			g2d.rotate(player2.getDirection(), player2.getX() + player2.getWidth() / 2,
					player2.getY() + player2.getHeight() / 2);

			// draw the image
			g2d.drawImage(player2.getImage(), (int) player2.getX(), (int) player2.getY(), player2.getWidth(),
					player2.getHeight(), this);
			g2d.setTransform(old);
			
			//draw player1 weapon type
			g2d.setColor(Color.black);
			g2d.drawString(player1.getWeaponType(), (int) player1.getX() + 10, (int) player1.getY() + 80);

			//draw player2 weapon type
			g2d.drawString(player2.getWeaponType(), (int) player2.getX() + 10, (int) player2.getY() + 80);

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

					if (isExplosion1) {
						Thread explosionThread = new Thread() {
							public void run() {
								try {
									g2d.fillRect((int) tmpB.getX(), (int) tmpB.getY(), 50, 50);
									Thread.sleep(3000);
									
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						};
						explosionThread.start();

						isExplosion1 = false;
					}
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
		else if (State == STATE.MENU) {
			mainMenu.render(g);
			
		}
		
		else if (State == STATE.PLAYER1WIN) {
			player1Win.render(g);
		}
		
		else if (State == STATE.PLAYER2WIN) {
			player2Win.render(g);
		}
		

	}

	public ArrayList<Actor> getActors() {

		ArrayList<Actor> actors = new ArrayList<Actor>();
		actors.add(player1);
		actors.add(player2);
		actors.add(powerup);
		for (Weapon bullet : bullets) {
			actors.add(bullet);
		}
		return actors;

	}

	public boolean isPlayerDead(Player player) {
		if (player.getHealth() <= 0)
			return true;
		else
			return false;
	}
	
	public void spawnPowerUp() { 
		
		if (respawntime == 0 && randomPowUp1Exist == false && randomPowUp2Exist == false) {
			
			randomPowUp1 = new Powerup(Math.random() * 2000 + 100, Math.random() * 1000 + 100, 0, 20, 20, "images/crate.png", this);
			randomPowUp1Exist = true;
			randomPowUp2 = new Powerup(Math.random() * 2000 + 100, Math.random() * 1000 + 100, 0, 20, 20, "images/crate.png", this);
			randomPowUp2Exist = true;
			
			System.out.println(randomPowUp1.getX());

			respawntime = 5000;
		}
		else {
			respawntime -= 1;
			if (respawntime < 0)
				respawntime = 0;
		}
	}

	// game key controll

	private class Controll extends KeyAdapter {
		
		public void keyPressed(KeyEvent e) {
			
			if (State == STATE.GAME) {
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
		}


		public void keyReleased(KeyEvent e) {
			
			if (State == State.GAME) {
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

		}
			

	} // end private class

	@Override
	public void run() {

		while (gameRunning) {
			repaint();
			play();
			repaint();
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
