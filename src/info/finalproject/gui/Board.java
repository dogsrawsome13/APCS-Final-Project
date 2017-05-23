package info.finalproject.gui;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.util.ArrayList;

import javax.swing.*;

import info.finalproject.actor.Actor;
import info.finalproject.actor.Player;
import info.finalproject.actor.Powerup;
import info.finalproject.weapon.Pistol;
import info.finalproject.weapon.Weapon;

public class Board extends JPanel implements Runnable { 
	  public static Weapon bullet;

	  private Thread loop; // the loop
	  private Player player1, player2;
	  private Powerup powerup;
	  private ArrayList<Weapon> bullets, bullets2;
	  private int tmpAngle,tmpAngle2, sx, sy, reload, numToShoot, spread;
	  private boolean moveForward, canForward, canBackward, moveBackward, left,
	         right, fire, special;
	  private boolean moveForward2, canForward2, canBackward2, moveBackward2, left2,
      right2, fire2, special2;

	
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
	    		new Pistol(400, 300, 0, 50, 50, "pistol.png", this), this);
	    player2 = new Player(400, 300, 0, 50, 50, "images/Shooter.png", 30,
	    		new Pistol(400, 300, 0, 50, 50, "pistol.png", this), this);
	    powerup = new Powerup(500, 500, 0, 20, 20, "images/crate.png", this);
	    tmpAngle = 0;
	    tmpAngle2 = 0;
	    special = fire = left = right = moveForward = moveBackward = false;
	    special2 = fire2 = left2 = right2 = moveForward2 = moveBackward2 = false;
	    canForward = canBackward = true;
	    canForward2 = canBackward2 = true;
	    sx = sy = 2;
	    bullet = new Weapon(0, 0, 0, 0, 0, null, null);
	    bullets = player1.getBullets();
	    bullets2 = player2.getBullets();
	    reload = 30;
	    numToShoot = 1;
	    spread = 0;

	    loop = new Thread(this);
	    loop.start();	
	}
	
    public void paint(Graphics g) {        
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
              RenderingHints.VALUE_ANTIALIAS_ON);
        AffineTransform old = g2d.getTransform();
        
        g2d.drawString("" + player1.getHealth(), (int) player1.getX() + 10, (int) player1.getY() - 50);
        g2d.drawString("" + player2.getHealth(), (int) player2.getX() + 10, (int) player2.getY() - 50);

        
        if (powerup.isVisible())
        	g2d.drawImage(powerup.getImage(), (int) powerup.getX(), (int) powerup.getY(), powerup.getWidth(),
        		powerup.getHeight(), this);
        
        

        // rotating player1, rotation point is the middle of the square
        g2d.rotate(player1.getDirection(), player1.getX() + player1.getWidth() / 2,
              player1.getY() + player1.getHeight() / 2);
        
        // draw the image
        g2d.drawImage(player1.getImage(), (int) player1.getX(), (int) player1.getY(),
              player1.getWidth(), player1.getHeight(), this);
        g2d.setTransform(old);
        
        // rotating player2, rotation point is the middle of the square
        g2d.rotate(player2.getDirection(), player2.getX() + player2.getWidth() / 2,
              player2.getY() + player2.getHeight() / 2);
        
        // draw the image
        g2d.drawImage(player2.getImage(), (int) player2.getX(), (int) player2.getY(),
              player2.getWidth(), player2.getHeight(), this);
        g2d.setTransform(old);
        
        

        // drawing player1 bullets
        ArrayList<Weapon> bullets = player1.getBullets();
        for (int i = 0; i < bullets.size(); i++) {
            Weapon tmpB = (Weapon) bullets.get(i);
            //playing with bullet colors
            if (i % 2 == 0) 
            	g2d.setColor(new Color(150, 130, 100));
            else 
            	g2d.setColor(new Color(60, 20, 120));
            g2d.fillRect((int) tmpB.getX(), (int) tmpB.getY(), tmpB.getWidth(),
            		tmpB.getHeight());
        }
        // in case you have other things to rotate
        g2d.setTransform(old);
        
        // drawing player2 bullets
        ArrayList<Weapon> bullets2 = player2.getBullets();
        for (int i = 0; i < bullets2.size(); i++) {
            Weapon tmpB = (Weapon) bullets2.get(i);
            //playing with bullet colors
            if (i % 2 == 0) 
            	g2d.setColor(new Color(150, 130, 100));
            else 
            	g2d.setColor(new Color(60, 20, 120));
            g2d.fillRect((int) tmpB.getX(), (int) tmpB.getY(), tmpB.getWidth(),
            		tmpB.getHeight());
        }
        // in case you have other things to rotate
        g2d.setTransform(old);
        
        
        

        
        		
    }
    
    public boolean checkCollisions(Actor a1, Actor a2) {
    	Rectangle r1 = a1.getBounds();
        Rectangle r2 = a2.getBounds();
            
        if (r1.intersects(r2)) {
        	Actor powerupItem = null;
        	if(a1 != player1 && a1 instanceof Powerup)
        		powerupItem = ((Powerup) a1).givePowerup();
        	if(a2 != player1 && a2 instanceof Powerup)
        		powerupItem = ((Powerup) a2).givePowerup();
        	if(powerupItem != null && powerupItem instanceof Weapon)
        		player1.setWeapon((Weapon) powerupItem);
        	if(a1 instanceof Player && a2 instanceof Player)
        		return false;
        	if((a1 instanceof Powerup && a2 instanceof Weapon) || (a1 instanceof Weapon && a2 instanceof Powerup))
        		return false;
        	if(a1 instanceof Weapon && a2 instanceof Weapon)
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
        
        
        // moving player1 bullets
        ArrayList<Weapon> tmpWs = player1.getBullets();
        for (int i = 0; i < tmpWs.size(); i++) {
        	Weapon tmpW = (Weapon) tmpWs.get(i);
        	tmpW.move();
        	if (tmpW.getX() > 3500 || tmpW.getX() < 0
                         || tmpW.getY() > 2000 || tmpW.getY() < 0)
        		tmpWs.remove(i);
        	}

        // moving player2 bullets
        ArrayList<Weapon> tmpWs2 = player2.getBullets();
                
        for (int i = 0; i < tmpWs2.size(); i++) {
        	Weapon tmpW2 = (Weapon) tmpWs2.get(i);
        	tmpW2.move();
        	if (tmpW2.getX() > 3500 || tmpW2.getX() < 0
                         || tmpW2.getY() > 2000 || tmpW2.getY() < 0)
        		tmpWs2.remove(i);
                
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
        
        if(checkCollisions(player1, powerup)){
        	Actor powerupItem = powerup.givePowerup();
        	if(powerupItem instanceof Weapon)
        		player1.setWeapon((Weapon) powerupItem);
        	powerup.removeSelf();
        }
        
        if(checkCollisions(player2, powerup)){
        	Actor powerupItem = powerup.givePowerup();
        	if(powerupItem instanceof Weapon)
        		player2.setWeapon((Weapon) powerupItem);
        	powerup.removeSelf();
        }

        System.out.println(player1.toString());
     }
    
    // game key controll

    private class Controll extends KeyAdapter {

       public void keyPressed(KeyEvent e) {
           if (e.getKeyCode() == KeyEvent.VK_SPACE)
               fire = true;
          if (e.getKeyCode() == e.VK_UP)
             moveForward = true;
          if (e.getKeyCode() == e.VK_DOWN)
             moveBackward = true;
          if (e.getKeyCode() == e.VK_LEFT)
             left = true;
          if (e.getKeyCode() == e.VK_RIGHT)
             right = true;
          
          if (e.getKeyCode() == KeyEvent.VK_T)
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
     	  if (e.getKeyCode() == KeyEvent.VK_SPACE)
               fire = false;
          if (e.getKeyCode() == e.VK_UP)
             moveForward = false;
          if (e.getKeyCode() == e.VK_DOWN)
             moveBackward = false;
          if (e.getKeyCode() == e.VK_LEFT)
             left = false;
          if (e.getKeyCode() == e.VK_RIGHT)
             right = false;
          
     	  if (e.getKeyCode() == KeyEvent.VK_T)
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
       
       
    } //end private class
    
	@Override
	public void run() {
		
		while (true) {
	         repaint();
	         play();
	         try {
	            Thread.sleep(5);
	         }
	         catch (InterruptedException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	         }
	      }
	}
	public ArrayList<Actor> getActors() {
		
		ArrayList<Actor> actors = new ArrayList<Actor>();
		actors.add(player1);
		actors.add(powerup);
		for(Weapon bullet: bullets) {
			actors.add(bullet);
		}
		return actors;
		
	}
}
