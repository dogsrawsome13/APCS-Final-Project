package info.finalproject.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import info.finalproject.actor.Actor;
import info.finalproject.actor.Craft;
import info.finalproject.actor.Player;

public class Board extends JPanel implements ActionListener
{
	private Timer myTimer;
	private Craft craft;
	private final int DELAY = 1;
	
	public Board()
	{
		initBoard();
	}
	
	private void initBoard()
	{
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.BLACK);
		craft = new Craft();
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
        g2d.drawImage(craft.getImage(), craft.getX(), craft.getY(), this);        
    }
    
    public void actionPerformed(ActionEvent e)
    {
        
        craft.move();
        repaint();  
    }
    
    private class TAdapter extends KeyAdapter
    {

        @Override
        public void keyReleased(KeyEvent e)
        {
            craft.keyReleased(e);
        }
    }
    
    public void keyPressed(KeyEvent e)
    {
        craft.keyPressed(e);
    }
    

}
