


package logicBox.sim.component.connective;

import java.util.Set;
import logicBox.sim.component.Component;
import logicBox.sim.component.ComponentPassive;
import logicBox.sim.component.ComponentType;
import logicBox.util.Util;



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
	
	
	
	public Trace( Pin source, Pin dest ) {
		this();
		this.source = source;
		this.dest   = dest;
		
		if (source != null) source.connectTrace( this );
		if (dest   != null) dest  .connectTrace( this );
	}
	
	
	
	public boolean getState() {
		return pinPowersMe(source) || pinPowersMe(dest);
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
	
	
	
	public Set<Component> getConnectedComponents() {
		Set<Component> set = Util.createIdentityHashSet();
		
		if (source != null) set.add( source.getAttachedComponent() );
		if (dest   != null) set.add( dest  .getAttachedComponent() );
		
		return set;
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.trace;
	}



	public String getName() {
		return "Trace";
	}
	
	
	
	private boolean pinPowersMe( Pin pin ) {
		if (pin == null)
			return false;
		
		boolean isBiOrOut = pin.isOutput() || pin.isBidirectional();
		
		return isBiOrOut && pin.getState();
	}
}
