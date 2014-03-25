package HelpMenuPrototype;



public class ComponentInfo 
{
	
	
	
	private String compName;
	private String compDescription;
	
	
	
	public ComponentInfo( String compName, String compDes ) {
		this.compDescription = compDes;
		this.compName = compName;
	}
	
	
	
	public String getCompName() {
		return compName;
	}
	
	
	
	public String getCompDescription() {
		return compDescription;
	}
}
