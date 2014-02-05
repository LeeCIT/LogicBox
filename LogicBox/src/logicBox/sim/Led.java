


package logicBox.sim;
import java.util.List;



/**
 * LED light.  Shows state of input pin.
 * @author Lee Coakley
 */
public class Led extends Component implements PinIn
{
	protected Pin pinInput;
	
	
	
	public Led() {
		super();
		this.pinInput = new Pin( this, IoMode.input );
	}
	
	
	
	public boolean getState() {
		return pinInput.getState();
	}
	
	
	
	public Pin getPinInput() {
		return pinInput;
	}
	
	
	
	public List<Pin> getPinInputs() {
		return Util.wrapInList( pinInput );
	}
}
