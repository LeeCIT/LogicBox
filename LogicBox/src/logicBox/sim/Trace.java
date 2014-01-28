


package logicBox.sim;



/**
 * Traces connect components together via pins.
 * @author Lee Coakley
 * @see    Pin
 */
public class Trace extends Component
{
	protected Pin source, dest;
	
	
	
	public Trace() {
		// Do nothing
	}
	
	
	
	public Trace( Pin source, Pin dest ) {
		this();
		this.source = source;
		this.dest   = dest;
	}
	
	
	
	public Pin getPinSource() {
		return source;
	}
	
	
	
	public Pin getPinDest() {
		return dest;
	}
}
