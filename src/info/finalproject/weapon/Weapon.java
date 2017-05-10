package info.finalproject.weapon;

import info.finalproject.actor.Powerup;

public class Weapon extends Powerup
{
	private int boardHeight;
    private int boardWidth;
    private int mySpeed;
    private double myDirection;

	
	public Weapon(double x, double y, double direction, int w, int h, String imageName)
	{
		super(x, y, w, h, imageName);

		boardHeight = 2000;
		boardWidth = 2000;
		myDirection = direction;
	}
	
	public int getBoardWidth()
	{
		return boardWidth;
	}
	
	public double getBoardHeight()
	{
		return boardHeight;
	}
	
	public void move()
	{
		super.setX(super.getX() + (int) (Math.cos(myDirection) * mySpeed));
	    super.setY(super.getY() + (int) (Math.sin(myDirection) * mySpeed));
	}




	
	




}
