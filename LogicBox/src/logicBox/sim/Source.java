


package logicBox.sim;
import java.util.List;



/**
 * A logic-level source.
 * @author Lee Coakley
 */
public abstract class Source extends Component implements PinOut, Updateable
{
	protected Pin pinOut;
	
	
	
	public Source( boolean state ) {
		super();
		pinOut = new Pin( this, IoMode.output );
		setState( state );
	}
	
	
	
	public void setState( boolean state ) {
		pinOut.setState( state );
	}
	
	
	
	public List<Pin> getPinOutputs() {
		return Util.wrapInList( pinOut );
	}
}
