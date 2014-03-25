


package logicBox.gui.help;



/**
 * Describes a sim component's help information.
 * @author Shaun O'Donovan
 */
public class ComponentHelpInfo 
{
	private String compName;
	private String compDescription;
	
	
	
	public ComponentHelpInfo( String compName, String compDes ) {
		this.compDescription = compDes;
		this.compName        = compName;
	}
	
	
	
	public String getCompName() {
		return compName;
	}
	
	
	
	public String getCompDescription() {
		return compDescription;
	}
}
