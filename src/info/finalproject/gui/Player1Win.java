package info.finalproject.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Player1Win {

	public void render(Graphics g) {
		Font fnt0 = new Font("arial", Font.BOLD, 100);
		g.setFont(fnt0);
		g.setColor(Color.BLACK);
		g.drawString("PLAYER 1 WINS ", 900, 500);
		
	}
}
