package HelpMenuPrototype;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import logicBox.sim.ComponentType;

public class HelpMenu extends JPanel
{
	private ComponentType componentType;
	
	public HelpMenu( ComponentType componentType ) 
	{
		this.componentType = componentType;
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
		switch (componentType)
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
			case sourceFixed:
			{
				System.out.println("Source fixed info");
				break;
			}
			case sourceToggle:
			{
				System.out.println("Source toggle info");
				break;
			}
			case sourceOscillator:
			{
				System.out.println("Source Oscillator info");
				break;
			}
			case junction:
			{
				System.out.println("Junction info");
				break;
			}
			case trace:
			{
				System.out.println("Trace info");
				break;
			}
			case displayLed:
			{
				System.out.println("LED info");
				break;
			}
			case displaySevenSeg:
			{
				System.out.println("Seven Seg info");
				break;
			}
			case mux:
			{
				System.out.println("Mux info");
				break;
			}
			case demux:
			{
				System.out.println("Demux info");
				break;
			}
			case flipFlopD:
			{
				System.out.println("D Flip Flop info");
				break;
			}
			case blackBox:
			{
				System.out.println("Black Box info");
				break;
			}
			case blackBoxPin:
			{
				System.out.println("Black Box Pin info");
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
