package info.finalproject.weapon;

public class MachineGun extends Weapon{
	
	private int myAttack;
    private int mySpeed;
    
	public MachineGun()
	{
		super(0, 0, 0, 0, 0, null);
		super.setX(super.getX());
		super.setY(super.getY());
		super.setDirection(super.getDirection());
		
		myAttack = 8;
		mySpeed = 12;
	}
	
	public MachineGun(double x, double y, double direction, int width, 
			int height, String imageName){
		super(x, y, direction, width, height, imageName);
		myAttack = 8;
		mySpeed = 12;
	}
<<<<<<< HEAD
	public void setAttack(int attack)
	{
		myAttack = attack;
	}
	
	public void move()
	{
		super.setX(super.getX() + (int) (Math.cos(super.getDirection()) * mySpeed));
	    super.setY(super.getY() + (int) (Math.sin(super.getDirection()) * mySpeed));
	}

=======

    public void move(){
        super.setX(super.getX() + mySpeed);
        
        if (super.getX() > super.getBoardWidth())
            super.setVisible(false);
    }
>>>>>>> 77c951c58e3fc8409c58f643b2c3cf2bfe83c86c
}