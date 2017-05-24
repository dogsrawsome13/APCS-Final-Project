package info.finalproject.gui;

import info.finalproject.actor.Actor;
import info.finalproject.weapon.Weapon;

public class Rock extends Actor {
	
	Rock(double x, double y, int width, int height, String imageName, Board board) {
		super(x, y, 0, width, height, imageName, board);
		
	}
	
	public boolean isDestroyed(Weapon weapon) {
		if (weapon.getBounds().intersects(super.getBounds()))
			return true;
		else
			return false;
			
	}

}
