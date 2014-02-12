package HelpMenuPrototype;

import javax.swing.*;

import logicBox.sim.Component;

public class HelpMenu extends JFrame
{
	private static Component gate;
	
	public HelpMenu() 
	{
		setSize(300,300);
	}
	
	/**
	 * Call a specific tab
	 * within the help menu.
	 * @param gate
	 */
	public void callGateInfo( Component gate )
	{
		this.gate = gate;
	}
	

	/**
	 * Call the help menu.
	 */
	public void callMenu()
	{
		setVisible(true);
	}
	
	public static void main(String[] args) 
	{
		HelpMenu menu = new HelpMenu();
		menu.callMenu();
		
	}
}
