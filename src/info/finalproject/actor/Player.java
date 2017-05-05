package info.finalproject.actor;

public class Player extends Actor
{
	private int myHealth;
	private int myAmmo;
	private int mySpeed;
	
	public Player(int health, int ammo, int speed)
	{
		super();
		myHealth = health;
		myAmmo = ammo;
		mySpeed = speed;
		
	}
	
	public int getHealth()
	{
		return myHealth;
	}

}
