


package logicBox.sim.component;



/**
 * Gate which outputs true if any of its inputs are true.
 * 0,0 -> 0
 * 0,1 -> 1
 * 1,0 -> 1
 * 1,1 -> 1
 * @author Lee Coakley
 */
public class GateOr extends Gate
{
	public GateOr() {
		super( 2 );
	}
	
	
	
	public GateOr( int inputPinCount ) {
		super( inputPinCount );
	}
	
	
	
	public boolean evaluate() {
		boolean state = false;
		
		for (Pin pin: getPinInputs())
			state |= pin.getState();
		
		return state;
	}
	
	
	
	public String getName() {
		return "OR gate";
	}
}
