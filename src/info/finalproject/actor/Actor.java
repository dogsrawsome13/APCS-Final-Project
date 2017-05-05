package info.finalproject.actor;

import java.awt.*;

import info.finalproject.map.*;

public abstract class Actor 
{
	private int myHeight;
	private int myWidth;
	private int myDirection;
	private Location myLocation;
	private Color myColor;
	
	public Actor()
	{
		myHeight = 50;
		myWidth = 50;
		myDirection = 0;
		myLocation = null;
	}
	
	

}
