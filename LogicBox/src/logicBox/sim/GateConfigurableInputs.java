


package logicBox.sim;



/**
 * Gate with user-configurable input pin count.
 * TODO other components than gates need configurable inputs.  Mux/demux etc
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
