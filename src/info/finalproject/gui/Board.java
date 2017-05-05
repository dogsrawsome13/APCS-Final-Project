package info.finalproject.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

import info.finalproject.actor.Actor;
import info.finalproject.actor.Craft;
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
		setBackground(Color.WHITE);
		player1 = new Player(100, 10, 10);
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
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(player1.getImage(), player1.getX(), player1.getY(), this);        
    }
    
    public void actionPerformed(ActionEvent e)
    {
        
        player1.move();
        repaint();  
    }
    
    private class TAdapter extends KeyAdapter
    {

        @Override
        public void keyReleased(KeyEvent e)
        {
            player1.keyReleased(e);
        }
        
        public void keyPressed(KeyEvent e)
        {
            player1.keyPressed(e);
        }
    }
    
  
    

}
