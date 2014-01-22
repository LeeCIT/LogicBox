


package logicBox.sim;



/**
 * A fixed logic-level source.
 * @author Lee Coakley
 */
public class SourceFixed extends Component
{
	protected Pin pinOut;
	
	
	
	public SourceFixed( boolean state ) {
		pinOut = new Pin( this );
		setState( state );
	}
	
	
	
	public void setState( boolean state ) {
		pinOut.setState( state );
	}
}
