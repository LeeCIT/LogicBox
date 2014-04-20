


package logicBox.gui.help;

import logicBox.sim.component.ComponentType;



/**
 * Describes a sim component's help information.
 * @author Shaun O'Donovan
 */
public class ComponentHelpInfo 
{
	private String compName;
	private String compDescription;
	private ComponentType compType;
	
	
	public ComponentHelpInfo( String compName, String compDes, ComponentType compType ) {
		this.compDescription = compDes;
		this.compName        = compName;
		this.compType 		 = compType;
	}
	
	
	
	public String getCompName() {
		return compName;
	}
	
	
	
	public String getCompDescription() {
		return compDescription;
	}
	
	
	public ComponentType getComponentType() {
		return compType;
	}
}
