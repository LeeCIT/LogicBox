package helpMenuPrototype;

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
	public void callMenu()
	{
		setVisible(true);
	}
	
	
}
