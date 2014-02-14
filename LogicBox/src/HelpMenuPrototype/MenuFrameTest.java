package HelpMenuPrototype;

import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;
import logicBox.sim.ComponentType;

public class MenuFrameTest extends JFrame 
{
	private HelpMenu menu;
	private ComponentType gateNot;
	
	public MenuFrameTest() 
	{
		setSize(300,300);
		setVisible(true);
		setLayout( new MigLayout() );
		
		getContentPane().add(menu = new HelpMenu(gateNot));
	}
	
	public static void main(String[] args) 
	{
		MenuFrameTest frame = new MenuFrameTest();
	}
}
