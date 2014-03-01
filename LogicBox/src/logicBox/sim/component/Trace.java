


package logicBox.sim.component;



/**
 * Traces connect components together via pins.
 * @author Lee Coakley
 * @see    Pin
 */
public class Trace extends ComponentPassive
{
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
