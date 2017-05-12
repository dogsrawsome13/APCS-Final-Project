package info.finalproject.weapon;

public class MachineGun extends Weapon{
	
	private int myAttack;
    private int mySpeed;
    
	public MachineGun()
	{
		super(0, 0, 0, 0, 0, null);
		myAttack = 8;
		mySpeed = 12;
	}
	
	public MachineGun(double x, double y, double direction, int width, 
			int height, String imageName){
		super(x, y, direction, width, height, imageName);
		myAttack = 8;
		mySpeed = 12;
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