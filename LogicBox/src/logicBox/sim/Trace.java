


package logicBox.sim;



/**
 * Traces connect components together via pins.
 * TODO: Traces have a directionality.  Needs to be taken into account.
 * TODO: Add solder attachment
 * TODO: Do solder joins need to be ordered?  Probably.
 * @author Lee Coakley
 * @see    Pin
 */
public class Trace extends Component
{
	protected Pin a, b;
	
	
	
	public Trace() {
		// Do nothing
	}
	
	
	
	public Trace( Pin a, Pin b ) {
		this.a = a;
		this.b = b;
	}
	
	
	
	/**
	 * Given one of the two connected pins, get the one at the opposite end.
	 */
	public Pin getOpposite( Pin c ) {
		return (c==a) ? b : a;
	}
}
