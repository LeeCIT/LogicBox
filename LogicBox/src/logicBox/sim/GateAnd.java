


package logicBox.sim;



/**
 * A gate which outputs true if all its inputs are true.
 * @author Lee Coakley
 */
public class GateAnd extends GateVariableInputs
{
	public GateAnd() {
		super();
	}
	
	
	
	public GateAnd( int inputPinCount ) {
		super( inputPinCount );
	}
	
	
	
	public boolean evaluate() {
		boolean state = true;
		
		for (Pin pin: pinInputs)
			state &= pin.getState();
			
		return state;
	}



	public void update() {
		pinOut.setState( evaluate() );
	}
}
