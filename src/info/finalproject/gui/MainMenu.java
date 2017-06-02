package info.finalproject.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class MainMenu {
	
	public void render(Graphics g) {
		Font fnt0 = new Font("arial", Font.BOLD, 100);
		g.setFont(fnt0);
		g.setColor(Color.BLACK);
		g.drawString("OPERATION SALAMANDER", 900, 500);
		
		fnt0 = new Font("arial", Font.ITALIC, 50);
		g.setFont(fnt0);
		g.drawString("Welcome to The Arena. You have one task. "
				+ "Kill your opponent. ", 600, 700);
		
		fnt0 = new Font("arial", Font.PLAIN, 50);
		g.setFont(fnt0);
		g.drawString("Player 1 Controls: Arrow Keys to Move. Spacebar to fire weapon.", 600, 1000);
		g.drawString("Player 2 Controls: WASD to Move. T to fire weapon. ", 600, 1100);
		g.drawString("Click anywhere to start game", 600, 1300);
		
		
	}

}
