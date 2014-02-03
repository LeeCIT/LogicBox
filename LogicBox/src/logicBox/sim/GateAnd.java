


package logicBox.sim;



/**
 * A gate which outputs true if all its inputs are true.
 * @author Lee Coakley
 */
public class GateAnd extends Gate
{
	public GateAnd() {
		super();
		dsfhdfkhdfhdsfjk
	}
	
	
	
	public GateAnd( int inputPinCount ) {
		super( inputPinCount );
	}
	
	
	
	public void update() {
		boolean state = true;
		
		for (Pin pin: pinInputs)
			if (pin != null)
				state &= pin.getState();
			
		pinOut.setState( state );
	}
}
