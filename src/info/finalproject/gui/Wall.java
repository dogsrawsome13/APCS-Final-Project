package info.finalproject.gui;

public class Wall extends Rock {
	private double myDegrees;
	public Wall(double x, double y, double degrees, int width, int height, String imageName, Board board) {
		super(x, y, width, height, imageName, board);
		myDegrees = degrees;
		setDirection(myDegrees);
	}
	public double getDegrees() {
		return myDegrees;
	}
}
