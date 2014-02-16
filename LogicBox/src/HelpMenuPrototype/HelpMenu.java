


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
	private JTextArea compDescription;
	
	
	
	public HelpMenu() {
		
	}
	
	
	
	public HelpMenu( Map<ComponentType, String> compMap ) {
		super();
		this.componentMap = compMap;
		compDescription = new JTextArea();
		add( compDescription );
		setSize(300,300);
		compDescription.setPreferredSize(getSize());
		compDescription.setLineWrap(true);
		compDescription.setEditable(false);
		displayDescription();
	}
	
	
	
	/**
	 * Draw the help information onto the panel.
	 */
	public void paintComponent( Graphics g ) {	
		//super.paintComponent( g );
		//StringBuilder sb = new StringBuilder(getDescription());
		//g.drawString(getDescription(), 10, 15);
		
	}
	
	
	
	/**
	 * Display the description of the specified component.
	 * @param compDes
	 */
	private void displayDescription(){
		compDescription.setText(getDescription());
	}
	
	
	
	/**
	 * Return the help menu description for the component
	 * passed in.
	 * @param componentType
	 * @return
	 */
	private String getDescription(){
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
		//repaint();
		displayDescription();
	}
	
	
	
}
