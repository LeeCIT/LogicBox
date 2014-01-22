


package logicBox.sim;
import java.util.ArrayList;



/**
 * Traces connect components together via pins.
 * @author Lee Coakley
 * @see    Pin
 */
public class Trace extends Component
{
	protected Pin a, b;
	
	
	
	/**
	 * Get the external components at the other end of the connected trace.
	 * @return A list of terminating components.  Empty if none are connected.
	 */
	public ArrayList<Pin> getTerminatingPins( Pin from ) {
		return null; // TODO
	}
	
	
	
	/**
	 * Given one of the two connected pins, get the one at the opposite end.
	 */
	public Pin getOpposite( Pin c ) {
		return (c==a) ? b : a;
	}
}
