


package logicBox.sim.component;



/**
 * Traces connect components together via pins.
 * Traces are dumb: they have no ownership and their state is a simple boolean. 
 * @author Lee Coakley
 * @see    Pin
 */
public class Trace extends ComponentPassive
{
	private static final long serialVersionUID = 1L;
	
	private Pin source, dest;
	
	
	
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
		return (pin == dest) ? source : dest;
	}



	public String getName() {
		return "Trace";
	}
}
