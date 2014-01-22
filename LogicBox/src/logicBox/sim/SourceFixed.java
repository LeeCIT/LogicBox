


package logicBox.sim;
import java.util.List;



/**
 * A fixed logic-level source.
 * @author Lee Coakley
 */
public class SourceFixed extends Component implements PinOut
{
	protected Pin pinOut;
	
	
	
	public SourceFixed( boolean state ) {
		pinOut = new Pin( this, false );
		setState( state );
	}
	
	
	
	public void setState( boolean state ) {
		pinOut.setState( state );
	}
	
	
	
	public List<Pin> getPinOutputs() {
		return Util.wrapInList( pinOut );
	}
}
