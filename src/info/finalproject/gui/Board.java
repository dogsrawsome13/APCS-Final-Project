package info.finalproject.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.*;

import javax.swing.*;

import info.finalproject.actor.Player;
public class Board extends JPanel implements ActionListener
{
	private Timer myTimer;
	private Player player1;
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
		player1 = new Player(100, 10, 10, 180);
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
        g2d.rotate(Math.toRadians(player1.getDegrees() -90), player1.getX() + 
        		player1.getImage().getWidth(null) / 2,
        		player1.getY() + player1.getImage().getHeight(null)/2);
        
        g2d.drawImage(player1.getImage(), player1.getX(), player1.getY(), null);
        g2d.dispose();       
    }
    
    public void actionPerformed(ActionEvent e)
    {
        
        player1.move();
        repaint();  
    }
    
    private class TAdapter extends KeyAdapter
    {
        public void keyPressed(KeyEvent e)
        {
            player1.keyPressed(e);
        }
    }
    
  
    

}
