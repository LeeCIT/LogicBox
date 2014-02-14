package HelpMenuPrototype;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import logicBox.sim.ComponentType;

public class HelpMenu extends JPanel
{
	private ComponentType gateType;
	
	public HelpMenu( ComponentType gateType ) 
	{
		this.gateType = gateType;
		setSize(300,300);
		setLayout( new MigLayout() );
		callMenu();
		displayGateInfo();
	}
	
	
	/**
	 * Display help information
	 * based on the specified gate.
	 */
	private void displayGateInfo()
	{
		switch (gateType)
		{
			case gateRelay:
			{
				System.out.println("Relay info");
				break;
			}
			case gateNot:
			{
				System.out.println("Not gate info");
				break;
			}
			case gateAnd:
			{
				System.out.println("And gate info");
				break;
			}
			case gateNand:
			{
				System.out.println("Nand gate info");
				break;
			}
			case gateOr:
			{
				System.out.println("Or gate info");
				break;
			}
			case gateNor:
			{
				System.out.println("Nor gate info");
				break;
			}
			case gateXnor:
			{
				System.out.println("Xnor gate info");
				break;
			}
			default:
			{
				
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
	
	
}
