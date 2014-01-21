


package logicBox.sim;
import java.util.ArrayList;



/**
 * A gate which outputs true if all its inputs are true.
 * @author Lee Coakley
 */
public class GateAnd extends Gate
{
	public boolean update() {
		for (Pin pin: pinInputs)
			if (pin == null || !pin.evaluate())
				return false;
			
		return true;
	}
}
