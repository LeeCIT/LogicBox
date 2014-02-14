package HelpMenuPrototype;

import java.awt.Dimension;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;
import logicBox.sim.ComponentType;

public class HelpMenu extends JFrame
{
	private ComponentType gateType;
	private JTabbedPane gateTabs;
	private JPanel andGate;
	private JPanel orGate;
	
	public HelpMenu( ComponentType gateType ) 
	{
		this.gateType = gateType;
		setSize(300,300);
		
		setLayout( new MigLayout() );
		
		addToContentPane();
		setComponentDimensions();
		
	}
	
	/**
	 * Call a specific tab
	 * within the help menu.
	 * @param gateType
	 */
	public void callGateInfo( ComponentType gateType )
	{
		this.gateType = gateType;
		setSelectedTab();
	}
	
	
	/**
	 * Set the specified tab
	 * as the one to be viewed.
	 */
	private void setSelectedTab()
	{
		switch (gateType)
		{
			case gateRelay:
			{
				gateTabs.setSelectedIndex(0);
				break;
			}
			case gateNot:
			{
				gateTabs.setSelectedIndex(1);
				break;
			}
			case gateAnd:
			{
				gateTabs.setSelectedIndex(3);
				break;
			}
			case gateNand:
			{
				gateTabs.setSelectedIndex(4);
				break;
			}
			case gateOr:
			{
				gateTabs.setSelectedIndex(5);
				break;
			}
			case gateNor:
			{
				gateTabs.setSelectedIndex(6);
				break;
			}
			case gateXnor:
			{
				gateTabs.setSelectedIndex(7);
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
		
	}
}
