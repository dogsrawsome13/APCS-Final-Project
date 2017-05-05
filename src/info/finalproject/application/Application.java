package info.finalproject.application;

import java.awt.EventQueue;
import javax.swing.JFrame;
import info.finalproject.gui.*;

public class Application extends JFrame
{
    
    public Application()
    {
        initUI();
    }

    private void initUI()
    {

        add(new Board());

        setSize(1000, 1000);

        setTitle("Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }    
    
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                Application ex = new Application();
                ex.setVisible(true);
            }
        }
        );
    }
}
