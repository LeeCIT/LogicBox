


package logicBox.sim;



/**
 * A fixed logic-level source.
 * @author Lee Coakley
 */
public class SourceFixed extends Source
{
	public SourceFixed( boolean state ) {
		super( state );
		setState( state );
	}
	
	
	
	public void update() {
		// Do nothing
	}
}
