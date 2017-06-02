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
	
	// rocks on the left border
	private Rock rock, rock1, rock2, rock3, rock4;
	
	// rocks on the top border
	private Rock rock5, rock6, rock7, rock8, rock9;
	
	// rocks on the right border 
	private Rock rock10, rock11, rock12, rock13;
	
	//rocks on the bottom border
	private Rock rock14, rock15, rock16, rock17;
	
	//rocks randomly placed in the center
	private Rock rock18, rock19, rock20;
	
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
		
		respawntime = 3000;
		
		
		rock = new Rock(100, 100, 100, 100, "images/Rock.png", this);
		rock1 = new Rock(100, 500, 100, 100, "images/Rock.png", this);
		rock2 = new Rock(100, 900, 100, 100, "images/Rock.png", this);
		rock3 = new Rock(100, 1300, 100, 100, "images/Rock.png", this);
		rock4 = new Rock(100, 1700, 100, 100, "images/Rock.png", this);
		
		rock5 = new Rock(500, 100, 100, 100, "images/Rock.png", this);
		rock6 = new Rock(1050, 100, 100, 100, "images/Rock.png", this);
		rock7 = new Rock(1600, 100, 100, 100, "images/Rock.png", this);
		rock8 = new Rock(2150, 100, 100, 100, "images/Rock.png", this);
		rock9 = new Rock(2700, 100, 100, 100, "images/Rock.png", this);
		
		rock10 = new Rock(2700, 500, 100, 100, "images/Rock.png", this);
		rock11 = new Rock(2700, 900, 100, 100, "images/Rock.png", this);
		rock12 = new Rock(2700, 1300, 100, 100, "images/Rock.png", this);
		rock13 = new Rock(2700, 1700, 100, 100, "images/Rock.png", this);
		
		rock14 = new Rock(500, 1700, 100, 100, "images/Rock.png", this);
		rock15 = new Rock(1050, 1700, 100, 100, "images/Rock.png", this);
		rock16 = new Rock(1600, 1700, 100, 100, "images/Rock.png", this);
		rock17 = new Rock(2150, 1700, 100, 100, "images/Rock.png", this);
		
		rock18 = new Rock(700, 1000, 100, 100, "images/Rock.png", this);
		rock19 = new Rock(1200, 700, 100, 100, "images/Rock.png", this);
		rock20 = new Rock(2000, 1000, 100, 100, "images/Rock.png", this);

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

	public void paint(Graphics g) {
		
		if (State == STATE.GAME) {
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
			if (rock4.isVisible())
				g2d.drawImage(rock4.getImage(), (int) rock4.getX(), (int) rock4.getY(), rock4.getWidth(), rock4.getHeight(),
						this);
			if (rock5.isVisible())
				g2d.drawImage(rock5.getImage(), (int) rock5.getX(), (int) rock5.getY(), rock5.getWidth(), rock5.getHeight(),
						this);
			if (rock6.isVisible())
				g2d.drawImage(rock6.getImage(), (int) rock6.getX(), (int) rock6.getY(), rock6.getWidth(), rock6.getHeight(),
						this);
			if (rock7.isVisible())
				g2d.drawImage(rock7.getImage(), (int) rock7.getX(), (int) rock7.getY(), rock7.getWidth(), rock7.getHeight(),
						this);
			if (rock8.isVisible())
				g2d.drawImage(rock8.getImage(), (int) rock8.getX(), (int) rock8.getY(), rock8.getWidth(), rock8.getHeight(),
						this);
			if (rock9.isVisible())
				g2d.drawImage(rock9.getImage(), (int) rock9.getX(), (int) rock9.getY(), rock9.getWidth(), rock9.getHeight(),
						this);
			
			if (rock10.isVisible())
				g2d.drawImage(rock10.getImage(), (int) rock10.getX(), (int) rock10.getY(), rock10.getWidth(), rock10.getHeight(),
						this);
			
			if (rock11.isVisible())
				g2d.drawImage(rock11.getImage(), (int) rock11.getX(), (int) rock11.getY(), rock11.getWidth(), rock11.getHeight(),
						this);
			
			if (rock12.isVisible())
				g2d.drawImage(rock12.getImage(), (int) rock12.getX(), (int) rock12.getY(), rock12.getWidth(), rock12.getHeight(),
						this);
			
			if (rock13.isVisible())
				g2d.drawImage(rock13.getImage(), (int) rock13.getX(), (int) rock13.getY(), rock13.getWidth(), rock13.getHeight(),
						this);
			
			if (rock14.isVisible())
				g2d.drawImage(rock14.getImage(), (int) rock14.getX(), (int) rock14.getY(), rock14.getWidth(), rock14.getHeight(),
						this);
			
			if (rock15.isVisible())
				g2d.drawImage(rock15.getImage(), (int) rock15.getX(), (int) rock15.getY(), rock15.getWidth(), rock15.getHeight(),
						this);
			
			if (rock16.isVisible())
				g2d.drawImage(rock16.getImage(), (int) rock16.getX(), (int) rock16.getY(), rock16.getWidth(), rock16.getHeight(),
						this);
			
			if (rock17.isVisible())
				g2d.drawImage(rock17.getImage(), (int) rock17.getX(), (int) rock17.getY(), rock17.getWidth(), rock17.getHeight(),
						this);
			if (rock18.isVisible())
				g2d.drawImage(rock18.getImage(), (int) rock18.getX(), (int) rock18.getY(), rock18.getWidth(), rock18.getHeight(),
						this);
			if (rock19.isVisible())
				g2d.drawImage(rock19.getImage(), (int) rock19.getX(), (int) rock19.getY(), rock19.getWidth(), rock19.getHeight(),
						this);
			if (rock20.isVisible())
				g2d.drawImage(rock20.getImage(), (int) rock20.getX(), (int) rock20.getY(), rock20.getWidth(), rock20.getHeight(),
						this);

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
					if (checkCollisions(tmpW, rock) 
							|| checkCollisions(tmpW, rock1)
							|| checkCollisions(tmpW, rock2)
							|| checkCollisions(tmpW, rock3)
							|| checkCollisions(tmpW, rock4)
							|| checkCollisions(tmpW, rock5)
							|| checkCollisions(tmpW, rock6)
							|| checkCollisions(tmpW, rock7)
							|| checkCollisions(tmpW, rock8)
							|| checkCollisions(tmpW, rock9)
							|| checkCollisions(tmpW, rock10)
							|| checkCollisions(tmpW, rock11)
							|| checkCollisions(tmpW, rock12)
							|| checkCollisions(tmpW, rock13)
							|| checkCollisions(tmpW, rock14)
							|| checkCollisions(tmpW, rock15)
							|| checkCollisions(tmpW, rock16)
							|| checkCollisions(tmpW, rock17)
							|| checkCollisions(tmpW, rock18)
							|| checkCollisions(tmpW, rock19)
							|| checkCollisions(tmpW, rock20))
						
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
					if (checkCollisions(tmpW1, rock)
							|| checkCollisions(tmpW1, rock1)
							|| checkCollisions(tmpW1, rock2)
							|| checkCollisions(tmpW1, rock3)
							|| checkCollisions(tmpW1, rock4)
							|| checkCollisions(tmpW1, rock5)
							|| checkCollisions(tmpW1, rock6)
							|| checkCollisions(tmpW1, rock7)
							|| checkCollisions(tmpW1, rock8)
							|| checkCollisions(tmpW1, rock9)
							|| checkCollisions(tmpW1, rock10)
							|| checkCollisions(tmpW1, rock11)
							|| checkCollisions(tmpW1, rock12)
							|| checkCollisions(tmpW1, rock13)
							|| checkCollisions(tmpW1, rock14)
							|| checkCollisions(tmpW1, rock15)
							|| checkCollisions(tmpW1, rock16)
							|| checkCollisions(tmpW1, rock17)
							|| checkCollisions(tmpW1, rock18)
							|| checkCollisions(tmpW1, rock19)
							|| checkCollisions(tmpW1, rock20))
						
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
					if (tmpW2.isHit(player2)
							|| tmpW2.isHit(rock)
							|| tmpW2.isHit(rock1)
							|| tmpW2.isHit(rock2)
							|| tmpW2.isHit(rock3)
							|| tmpW2.isHit(rock4)
							|| tmpW2.isHit(rock5)
							|| tmpW2.isHit(rock6)
							|| tmpW2.isHit(rock7)
							|| tmpW2.isHit(rock8)
							|| tmpW2.isHit(rock9)
							|| tmpW2.isHit(rock10)
							|| tmpW2.isHit(rock11)
							|| tmpW2.isHit(rock12)
							|| tmpW2.isHit(rock13)
							|| tmpW2.isHit(rock14)
							|| tmpW2.isHit(rock15)
							|| tmpW2.isHit(rock16)
							|| tmpW2.isHit(rock17)
							|| tmpW2.isHit(rock18)
							|| tmpW2.isHit(rock19)
							|| tmpW2.isHit(rock20)) {
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
					if (checkCollisions(tmpW, rock) 
							|| checkCollisions(tmpW, rock1)
							|| checkCollisions(tmpW, rock2)
							|| checkCollisions(tmpW, rock3)
							|| checkCollisions(tmpW, rock4)
							|| checkCollisions(tmpW, rock5)
							|| checkCollisions(tmpW, rock6)
							|| checkCollisions(tmpW, rock7)
							|| checkCollisions(tmpW, rock8)
							|| checkCollisions(tmpW, rock9)
							|| checkCollisions(tmpW, rock10)
							|| checkCollisions(tmpW, rock11)
							|| checkCollisions(tmpW, rock12)
							|| checkCollisions(tmpW, rock13)
							|| checkCollisions(tmpW, rock14)
							|| checkCollisions(tmpW, rock15)
							|| checkCollisions(tmpW, rock16)
							|| checkCollisions(tmpW, rock17)
							|| checkCollisions(tmpW, rock18)
							|| checkCollisions(tmpW, rock19)
							|| checkCollisions(tmpW, rock20))

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
					if (checkCollisions(tmpW, rock) 
							|| checkCollisions(tmpW, rock1)
							|| checkCollisions(tmpW, rock2)
							|| checkCollisions(tmpW, rock3)
							|| checkCollisions(tmpW, rock4)
							|| checkCollisions(tmpW, rock5)
							|| checkCollisions(tmpW, rock6)
							|| checkCollisions(tmpW, rock7)
							|| checkCollisions(tmpW, rock8)
							|| checkCollisions(tmpW, rock9)
							|| checkCollisions(tmpW, rock10)
							|| checkCollisions(tmpW, rock11)
							|| checkCollisions(tmpW, rock12)
							|| checkCollisions(tmpW, rock13)
							|| checkCollisions(tmpW, rock14)
							|| checkCollisions(tmpW, rock15)
							|| checkCollisions(tmpW, rock16)
							|| checkCollisions(tmpW, rock17)
							|| checkCollisions(tmpW, rock18)
							|| checkCollisions(tmpW, rock19)
							|| checkCollisions(tmpW, rock20))
						
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
					if (tmpW.isHit(player1)
							|| tmpW.isHit(rock)
							|| tmpW.isHit(rock1)
							|| tmpW.isHit(rock2)
							|| tmpW.isHit(rock3)
							|| tmpW.isHit(rock4)
							|| tmpW.isHit(rock5)
							|| tmpW.isHit(rock6)
							|| tmpW.isHit(rock7)
							|| tmpW.isHit(rock8)
							|| tmpW.isHit(rock9)
							|| tmpW.isHit(rock10)
							|| tmpW.isHit(rock11)
							|| tmpW.isHit(rock12)
							|| tmpW.isHit(rock13)
							|| tmpW.isHit(rock14)
							|| tmpW.isHit(rock15)
							|| tmpW.isHit(rock16)
							|| tmpW.isHit(rock17)
							|| tmpW.isHit(rock18)
							|| tmpW.isHit(rock19)
							|| tmpW.isHit(rock20)) {
						tmpW.explode();
						tmpWs2.remove(i);
					}
					if (tmpW.getX() > 3500 || tmpW.getX() < 0 || tmpW.getY() > 2000 || tmpW.getY() < 0)
						tmpWs2.remove(i);
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
				Actor powerupItem = powerup.givePowerup();
				if (powerupItem instanceof Weapon)
					player1.setWeapon((Weapon) powerupItem);
				powerup.removeSelf();
			}
			
			if (checkCollisions(player1, powerup1)) {
				Actor powerupItem = powerup1.givePowerup();
				if (powerupItem instanceof Weapon)
					player1.setWeapon((Weapon) powerupItem);
				powerup1.removeSelf();
			}
			
			if (randomPowUp1Exist)
				if (checkCollisions(player1, randomPowUp1)) {
					Actor powerupItem = randomPowUp1.givePowerup();
					if (powerupItem instanceof Weapon) {
						player1.setWeapon((Weapon) powerupItem);
						randomPowUp1.removeSelf();
						randomPowUp1Exist = false;
					}

			}
			
			if (randomPowUp2Exist)
				if (checkCollisions(player1, randomPowUp2)) {
					Actor powerupItem = randomPowUp2.givePowerup();
					if (powerupItem instanceof Weapon) {
						player1.setWeapon((Weapon) powerupItem);
						randomPowUp2.removeSelf();
						randomPowUp2Exist = false;
					}

			}


			if (checkCollisions(player2, powerup)) {
				Actor powerupItem = powerup.givePowerup();
				if (powerupItem instanceof Weapon)
					player2.setWeapon((Weapon) powerupItem);
				powerup.removeSelf();
			}
			
			if (checkCollisions(player2, powerup1)) {
				Actor powerupItem = powerup1.givePowerup();
				if (powerupItem instanceof Weapon)
					player2.setWeapon((Weapon) powerupItem);
				powerup1.removeSelf();
			}
			
			if (randomPowUp1Exist)
				if (checkCollisions(player2, randomPowUp1)) {
					Actor powerupItem = randomPowUp1.givePowerup();
					if (powerupItem instanceof Weapon)
						player2.setWeapon((Weapon) powerupItem);
					randomPowUp1.removeSelf();
					randomPowUp1Exist = false;
			}
			
			if (randomPowUp2Exist)
				if (checkCollisions(player2, randomPowUp2)) {
					Actor powerupItem = randomPowUp2.givePowerup();
					if (powerupItem instanceof Weapon)
						player2.setWeapon((Weapon) powerupItem);
					randomPowUp2.removeSelf();
					randomPowUp2Exist = false;

			}
			
			if (checkCollisions(player1, rock)) {
				Rectangle overlap = player1.getBounds().intersection(rock.getBounds());
				if (overlap.width < overlap.height && sy1 != 0)
					sx1 = 0;
				else if (overlap.width > overlap.height && sx1 != 0)
					sy1 = 0;
				else
					sx1 = sy1 = 2;
			
			}
			
			if (checkCollisions(player1, rock1)) {
				Rectangle overlap = player1.getBounds().intersection(rock1.getBounds());
				if (overlap.width < overlap.height && sy1 != 0)
					sx1 = 0;
				else if (overlap.width > overlap.height && sx1 != 0)
					sy1 = 0;
				else
					sx1 = sy1 = 2;
			}
			
			if (checkCollisions(player1, rock2)) {
				Rectangle overlap = player1.getBounds().intersection(rock2.getBounds());
				if (overlap.width < overlap.height && sy1 != 0)
					sx1 = 0;
				else if (overlap.width > overlap.height && sx1 != 0)
					sy1 = 0;
				else
					sx1 = sy1 = 2;
			
			}
			
			if (checkCollisions(player1, rock3)) {
				Rectangle overlap = player1.getBounds().intersection(rock3.getBounds());
				if (overlap.width < overlap.height && sy1 != 0)
					sx1 = 0;
				else if (overlap.width > overlap.height && sx1 != 0)
					sy1 = 0;
				else
					sx1 = sy1 = 2;
			
			}
			
			if (checkCollisions(player1, rock4)) {
				Rectangle overlap = player1.getBounds().intersection(rock4.getBounds());
				if (overlap.width < overlap.height && sy1 != 0)
					sx1 = 0;
				else if (overlap.width > overlap.height && sx1 != 0)
					sy1 = 0;
				else
					sx1 = sy1 = 2;
			
			}
			
			if (checkCollisions(player1, rock5)) {
				Rectangle overlap = player1.getBounds().intersection(rock5.getBounds());
				if (overlap.width < overlap.height && sy1 != 0)
					sx1 = 0;
				else if (overlap.width > overlap.height && sx1 != 0)
					sy1 = 0;
				else
					sx1 = sy1 = 2;
			
			}
			
			if (checkCollisions(player1, rock6)) {
				Rectangle overlap = player1.getBounds().intersection(rock6.getBounds());
				if (overlap.width < overlap.height && sy1 != 0)
					sx1 = 0;
				else if (overlap.width > overlap.height && sx1 != 0)
					sy1 = 0;
				else
					sx1 = sy1 = 2;
			
			}
			if (checkCollisions(player1, rock7)) {
				Rectangle overlap = player1.getBounds().intersection(rock7.getBounds());
				if (overlap.width < overlap.height && sy1 != 0)
					sx1 = 0;
				else if (overlap.width > overlap.height && sx1 != 0)
					sy1 = 0;
				else
					sx1 = sy1 = 2;
			
			}
			if (checkCollisions(player1, rock8)) {
				Rectangle overlap = player1.getBounds().intersection(rock8.getBounds());
				if (overlap.width < overlap.height && sy1 != 0)
					sx1 = 0;
				else if (overlap.width > overlap.height && sx1 != 0)
					sy1 = 0;
				else
					sx1 = sy1 = 2;
			
			}
			
			if (checkCollisions(player1, rock9)) {
				Rectangle overlap = player1.getBounds().intersection(rock9.getBounds());
				if (overlap.width < overlap.height && sy1 != 0)
					sx1 = 0;
				else if (overlap.width > overlap.height && sx1 != 0)
					sy1 = 0;
				else
					sx1 = sy1 = 2;
			
			}
			
			if (checkCollisions(player1, rock10)) {
				Rectangle overlap = player1.getBounds().intersection(rock10.getBounds());
				if (overlap.width < overlap.height && sy1 != 0)
					sx1 = 0;
				else if (overlap.width > overlap.height && sx1 != 0)
					sy1 = 0;
				else
					sx1 = sy1 = 2;
			
			}
			
			if (checkCollisions(player1, rock11)) {
				Rectangle overlap = player1.getBounds().intersection(rock11.getBounds());
				if (overlap.width < overlap.height && sy1 != 0)
					sx1 = 0;
				else if (overlap.width > overlap.height && sx1 != 0)
					sy1 = 0;
				else
					sx1 = sy1 = 2;
			
			}
			
			if (checkCollisions(player1, rock12)) {
				Rectangle overlap = player1.getBounds().intersection(rock12.getBounds());
				if (overlap.width < overlap.height && sy1 != 0)
					sx1 = 0;
				else if (overlap.width > overlap.height && sx1 != 0)
					sy1 = 0;
				else
					sx1 = sy1 = 2;
			
			}
			
			if (checkCollisions(player1, rock13)) {
				Rectangle overlap = player1.getBounds().intersection(rock13.getBounds());
				if (overlap.width < overlap.height && sy1 != 0)
					sx1 = 0;
				else if (overlap.width > overlap.height && sx1 != 0)
					sy1 = 0;
				else
					sx1 = sy1 = 2;
			
			}
			
			if (checkCollisions(player1, rock14)) {
				Rectangle overlap = player1.getBounds().intersection(rock14.getBounds());
				if (overlap.width < overlap.height && sy1 != 0)
					sx1 = 0;
				else if (overlap.width > overlap.height && sx1 != 0)
					sy1 = 0;
				else
					sx1 = sy1 = 2;
			
			}
			
			if (checkCollisions(player1, rock15)) {
				Rectangle overlap = player1.getBounds().intersection(rock15.getBounds());
				if (overlap.width < overlap.height && sy1 != 0)
					sx1 = 0;
				else if (overlap.width > overlap.height && sx1 != 0)
					sy1 = 0;
				else
					sx1 = sy1 = 2;
			
			}
			
			if (checkCollisions(player1, rock16)) {
				Rectangle overlap = player1.getBounds().intersection(rock16.getBounds());
				if (overlap.width < overlap.height && sy1 != 0)
					sx1 = 0;
				else if (overlap.width > overlap.height && sx1 != 0)
					sy1 = 0;
				else
					sx1 = sy1 = 2;
			
			}
			
			if (checkCollisions(player1, rock17)) {
				Rectangle overlap = player1.getBounds().intersection(rock17.getBounds());
				if (overlap.width < overlap.height && sy1 != 0)
					sx1 = 0;
				else if (overlap.width > overlap.height && sx1 != 0)
					sy1 = 0;
				else
					sx1 = sy1 = 2;
			
			}
			
			if (checkCollisions(player1, rock18)) {
				Rectangle overlap = player1.getBounds().intersection(rock18.getBounds());
				if (overlap.width < overlap.height && sy1 != 0)
					sx1 = 0;
				else if (overlap.width > overlap.height && sx1 != 0)
					sy1 = 0;
				else
					sx1 = sy1 = 2;
			
			}
			
			if (checkCollisions(player1, rock19)) {
				Rectangle overlap = player1.getBounds().intersection(rock19.getBounds());
				if (overlap.width < overlap.height && sy1 != 0)
					sx1 = 0;
				else if (overlap.width > overlap.height && sx1 != 0)
					sy1 = 0;
				else
					sx1 = sy1 = 2;
			
			}
			
			if (checkCollisions(player1, rock20)) {
				Rectangle overlap = player1.getBounds().intersection(rock20.getBounds());
				if (overlap.width < overlap.height && sy1 != 0)
					sx1 = 0;
				else if (overlap.width > overlap.height && sx1 != 0)
					sy1 = 0;
				else
					sx1 = sy1 = 2;
			
			}
			

			if (checkCollisions(player2, rock)) {
				Rectangle overlap = player2.getBounds().intersection(rock.getBounds());
				if (overlap.width < overlap.height && sy2 != 0)
					sx2 = 0;
				else if (overlap.width > overlap.height && sx2 != 0)
					sy2 = 0;
				else
					sx2 = sy2 = 2;
			
			}
			
			if (checkCollisions(player2, rock1)) {
				Rectangle overlap = player2.getBounds().intersection(rock1.getBounds());
				if (overlap.width < overlap.height && sy2 != 0)
					sx2 = 0;
				else if (overlap.width > overlap.height && sx2 != 0)
					sy2 = 0;
				else
					sx2 = sy2 = 2;
			
			}
			
			if (checkCollisions(player2, rock2)) {
				Rectangle overlap = player2.getBounds().intersection(rock2.getBounds());
				if (overlap.width < overlap.height && sy2 != 0)
					sx2 = 0;
				else if (overlap.width > overlap.height && sx2 != 0)
					sy2 = 0;
				else
					sx2 = sy2 = 2;
			
			}
			
			if (checkCollisions(player2, rock3)) {
				Rectangle overlap = player2.getBounds().intersection(rock3.getBounds());
				if (overlap.width < overlap.height && sy2 != 0)
					sx2 = 0;
				else if (overlap.width > overlap.height && sx2 != 0)
					sy2 = 0;
				else
					sx2 = sy2 = 2;
			
			}
			
			if (checkCollisions(player2, rock4)) {
				Rectangle overlap = player2.getBounds().intersection(rock4.getBounds());
				if (overlap.width < overlap.height && sy2 != 0)
					sx2 = 0;
				else if (overlap.width > overlap.height && sx2 != 0)
					sy2 = 0;
				else
					sx2 = sy2 = 2;
			
			}
			
			if (checkCollisions(player2, rock5)) {
				Rectangle overlap = player2.getBounds().intersection(rock5.getBounds());
				if (overlap.width < overlap.height && sy2 != 0)
					sx2 = 0;
				else if (overlap.width > overlap.height && sx2 != 0)
					sy2 = 0;
				else
					sx2 = sy2 = 2;
			
			}
			
			if (checkCollisions(player2, rock6)) {
				Rectangle overlap = player2.getBounds().intersection(rock6.getBounds());
				if (overlap.width < overlap.height && sy2 != 0)
					sx2 = 0;
				else if (overlap.width > overlap.height && sx2 != 0)
					sy2 = 0;
				else
					sx2 = sy2 = 2;
			
			}
			
			if (checkCollisions(player2, rock7)) {
				Rectangle overlap = player2.getBounds().intersection(rock7.getBounds());
				if (overlap.width < overlap.height && sy2 != 0)
					sx2 = 0;
				else if (overlap.width > overlap.height && sx2 != 0)
					sy2 = 0;
				else
					sx2 = sy2 = 2;
			
			}
			
			if (checkCollisions(player2, rock8)) {
				Rectangle overlap = player2.getBounds().intersection(rock8.getBounds());
				if (overlap.width < overlap.height && sy2 != 0)
					sx2 = 0;
				else if (overlap.width > overlap.height && sx2 != 0)
					sy2 = 0;
				else
					sx2 = sy2 = 2;
			
			}
			
			if (checkCollisions(player2, rock9)) {
				Rectangle overlap = player2.getBounds().intersection(rock9.getBounds());
				if (overlap.width < overlap.height && sy2 != 0)
					sx2 = 0;
				else if (overlap.width > overlap.height && sx2 != 0)
					sy2 = 0;
				else
					sx2 = sy2 = 2;
			
			}
			if (checkCollisions(player2, rock10)) {
				Rectangle overlap = player2.getBounds().intersection(rock10.getBounds());
				if (overlap.width < overlap.height && sy2 != 0)
					sx2 = 0;
				else if (overlap.width > overlap.height && sx2 != 0)
					sy2 = 0;
				else
					sx2 = sy2 = 2;
			
			}
			
			if (checkCollisions(player2, rock11)) {
				Rectangle overlap = player2.getBounds().intersection(rock11.getBounds());
				if (overlap.width < overlap.height && sy2 != 0)
					sx2 = 0;
				else if (overlap.width > overlap.height && sx2 != 0)
					sy2 = 0;
				else
					sx2 = sy2 = 2;
			
			}
			
			if (checkCollisions(player2, rock12)) {
				Rectangle overlap = player2.getBounds().intersection(rock12.getBounds());
				if (overlap.width < overlap.height && sy2 != 0)
					sx2 = 0;
				else if (overlap.width > overlap.height && sx2 != 0)
					sy2 = 0;
				else
					sx2 = sy2 = 2;
			
			}
			
			if (checkCollisions(player2, rock13)) {
				Rectangle overlap = player2.getBounds().intersection(rock13.getBounds());
				if (overlap.width < overlap.height && sy2 != 0)
					sx2 = 0;
				else if (overlap.width > overlap.height && sx2 != 0)
					sy2 = 0;
				else
					sx2 = sy2 = 2;
			
			}
			
			if (checkCollisions(player2, rock14)) {
				Rectangle overlap = player2.getBounds().intersection(rock14.getBounds());
				if (overlap.width < overlap.height && sy2 != 0)
					sx2 = 0;
				else if (overlap.width > overlap.height && sx2 != 0)
					sy2 = 0;
				else
					sx2 = sy2 = 2;
			
			}
			
			if (checkCollisions(player2, rock15)) {
				Rectangle overlap = player2.getBounds().intersection(rock15.getBounds());
				if (overlap.width < overlap.height && sy2 != 0)
					sx2 = 0;
				else if (overlap.width > overlap.height && sx2 != 0)
					sy2 = 0;
				else
					sx2 = sy2 = 2;
			
			}
			
			if (checkCollisions(player2, rock16)) {
				Rectangle overlap = player2.getBounds().intersection(rock16.getBounds());
				if (overlap.width < overlap.height && sy2 != 0)
					sx2 = 0;
				else if (overlap.width > overlap.height && sx2 != 0)
					sy2 = 0;
				else
					sx2 = sy2 = 2;
			
			}
			
			if (checkCollisions(player2, rock17)) {
				Rectangle overlap = player2.getBounds().intersection(rock17.getBounds());
				if (overlap.width < overlap.height && sy2 != 0)
					sx2 = 0;
				else if (overlap.width > overlap.height && sx2 != 0)
					sy2 = 0;
				else
					sx2 = sy2 = 2;
		
			}
			
			if (checkCollisions(player2, rock18)) {
				Rectangle overlap = player2.getBounds().intersection(rock18.getBounds());
				if (overlap.width < overlap.height && sy2 != 0)
					sx2 = 0;
				else if (overlap.width > overlap.height && sx2 != 0)
					sy2 = 0;
				else
					sx2 = sy2 = 2;
		
			}
			
			if (checkCollisions(player2, rock19)) {
				Rectangle overlap = player2.getBounds().intersection(rock19.getBounds());
				if (overlap.width < overlap.height && sy2 != 0)
					sx2 = 0;
				else if (overlap.width > overlap.height && sx2 != 0)
					sy2 = 0;
				else
					sx2 = sy2 = 2;
		
			}
			
			if (checkCollisions(player2, rock20)) {
				Rectangle overlap = player2.getBounds().intersection(rock20.getBounds());
				if (overlap.width < overlap.height && sy2 != 0)
					sx2 = 0;
				else if (overlap.width > overlap.height && sx2 != 0)
					sy2 = 0;
				else
					sx2 = sy2 = 2;
		
			}
			
			if (isPlayerDead(player1)) {
				State = State.PLAYER2WIN;
			}
			
			if (isPlayerDead(player2)) {
				State = State.PLAYER1WIN;
			}
			
		}
	}

	public ArrayList<Actor> getActors() {

		ArrayList<Actor> actors = new ArrayList<Actor>();
		actors.add(player1);
		actors.add(player2);
		actors.add(rock);
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

			respawntime = 3000;
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
			try {
				Thread.sleep(6);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
