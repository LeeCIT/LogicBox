package HelpMenuPrototype;

import java.awt.Dimension;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;
import logicBox.sim.Component;

public class HelpMenu extends JFrame
{
	private static Component gate;
	private JTabbedPane gateTabs;
	private JPanel andGate;
	private JPanel orGate;
	
	public HelpMenu() 
	{
		setSize(300,300);
		
		setLayout( new MigLayout() );
		
		getContentPane().add(gateTabs  = new JTabbedPane());
		gateTabs.add("& Gate", andGate = new JPanel());
		gateTabs.add("| Gate", orGate  = new JPanel());
		
		setComponentDimensions();
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
	
	
	private void setComponentDimensions()
	{
		andGate.setPreferredSize(new Dimension(getSize()));
		orGate.setPreferredSize(new Dimension(getSize()));
	}
	
	public static void main(String[] args) 
	{
		HelpMenu menu = new HelpMenu();
		menu.callMenu();
		
	}
}
