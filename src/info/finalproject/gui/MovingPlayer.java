package info.finalproject.gui;
import java.awt.EventQueue;
import javax.swing.JFrame;

public class MovingPlayer extends JFrame
{
	public MovingPlayer()
	{
		initUI();
	}
	
	private void initUI()
	{
	      
        add(new Board());
        
        setSize(1000, 1000);
        setResizable(false);
        
        setTitle("Moving Player");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
    public static void main(String[] args)
    {
        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                MovingPlayer ex = new MovingPlayer();
                ex.setVisible(true);
            }
        });
    }
}
	


