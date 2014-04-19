


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
	
	protected Pin source, dest; // These names are arbitrary!  Assume nothing.
	
	
	
	public Trace() {
		super();
	}
	
	
	
	public boolean getState() {
		boolean state = false;
		
		if (source != null && source.isOutput()) state |= source.getState();
		if (dest   != null && dest  .isOutput()) state |= dest  .getState();
			
		return state;
	}
	
	
	
	public Trace( Pin source, Pin dest ) {
		this();
		this.source = source;
		this.dest   = dest;
	}
	
	
	
	public boolean isSourceConnected() {
		return (source != null);
	}
	
	
	
	public boolean isDestConnected() {
		return (dest != null);
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
	
	
	
	public boolean hasPinOtherSide( Pin pin ) {
		return null != getPinOtherSide( pin );
	}
	
	
	
	/**
	 * The main means of disconnection for traces is done through pins.
	 * Traces are dumb objects.
	 */
	public void disconnect() {
		if (source != null) source.disconnect();
		if (dest   != null) dest  .disconnect();
		
		source = null;
		dest   = null;
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.trace;
	}



	public String getName() {
		return "Trace";
	}
}
