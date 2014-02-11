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
		mainFrame.addKeyListener( new functionListener() ); //Set keylistener to the main frame.
		mainFrame.setFocusable(true); //Put the main frame into focus.
	}
	
	
}
