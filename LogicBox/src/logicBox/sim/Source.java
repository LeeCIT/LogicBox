


package logicBox.sim;
import java.util.ArrayList;
import java.util.List;



/**
 * A logic-level source.
 * @author Lee Coakley
 */
public abstract class Source extends ComponentActive
{
	protected Pin pinOut;
	
	
	
	public Source( boolean state ) {
		super();
		pinOut = new Pin( this, PinIoMode.output );
		setState( state );
	}
	
	
	
	public void setState( boolean state ) {
		pinOut.setState( state );
	}
	
	
	
	public boolean getState() {
		return pinOut.getState();
	}
	
	
	
	public List<Pin> getPinInputs() {
		return new ArrayList<>();
	}
	
	
	
	public List<Pin> getPinOutputs() {
		return SimUtil.wrapInList( pinOut );
	}
}
