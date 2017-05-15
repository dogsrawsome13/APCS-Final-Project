package info.finalproject.weapon;

public class RPG extends Weapon {
	
	private int myAttack;
    private int mySpeed;
    
	public RPG(){
		super(0, 0, 0, 0, 0, null);
		myAttack = 20;
		mySpeed = 7;
	}
	public RPG(double x, double y, double direction, int width, 
			int height, String imageName){
		super(x, y, direction, width, height, imageName);
		myAttack = 8;
		mySpeed = 12;
	}

    public void move(){
        super.setX(super.getX() + mySpeed);
        
        if (super.getX() > super.getBoardWidth())
            super.setVisible(false);
    }
    public String toString(){
    	return "RPG";
    }
}
