package info.finalproject.application;

import java.awt.EventQueue;
import javax.swing.JFrame;
import info.finalproject.gui.*;

public class Application extends JFrame
{
	
	  public Application() {

	      add(new Board());

	      setTitle("Application");
	      setSize(1920, 1080);
	      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      setResizable(true);
	      setVisible(true);
	   }
	  
	  public static void main(String[] args) {
	      new Application();
	   }
    
   
    
}
