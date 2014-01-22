


package logicBox.sim;



/**
 * LED light.  Shows output state.
 * @author Lee Coakley
 */
public class Led
{
	protected Pin pinInput;
	
	
	
	public boolean getState() {
		return pinInput.getState();
	}
	
	
	
	public Pin getPinInput() {
		return pinInput;
	}
}
