


package logicBox.sim;



/**
 * A gate which outputs true if all its inputs are true.
 * @author Lee Coakley
 */
public class GateAnd extends Gate
{
	public boolean eval() {
		boolean state = true;
		
		for (Pin pin: pinInputs)
			if (pin != null)
				state &= pin.getState();
			
		return state;
	}
}
