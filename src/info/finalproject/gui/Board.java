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
import info.finalproject.weapon.MachineGun;
import info.finalproject.weapon.Pistol;
import info.finalproject.weapon.Weapon;

public class Board extends JPanel implements Runnable
{ 
	  public static Weapon bullet;
	  private Thread loop; // the loop
	  private Player player1;
	  private Player player2;
	  private Powerup powerup;
	  private ArrayList<Weapon> bullets1, bullets2;
	  private int tmpAngle1, tmpAngle2, sx, sy, reload, numToShoot, spread;
	  private boolean moveForward1, canForward1, canBackward1, moveBackward1, left1,
	         right1, fire1, special1;
	  private boolean moveForward2, canForward2, canBackward2, moveBackward2, left2,
      right2, fire2, special2;
	 

	
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
	    player1 = new Player(400, 300, 0, 75, 75, "images/Shooter.png", 30);
	    player2 = new Player(400, 300, 0, 75, 75, "images/Shooter.png", 30);
	    powerup = new Powerup(500, 500, 0, 20, 20, "images/crate.png");
	    tmpAngle1 = 0;
	    tmpAngle2 = 0;
	    special1 = fire1 = left1 = right1 = moveForward1 = moveBackward1 = false;
	    canForward1 = canBackward1 = true;
	    special2 = fire2 = left2 = right2 = moveForward2 = moveBackward2 = false;
	    canForward2 = canBackward2 = true;

	    sx = sy = 2;
	    bullet = new Weapon(0, 0, 0, 0, 0, null);

	    

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
        
        if (powerup.isVisible())
        	g2d.drawImage(powerup.getImage(), (int) powerup.getX(), (int) powerup.getY(), powerup.getWidth(),
        		powerup.getHeight(), this);


        // rotating the players, rotation point is the middle of the square
        g2d.rotate(player1.getDirection(), player1.getX() + player1.getWidth(),
              player1.getY() + player1.getHeight() / 2);
        
        // draw the image of player 1
        g2d.drawImage(player1.getImage(), (int) player1.getX(), (int) player1.getY(),
              player1.getWidth(), player1.getHeight(), this);
        g2d.setTransform(old);
        
        // draw the image of player 2
        g2d.rotate(player2.getDirection(), player2.getX() + player2.getWidth(),
                player2.getY() + player2.getHeight() / 2);
        g2d.drawImage(player2.getImage(), (int) player2.getX(), (int) player2.getY(),
                player2.getWidth(), player2.getHeight(), this);
        g2d.setTransform(old);
        



