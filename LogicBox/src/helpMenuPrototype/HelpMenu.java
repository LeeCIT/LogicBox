package helpMenuPrototype;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class HelpMenu extends JFrame
{
	public HelpMenu() 
	{
		setSize(300, 300);
		setTitle("Help Menu");
	}
	
	
	/**
	 * Sets a KeyListener to
	 * the passed in JFrame.
	 * @param mainFrame
	 */
	public void callMenu( JFrame mainFrame )
	{
		mainFrame.addKeyListener( new functionListener() );
	}
	
	/**
	 * Inner KeyListener
	 * class for the mainFrame
	 * @author Shaun
	 */
	class functionListener implements KeyListener
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			//Check if the F1 key was pressed
			if ( e.getKeyCode() == 112 ) { setVisible(true); }
		}
		
		@Override
		public void keyReleased(KeyEvent e) {}
		
		@Override
		public void keyTyped(KeyEvent e) {}
	}
}
