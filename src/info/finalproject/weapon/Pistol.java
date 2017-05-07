package info.finalproject.weapon;

import java.awt.Image;

public class Pistol extends Weapon
{
	private int myAttack;
    private int mySpeed;
    
    public Pistol()
    {
    	super(0, 0, null, 0);
    	myAttack = 10;
    	mySpeed = 10;
    	
    }

	public Pistol(int x, int y, String imageName, double degrees)
	{
		super(x, y, imageName, degrees);
		myAttack = 10;
		mySpeed = 10;
	}
	
	public void setAttack(int attack)
	{
		myAttack = attack;
	}
	
    public void move()
    {
        super.setX(super.getX() + mySpeed);
        
        if (super.getX() > super.getBoardWidth())
        {
            super.setVisible(false);
        }
    }

}
