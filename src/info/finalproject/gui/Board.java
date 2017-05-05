package info.finalproject.gui;
import java.awt.*;
import javax.swing.*;

public class Board extends JPanel
{
	private Image myImage;
	
	public Board()
	{
		initBoard();
	}
	
	private void initBoard()
	{
		loadImage();
		int h = myImage.getHeight(this);
		int w = myImage.getWidth(this);
		
        setPreferredSize(new Dimension(w, h));        
	}
	
	private void loadImage()
	{
        ImageIcon ii = new ImageIcon("images/charizard.jpg");
        myImage = ii.getImage();   
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		g.drawImage(myImage, 0, 0, null);
	}

}
