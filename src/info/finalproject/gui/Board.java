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

import info.finalproject.actor.Player;
import info.finalproject.actor.Powerup;
import info.finalproject.weapon.Pistol;
import info.finalproject.weapon.Weapon;
public class Board extends JPanel implements Runnable
{ 
	  public static final int WIDTH = 1000, HEIGHT = 1000;
	  public static Weapon bullet;

	  private Thread loop; // the loop
	  private Player player1;
	  private ArrayList bullets;
	  private int tmpAngle, sx, sy, reload, numToShoot, spread, bWidth,
	         bHeight;
	  private boolean moveForward, canForward, canBackward, moveBackward, left,
	         right, fire, special;

	
	public Board()
	{
		initBoard();
	    addKeyListener(new Controll());
	    setFocusable(true);
	    setBackground(Color.WHITE);
	    setDoubleBuffered(true);
	    setFocusable(true);
	}
	
	private void initBoard()
	{
	    player1 = new Player(400, 300, 0, "images/Shooter.png", 50, 
	    		new Pistol(400, 300, 0, 10, 10, "images/missile.png"));
	    tmpAngle = 0;
	    special = fire = left = right = moveForward = moveBackward = false;
	    canForward = canBackward = true;
	    sx = sy = 2;

	    bullet = new Pistol(400, 300, 0, 10, 10, "images/missile.png");
	    bullets = player1.getBullets();
	    reload = 30;
	    numToShoot = 1;
	    spread = 0;


	    loop = new Thread(this);
	    loop.start();
		
	}
	
    public void paint(Graphics g)
    {        
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
              RenderingHints.VALUE_ANTIALIAS_ON);
        AffineTransform old = g2d.getTransform();

        // rotating the hero, rotation point is the middle of the square
        g2d.rotate(player1.getDirection(), player1.getX() + player1.getWidth(),
              player1.getY() + player1.getHeight() / 2);
        // draw the image
        g2d.drawImage(player1.getImage(), (int) player1.getX(), (int) player1.getY(),
              player1.getWidth(), player1.getHeight(), this);
        g2d.setTransform(old);

        // drawing the bullets
        ArrayList bullets = player1.getBullets();
        for (int i = 0; i < bullets.size(); i++)
        {
           Weapon tmpW = (Weapon) bullets.get(i);
                          //playing with bullet colors
           g2d.drawImage(tmpW.getImage(), (int) tmpW.getX(), (int) tmpW.getY(), dx2, dy2, sx1, sy1, sx2, sy2, observer)
        }
        // in case you have other things to rotate
        g2d.setTransform(old);
    }
    
    public void play()
    {

        // if the hero get off the screen
        // we make it appear from the opposite side of the screen
        if (player1.getX() > 800)
        {
           player1.setX(0);
        }
        else if (player1.getX() < -100)
        {
           player1.setX(800);
        }

        if (player1.getY() > 600)
        {
           player1.setY(0);
        }
        else if (player1.getY() < -100)
        {
           player1.setY(600);
        }

        // moving bullets
        if (player1.getWeapon() instanceof Pistol)
        {
            ArrayList tmpWs = player1.getBullets();
            for (int i = 0; i < tmpWs.size(); i++)
            {
               Weapon tmpW = (Pistol) tmpWs.get(i);

               tmpW.move();

               if (tmpW.getX() > WIDTH || tmpW.getX() < 0
                     || tmpW.getY() > HEIGHT || tmpW.getY() < 0)
               {
                  tmpWs.remove(i);
               }

            }
        }
 

        // check if shooting
        if (fire)
        {
           player1.fire(reload, numToShoot, spread);
        }
        if (special)
        {
           player1.fire(5, 3, 2);
        }

        // changing the hero angle
        if (left)
        {
           tmpAngle -= 1;
        }
        if (right)
        {
           tmpAngle += 1;
        }

        // setting the hero angle
        player1.setDirection(tmpAngle);

        // this is just to keep the angle between 0 and 360
        if (tmpAngle > 360)
        {
           tmpAngle = 0;
        } else if (tmpAngle < 0)
        {
           tmpAngle = 360;

        }

        // moving the hero
        if (moveForward)
        {
           if (canForward)
           {
              player1.moveForward(sx, sy);
           }
        }
        if (moveBackward)
        {
           if (canBackward)
           {
              player1.moveBackward(sx, sy);
           }
        }

     }
    
    // game key controll
    // (my keyboard is AZERTY so ignore the multiple key in
    // the if statement
    private class Controll extends KeyAdapter 
    {

       public void keyPressed(KeyEvent e)
       {
           if (e.getKeyCode() == KeyEvent.VK_SPACE)
           {
               fire = true;
           }

          if (e.getKeyCode() == e.VK_UP || e.getKeyCode() == e.VK_Z
                || e.getKeyCode() == e.VK_W) {
             moveForward = true;

          }
          if (e.getKeyCode() == e.VK_DOWN || e.getKeyCode() == e.VK_S) {
             moveBackward = true;

          }
          if (e.getKeyCode() == e.VK_LEFT || e.getKeyCode() == e.VK_Q
                || e.getKeyCode() == e.VK_A) {
             left = true;
          }
          if (e.getKeyCode() == e.VK_RIGHT || e.getKeyCode() == e.VK_D) {
             right = true;
          }
          if (e.getKeyCode() == e.VK_SHIFT) {
             fire = true;
          }

       }
 

/*
 *    public void checkCollisions()
    {

        Rectangle r3 = player1.getBounds();
        Rectangle r2 = powerup.getBounds();
        
        if (r3.intersects(r2))
        	powerup.setVisible(false);


        ArrayList<Pistol> ammo = player1.getAmmo();

        for (Weapon m : ammo)
        {

            Rectangle r1 = m.getBounds();

            if (r1.intersects(r2))
            {
            	m.setVisible(false);
                powerup.setVisible(false);
            }
        }
    }
 */
   
    
    
 

       public void keyReleased(KeyEvent e)
       {
     	  if (e.getKeyCode() == KeyEvent.VK_SPACE)
           {
               fire = false;
           }

          if (e.getKeyCode() == e.VK_UP || e.getKeyCode() == e.VK_Z
                || e.getKeyCode() == e.VK_W)
          {
             moveForward = false;
          }
          if (e.getKeyCode() == e.VK_DOWN || e.getKeyCode() == e.VK_S)
          {
             moveBackward = false;
          }
          if (e.getKeyCode() == e.VK_LEFT || e.getKeyCode() == e.VK_Q
                || e.getKeyCode() == e.VK_A)
          {

             left = false;
          }
          if (e.getKeyCode() == e.VK_RIGHT || e.getKeyCode() == e.VK_D)
          {
             right = false;
          }
          if (e.getKeyCode() == e.VK_SHIFT)
          {
             fire = false;
          }
       }
    }

    

	@Override
	public void run() 
	{
		
		while (true)
	      {
	         repaint();
	         play();
	         try
	         {
	            Thread.sleep(5);
	         }
	         catch (InterruptedException e)
	         {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	         }

	      }

		
	}

}
