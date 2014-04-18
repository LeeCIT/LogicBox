


package logicBox.sim.component;



/**
 * A user-switchable logic-level source.
 * @author Lee Coakley
 */
public class SourceToggle extends SourceFixed
{
	private static final long serialVersionUID = 1L;
	
	
	
	public SourceToggle( boolean state ) {
		super( state );
	}
	
	
	
	public boolean interactClick() {
		toggleState();
		return true;
	}
	
	
	
	public void toggleState() {
		setState( ! getState() );
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.sourceToggle;
	}
	
	
	
	public String getName() {
		return "Switchable source";
	}
}
