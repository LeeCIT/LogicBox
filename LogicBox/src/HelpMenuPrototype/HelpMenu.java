package HelpMenuPrototype;

import java.awt.Dimension;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;
import logicBox.sim.Component;

public class HelpMenu extends JFrame
{
	private int gate;
	private JTabbedPane gateTabs;
	private JPanel andGate;
	private JPanel orGate;
	
	public HelpMenu() 
	{
		setSize(300,300);
		
		setLayout( new MigLayout() );
		
		addToContentPane();
		setComponentDimensions();
		
	}
	
	/**
	 * Call a specific tab
	 * within the help menu.
	 * @param gate
	 */
	public void callGateInfo( int gate )
	{
		this.gate = gate;
		setSelectedTab();
	}
	
	
	/**
	 * Set the specified tab
	 * as the one to be viewed.
	 */
	private void setSelectedTab()
	{
		switch (gate)
		{
			case 0:
			{
				gateTabs.setSelectedIndex(gate);
				break;
			}
			case 1:
			{
				gateTabs.setSelectedIndex(gate);
				break;
			}
			case 2:
			{
				gateTabs.setSelectedIndex(gate);
				break;
			}
			default:
			{
				gateTabs.setSelectedIndex(0);
				break;
			}
		}
	}

	/**
	 * Call the help menu.
	 */
	public void callMenu()
	{
		setVisible(true);
	}
	
	
	/**
	 * Add each component
	 * to the content pane.
	 */
	private void addToContentPane()
	{
		getContentPane().add(gateTabs  = new JTabbedPane());
		gateTabs.add("& Gate", andGate = new JPanel());
		gateTabs.add("| Gate", orGate  = new JPanel());
	}
	
	/**
	 * Set the dimensions
	 * of each component.
	 */
	protected void setComponentDimensions()
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
