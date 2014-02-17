


package logicBox.sim;



/**
 * Gate with user-configurable input pin count.  TODO
 * @author Lee Coakley
 */
public abstract class GateConfigurableInputs extends Gate
{
	public GateConfigurableInputs() {
		super();
	}
	
	
	
	public GateConfigurableInputs( int inputPinCount ) {
		super( inputPinCount );
	}
	
	
	
	public boolean hasConfigurableInputPinCount() {
		return true;
	}
}
