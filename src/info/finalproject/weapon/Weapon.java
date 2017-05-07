package info.finalproject.weapon;

import info.finalproject.actor.Powerup;

public class Weapon extends Powerup
{
	private int boardHeight;
    private int boardWidth;
    private int mySpeed;
    private double myDegrees;
    
    
    public Weapon(double degrees)
    {
    	super(0, 0, null);
    	boardHeight = 1000;
    	boardWidth = 1000;
    	myDegrees = degrees;
    }

	
	public Weapon(int x, int y, String imageName, double degrees)
	{
		super(x, y, imageName);

		boardHeight = 1000;
		boardWidth = 1000;
		myDegrees = degrees;
	}
	
	public int getBoardWidth()
	{
		return boardWidth;
	}
	public void move()
	{
		super.setX(super.getX() + (int) (Math.sin(myDegrees * (Math.PI/180)) * mySpeed));
	    super.setY(super.getY() + (int) (Math.cos(myDegrees * (Math.PI/180)) * -mySpeed));
	}




	
	




}
