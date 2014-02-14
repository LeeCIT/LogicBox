


package logicBox.sim;



/**
 * A fixed logic-level source.
 * @author Lee Coakley
 */
public class SourceFixed extends Source implements Stateful
{
	public SourceFixed( boolean state ) {
		super( state );
		setState( state );
	}
	
	
	
	public void update() {
		// Do nothing
	}
	
	
	
	public String getName() {
		return "Fixed source (" + (getState()?"1":"0") + ")";
	}
}
