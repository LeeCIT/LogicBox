


package logicBox.sim;



/**
 * Traces connect components together via pins.
 * @author Lee Coakley
 * @see    Pin
 */
public class Trace extends Component
{
	protected Pin     source, dest;
	protected boolean state;
	
	
	
	public Trace() {
		super();
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
	
	
	
	public Pin getPinOtherSide( Pin pin ) {
		if (pin != source && pin != dest)
			throw new RuntimeException( "Pin must be the source or destination of the trace." );
		
		return (pin == dest) ? source : dest;
	}
	
	
	
	public boolean getState() {
		return state;
	}



	public void setState( boolean state ) {
		this.state = state;
	}
}
