package info.finalproject.actor;

import java.awt.*;
import javax.swing.*;

import info.finalproject.gui.*;

public class Actor {
	private int myHeight;
	private int myWidth;
	private double myX;
	private double myY;
	private double myDegrees;
	private boolean visible;
	private Image myImage;
	private Board myBoard;
	
	public Actor(double x, double y, double degrees, int width, int height, String imageName, Board board) {
		myX = x;
		myY = y;
		myDegrees = degrees;
		myHeight = width;
		myWidth = height;
		visible = true;
		loadImage(imageName);
		myBoard = board;
	}
	
	public void loadImage(String imageName) {
	    ImageIcon ii = new ImageIcon(imageName);
	    myImage = ii.getImage();
	}
	
	public int getHeight() {
		return myHeight;
	}
	
	public int getWidth() {
		return myWidth;
	}
	
    public double getDirection() {
    	return myDegrees;
    }
    
    public double getX() {
        return myX;
    }

    public double getY() {
        return myY;
    }
    
    public Image getImage() {
        return myImage;
    }
    
    public void setHeight(int height) {
    	myHeight = height;
    }
    
    public void setWidth(int width) {
    	myWidth = width;
    }
    
    public void setX(double x) {
    	myX = x;
    }
    
    public void setY(double y) {
    	myY = y;
    }
    
    public void setDirection(double degrees) {
    	myDegrees = Math.toRadians(degrees);
    }
    

    public boolean isVisible() {
    	return visible;
    }
    
    public void setVisible(boolean vis) {
    	visible = vis;
    }
    
    public void rotateLeft() {
        myDegrees = myDegrees - 5;
    }
    public void rotateRight() {
        myDegrees = myDegrees + 5;
    }
    
    public Rectangle getBounds() {
    	return new Rectangle((int) myX, (int) myY, myWidth, myHeight);
    }
    
    public String toString() {
    	return myX + "," + myY;
    }
    
    public Board getBoard() {
    	return myBoard;
    }
}
    
	
	
	
	


