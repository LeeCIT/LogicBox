


package logicBox.sim;



/**
 * Gate with user-configurable input pin count.  TODO
 * @author Lee Coakley
 */
public abstract class GateVariableInputs extends Gate
{
	public GateVariableInputs() {
		super();
	}
	
	
	
	public GateVariableInputs( int inputPinCount ) {
		super( inputPinCount );
	}
	
	
	
	public boolean hasVariableInputPinCount() {
		return true;
	}
}
