package logicBox.gui;

import helpMenuPrototype.*;
import javax.swing.JFrame;

public class HelpMenuFrameTest extends JFrame
{
	private HelpMenu helpMenu = new HelpMenu();
	
	public HelpMenuFrameTest() 
	{
		setSize(300, 300);
		setVisible(true);
		
		
	}
	
	public void getHelpMenu()
	{
		helpMenu.callMenu();
	}
}
