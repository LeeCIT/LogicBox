


package logicBox.sim.component;



/**
 * A user-switchable logic-level source.
 * @author Lee Coakley
 */
public class SourceToggle extends SourceFixed
{
	public SourceToggle( boolean state ) {
		super( state );
	}
	
	
	
	public void toggleState() {
		pinOut.setState( ! pinOut.getState() );
	}
	
	
	
	public String getName() {
		return "Switchable source";
	}
}