        // drawing the bullets
        ArrayList bullets1 = player1.getBullets();
        for (int i = 0; i < bullets1.size(); i++)
        {
            Weapon tmpB = (Weapon) bullets1.get(i);
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
        
        // drawing the bullets
        ArrayList bullets2 = player2.getBullets();
        for (int i = 0; i < bullets2.size(); i++)
        {
            Weapon tmpB = (Weapon) bullets2.get(i);
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
    public void checkCollisions()
    {
    	Rectangle player1Rec = player1.getBounds();
    	Rectangle player2Rec = player2.getBounds();
        Rectangle powerupRec = powerup.getBounds();
       
      
            
        if (player1Rec.intersects(powerupRec))
        {
        	player1.updateWeapon(new MachineGun());
        	powerup.setVisible(false);
        }
        if (player2Rec.intersects(powerupRec))
        {
        	player2.updateWeapon(new MachineGun());
        	powerup.setVisible(false);
        }
        

     }
    

    public void play()
    {

        // if the hero get off the screen
        // we make it appear from the opposite side of the screen
        if (player1.getX() > 3000)
        {
           player1.setX(0);
        }
        else if (player1.getX() < -100)
        {
           player1.setX(3000);
        }

        if (player1.getY() > 1800)
        {
           player1.setY(0);
        }
        else if (player1.getY() < -100)
        {
           player1.setY(1800);
        }
        

        // moving pistol bullets
        if (player1.getWeapon() instanceof Pistol)
        {
            ArrayList<Weapon> tmpWs = player1.getBullets();
            
            for (int i = 0; i < tmpWs.size(); i++)
            {
               Weapon tmpW = (Weapon) tmpWs.get(i);

               tmpW.move();
               System.out.println(player1.typeOfWeapon() + " " + tmpW.getX());

               if (tmpW.getX() > 2000 || tmpW.getX() < 0
                     || tmpW.getY() > 2000 || tmpW.getY() < 0)
               {
                  tmpWs.remove(i);
               }
            }
        }
        
        // moving machinegun bullets
        if (player1.getWeapon() instanceof MachineGun)
        {
            ArrayList<Weapon> tmpWs = player1.getBullets();
            
            for (int i = 0; i < tmpWs.size(); i++)
            {
               Weapon tmpW = (Weapon) tmpWs.get(i);

               tmpW.move();
               System.out.println(player1.typeOfWeapon() + " " + tmpW.getX());

               if (tmpW.getX() > 2000 || tmpW.getX() < 0
                     || tmpW.getY() > 2000 || tmpW.getY() < 0)
               {
                  tmpWs.remove(i);
               }
            }
        }

        
        // moving pistol bullets
        if (player2.getWeapon() instanceof Pistol)
        {
            ArrayList<Weapon> tmpWs = player2.getBullets();
            
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
        
        // moving machinegun bullets
        if (player2.getWeapon() instanceof MachineGun)
        {
            ArrayList tmpWs = player2.getBullets();
            
            for (int i = 0; i < tmpWs.size(); i++)
            {
               Weapon tmpW = (Weapon) tmpWs.get(i);

               tmpW.move();
               System.out.println(player1.typeOfWeapon() + " " + tmpW.getX());

               if (tmpW.getX() > 2000 || tmpW.getX() < 0
                     || tmpW.getY() > 2000 || tmpW.getY() < 0)
               {
                  tmpWs.remove(i);
               }
            }
        }

        // check if shooting
        if (fire1)
        {
           player1.fire(reload, numToShoot, spread);
        }
        if (special1)
        {
           player1.fire(5, 3, 2);
        }

        // changing the hero angle
        if (left1)
        {
           tmpAngle1 -= 1;
        }
        if (right1)
        {
           tmpAngle1 += 1;
        }

        // setting the hero angle
        player1.setDirection(tmpAngle1);

        // this is just to keep the angle between 0 and 360
        if (tmpAngle1 > 360)
        {
           tmpAngle1 = 0;
        }
        else if (tmpAngle1 < 0)
        {
           tmpAngle1 = 360;
        }

        // moving the hero
        if (moveForward1)
        {
           if (canForward1)
           {
              player1.moveForward(sx, sy);
           }
        }
        
        if (moveBackward1)
        {
           if (canBackward1)
           {
              player1.moveBackward(sx, sy);
           }
        }
        
        checkCollisions();
        
        if (fire2)
        {
           player2.fire(reload, numToShoot, spread);
        }
        if (special2)
        {
           player2.fire(5, 3, 2);
        }

        // changing the hero angle
        if (left2)
        {
           tmpAngle2 -= 1;
        }
        if (right2)
        {
           tmpAngle2 += 1;
        }

        // setting the hero angle
        player2.setDirection(tmpAngle2);

        // this is just to keep the angle between 0 and 360
        if (tmpAngle2 > 360)
        {
           tmpAngle2 = 0;
        }
        else if (tmpAngle2 < 0)
        {
           tmpAngle2 = 360;
        }

        // moving the hero
        if (moveForward2)
        {
           if (canForward2)
           {
              player2.moveForward(sx, sy);
           }
        }
        
        if (moveBackward2)
        {
           if (canBackward2)
           {
              player2.moveBackward(sx, sy);
           }
        }
        
        checkCollisions();

     }
    
    // game key controll

    private class Controll extends KeyAdapter 
    {

       public void keyPressed(KeyEvent e)
       {
           if (e.getKeyCode() == KeyEvent.VK_SPACE)
           {
               fire1 = true;
           }

          if (e.getKeyCode() == e.VK_UP)
          {
             moveForward1 = true;
          }
          if (e.getKeyCode() == e.VK_DOWN)
          {
             moveBackward1 = true;
          }
          
          if (e.getKeyCode() == e.VK_LEFT)
          {
             left1 = true;
          }
          if (e.getKeyCode() == e.VK_RIGHT)
          {
             right1 = true;
          }
          
          if (e.getKeyCode() == KeyEvent.VK_SPACE)
          {
              fire1 = true;
          }

         if (e.getKeyCode() == e.VK_W)
         {
            moveForward2 = true;
         }
         if (e.getKeyCode() == e.VK_S)
         {
            moveBackward2 = true;
         }
         
         if (e.getKeyCode() == e.VK_A)
         {
            left2 = true;
         }
         if (e.getKeyCode() == e.VK_D)
         {
            right2 = true;
         }
         if (e.getKeyCode() == KeyEvent.VK_T)
         {
             fire2 = true;
         }

       }
 

 

       public void keyReleased(KeyEvent e)
       {
     	  if (e.getKeyCode() == KeyEvent.VK_SPACE)
           {
               fire1 = false;
           }

          if (e.getKeyCode() == e.VK_UP)
          {
             moveForward1 = false;
          }
          if (e.getKeyCode() == e.VK_DOWN)
          {
             moveBackward1 = false;
          }
          if (e.getKeyCode() == e.VK_LEFT)
          {
             left1 = false;
          }
          if (e.getKeyCode() == e.VK_RIGHT)
          {
             right1 = false;
          }
          
     	  if (e.getKeyCode() == KeyEvent.VK_SPACE)
          {
              fire1 = false;
          }

         if (e.getKeyCode() == e.VK_W)
         {
            moveForward2 = false;
         }
         if (e.getKeyCode() == e.VK_S)
         {
            moveBackward2 = false;
         }
         if (e.getKeyCode() == e.VK_A)
         {
            left2 = false;
         }
         if (e.getKeyCode() == e.VK_D)
         {
            right2 = false;
         }
    	 if (e.getKeyCode() == KeyEvent.VK_T)
         {
             fire2 = false;
         }
       }
    } //end private class
    
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
