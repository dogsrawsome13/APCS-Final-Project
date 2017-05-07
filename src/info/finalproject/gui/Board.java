package info.finalproject.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.*;
import java.util.ArrayList;

import javax.swing.*;

import info.finalproject.actor.Player;
import info.finalproject.actor.Powerup;
import info.finalproject.weapon.Pistol;
import info.finalproject.weapon.Weapon;
public class Board extends JPanel implements ActionListener
{
	private Timer myTimer;
	private Player player1;
	private Powerup powerup;
	private final int DELAY = 1;
	
	public Board()
	{
		initBoard();
	}
	
	private void initBoard()
	{
		addKeyListener(new TAdapter());
		setFocusable(true);
        setDoubleBuffered(true);
		setBackground(Color.WHITE);
		player1 = new Player(100, 100, "images/Shooter.png", 100, 10, 10, 180, new Pistol());
		powerup = new Powerup(400, 400, "images/crate.png");
		myTimer = new Timer(DELAY, this);
		myTimer.start();
		
	}
	
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        doDrawing(g);

        Toolkit.getDefaultToolkit().sync();
    }
    
    private void doDrawing(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g.create();
        g2d.drawImage(powerup.getImage(), 100, 100, 200, 200, 0, 0,
        		powerup.getX(), powerup.getY(), null);
        
      
        g2d.rotate(Math.toRadians(player1.getDegrees() -90), player1.getX() + 
        		player1.getImage().getWidth(null) / 2,
        		player1.getY() + player1.getImage().getHeight(null)/2);
        
        g2d.drawImage(player1.getImage(), player1.getX(), player1.getY(), null);

        ArrayList<Weapon> weapon = player1.getAmmo();
        for (Object m1 : weapon)
        {
            Weapon m = (Weapon) m1;
            g2d.drawImage(m.getImage(), m.getX(),
                    m.getY(), this);
        }
        
 
        g2d.dispose();       
    }
    
    public void actionPerformed(ActionEvent e)
    {
        updateMissiles();
        repaint();  
    }
    
    private void updateMissiles()
    {

        ArrayList<Weapon> weapon = player1.getAmmo();

        for (int i = 0; i < weapon.size(); i++)
        {

            Weapon m = (Weapon) weapon.get(i);

            if (m.isVisible())
            {

                m.move();
            }
            else
            {
                weapon.remove(i);
            }
        }
    }
    
    private class TAdapter extends KeyAdapter
    {
        public void keyPressed(KeyEvent e)
        {
            player1.keyPressed(e);
        }
    }
    
  
    

}
