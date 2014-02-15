


package HelpMenuPrototype;



import java.awt.Color;
import java.awt.Graphics;
import java.util.Map;

import javax.swing.*;

import logicBox.sim.ComponentType;



public class HelpMenu extends JPanel
{
	
	
	
	private ComponentType componentType;
	private Map <ComponentType, String> componentMap;
	
	
	
	public HelpMenu() {
		
	}
	
	
	
	public HelpMenu( Map<ComponentType, String> compMap ) 
	{
		super();
		this.componentMap = compMap;
		setSize(300,300);
	}
	
	
	
	/**
	 * Draw the help information onto the panel.
	 */
	public void paintComponent( Graphics g )
	{	
		super.paintComponent( g );
		g.setColor(Color.black);
		g.drawString(getDescription(), 64, 64);	
	}
	
	
	
	/**
	 * Return the help menu description for the component
	 * passed in.
	 * @param componentType
	 * @return
	 */
	private String getDescription()
	{
		if ( ! componentMap.containsKey(componentType) ){
			return "Missing info";
		}
		
		return componentMap.get(componentType);
	}
	
	
	
	/**
	 * Change the help information currently displayed.
	 * @param compType
	 */
	public void setDisplayedInfo(ComponentType compType) {
		this.componentType = compType;
		repaint();
	}
	
	
	
}
