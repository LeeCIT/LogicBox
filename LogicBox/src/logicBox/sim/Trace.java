


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
