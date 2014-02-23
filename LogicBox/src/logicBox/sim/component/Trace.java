


package logicBox.sim.component;



/**
 * Traces connect components together via pins.
 * @author Lee Coakley
 * @see    Pin
 */
public class Trace extends ComponentPassive
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
		return (pin == dest) ? source : dest;
	}
	
	
	
	public boolean getState() {
		return state;
	}



	public void setState( boolean state ) {
		this.state = state;
	}



	public String getName() {
		return "Trace";
	}
}
