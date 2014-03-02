


package logicBox.sim;
import java.util.Iterator;
import java.util.Set;
import logicBox.sim.component.Component;
import logicBox.sim.component.ComponentActive;
import logicBox.sim.component.ComponentPassive;
import logicBox.sim.component.Junction;
import logicBox.sim.component.Pin;
import logicBox.sim.component.Stateful;
import logicBox.sim.component.Trace;
import logicBox.sim.component.Updateable;
import logicBox.util.Util;



/**
 * Abstracts connective circuitry into a single object.
 * Can be used to treat interconnects and pins as a single entity.
 * Also useful as an informational structure.
 * @author Lee Coakley
 */
public class Net implements Stateful, Updateable, Iterable<ComponentPassive>
{
	public Set<ComponentPassive> all;        // Everything in the net
	public Set<ComponentPassive> writeables; // Everything except pins leading into the net  
	public Set<Junction>         junctions;  // 
	public Set<Trace>            traces;     // 
	public Set<Pin>              pins;       // All pins, except virtual Junction pins
	public Set<Pin>              pinInputs;  // Input pins (to components)
	public Set<Pin>              pinOutputs; // Output pins (from components)
	
	
	
	/**
	 * Find the connectivity network of the pin.
	 * @param pin
	 */
	public Net( Pin pin ) {
		all        = Util.createIdentityHashSet();
		writeables = Util.createIdentityHashSet();
		junctions  = Util.createIdentityHashSet();
		traces     = Util.createIdentityHashSet();
		pins       = Util.createIdentityHashSet();
		pinInputs  = Util.createIdentityHashSet();
		pinOutputs = Util.createIdentityHashSet();
		
		accumulateFromPin( pin, true );
	}
	
	
	
	public boolean equals( Object other ) {
		if (other == this) return true;
		if (other == null) return false;
		
		if (other.getClass() != this.getClass())
			return false;
		
		return all.equals( ((Net)other).all );
	}
	
	
	
	public int hashCode() {
		return all.hashCode();
	}
	
	
	
	public boolean isEmpty() {
		return all.isEmpty();
	}
	
	
	
	public boolean contains( ComponentPassive com ) {
		return all.contains( com );
	}
	
	
	
	/**
	 * Find all the active components connected via their input pins from this net (outward edge)
	 */
	public Set<ComponentActive> getFanout() {
		Set<ComponentActive> set = Util.createIdentityHashSet();
		
		for (Pin pin: pinInputs)
			set.add( (ComponentActive) pin.getAttachedComponent() );
		
		return set;
	}
	
	
	
	/**
	 * Find all the active components connected via their outputs pins to this net (inward edge)
	 */
	public Set<ComponentActive> getFanin() {
		Set<ComponentActive> set = Util.createIdentityHashSet();
		
		for (Pin pin: pinOutputs)
			set.add( (ComponentActive) pin.getAttachedComponent() );
		
		return set;
	}
	
	
	
	public void update() {
		setState( getState() );
	}
	
	
	
	/**
	 * Get the net's logic level, as defined by the inputs coming into it.
	 */
	public boolean getState() {
		boolean state = false;
		
		for (Stateful s: pinOutputs)
			state |= s.getState();
		
		return state;
	}
	
	
	
	/**
	 * Set the state of the net's outputs.  (No effect on output pins leading into the net)
	 */
	public void setState( boolean state ) {
		for (Stateful s: writeables)
			s.setState( state );
	}
	
	
	
	/**
	 * OR the state of the net's outputs.  (No effect on output pins leading into the net)
	 */
	public void orState( boolean state ) {
		for (Stateful s: writeables)
			s.orState( state );
	}
	
	
	
	public Iterator<ComponentPassive> iterator() {
		return all.iterator();
	}
	
	
	
	private void add( Junction junc ) {
		all       .add( junc );
		junctions .add( junc );
		writeables.add( junc );
	}
	
	
	
	private void add( Trace trace ) {
		all       .add( trace );
		traces    .add( trace );
		writeables.add( trace );
	}
	
	
	
	private void add( Pin pin ) {
		all .add( pin );
		pins.add( pin );
		
		if (pin.isInput()) { 
			pinInputs .add( pin );
			writeables.add( pin );
		} else if (pin.isOutput()) {
			pinOutputs.add( pin );
		}
	}
	
	
	
	private void accumulateFromPin( Pin pin, boolean includeSourcePin ) {
		if (includeSourcePin)
			add( pin );
		
		if ( ! pin.hasTrace())
			return;
		
		Trace     trace    = pin.getTrace();
		Pin       otherPin = trace.getPinOtherSide( pin );
		Component attached = otherPin.getAttachedComponent();
		boolean   isJunc   = attached instanceof Junction;
		
		add( trace );
		
		if ( ! isJunc)
			add( otherPin );
		
		if (isJunc)
			accumulateFromJunction( (Junction) attached, otherPin );
	}
	
	
	
	private void accumulateFromJunction( Junction junc, Pin originatingPin ) {
		if (junctions.contains(junc)) // Prevent infinite recursion in junction loops
			return;
		
		add( junc );
		
		for (Pin pin: junc.getPinsExcept( originatingPin ))
			accumulateFromPin( pin, false );
	}
	
	
	
	public String toString() {
		String str = "Net [";
		
		for (Component com: this)
			str += "\n  " + com;
		
		return str + "\n]";
	}
}

















