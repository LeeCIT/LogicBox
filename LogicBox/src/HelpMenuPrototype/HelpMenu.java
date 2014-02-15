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
	
	
	
	public void paintComponent( Graphics g )
	{	
		super.paintComponent( g );
		g.setColor(Color.black);
		g.drawString(getDescription(componentType), 64, 64);	
	}
	
	
	
	
	private String getDescription( ComponentType componentType )
	{
		if ( ! componentMap.containsKey(componentType) ){
			return "Missing info";
		}
		
		return componentMap.get(componentType);
	}
	
	
	
	
	public void setDisplayedInfo(ComponentType compType) {
		this.componentType = compType;
		repaint();
	}
	
	
	
}
