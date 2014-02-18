


package logicBox.sim.component;

import logicBox.sim.Pin;



/**
 * A gate which outputs true if all its inputs are true.
 * 0,0 -> 0
 * 0,1 -> 0
 * 1,0 -> 0
 * 1,1 -> 1
 * @author Lee Coakley
 */
public class GateAnd extends GateConfigurableInputs
{
	public GateAnd() {
		super( 2 );
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
	
	
	
	public String getName() {
		return "AND gate";
	}
}
