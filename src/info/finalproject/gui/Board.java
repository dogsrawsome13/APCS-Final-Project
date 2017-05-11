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
	  public static Weapon bullet;

	  private Thread loop; // the loop
	  private Player player1;
	  private ArrayList<Weapon> bullets;
	  private int tmpAngle, sx, sy, reload, numToShoot, spread;
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
	    player1 = new Player(400, 300, 0, 50, 50, "images/Shooter.png", 30,
	    		null);
	    tmpAngle = 0;
	    special = fire = left = right = moveForward = moveBackward = false;
	    canForward = canBackward = true;
	    sx = sy = 2;
	    bullet = new Weapon(0, 0, 0, 0, 0, null);
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
            Weapon tmpB = (Weapon) bullets.get(i);
            //playing with bullet colors
            if (i % 2 == 0) 
            {
            	g2d.setColor(new Color(150, 130, 100));
            } 
            else 
            {
            	g2d.setColor(new Color(60, 20, 120));
            }
            g2d.fillRect((int) tmpB.getX(), (int) tmpB.getY(), tmpB.getWidth(),
            		tmpB.getHeight());
        }
        // in case you have other things to rotate
        g2d.setTransform(old);
    }
    
    public void play()
    {

        // if the hero get off the screen
        // we make it appear from the opposite side of the screen
        if (player1.getX() > 2000)
        {
           player1.setX(0);
        }
        else if (player1.getX() < -100)
        {
           player1.setX(2000);
        }

        if (player1.getY() > 2000)
        {
           player1.setY(0);
        }
        else if (player1.getY() < -100)
        {
           player1.setY(2000);
        }

        // moving bullets
        
        	{
                ArrayList<Weapon> tmpWs = player1.getBullets();
                
                for (int i = 0; i < tmpWs.size(); i++)
                {
                   Weapon tmpW = (Weapon) tmpWs.get(i);

                   tmpW.move();

                   if (tmpW.getX() > 2000 || tmpW.getX() < 0
                         || tmpW.getY() > 2000 || tmpW.getY() < 0)
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
        }
        else if (tmpAngle < 0)
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

    private class Controll extends KeyAdapter 
    {

       public void keyPressed(KeyEvent e)
       {
           if (e.getKeyCode() == KeyEvent.VK_SPACE)
           {
               fire = true;
           }

          if (e.getKeyCode() == e.VK_UP)
          {
             moveForward = true;
          }
          if (e.getKeyCode() == e.VK_DOWN)
          {
             moveBackward = true;
          }
          
          if (e.getKeyCode() == e.VK_LEFT)
          {
             left = true;
          }
          if (e.getKeyCode() == e.VK_RIGHT)
          {
             right = true;
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

          if (e.getKeyCode() == e.VK_UP)
          {
             moveForward = false;
          }
          if (e.getKeyCode() == e.VK_DOWN)
          {
             moveBackward = false;
          }
          if (e.getKeyCode() == e.VK_LEFT)
          {
             left = false;
          }
          if (e.getKeyCode() == e.VK_RIGHT)
          {
             right = false;
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
