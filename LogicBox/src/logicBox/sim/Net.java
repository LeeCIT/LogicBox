


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
import logicBox.util.Util;



/**
 * Struct for modelling the connections of a component.
 * Can be used to treat interconnects and pins as a single entity.
 * @author Lee Coakley
 */
public class Net implements Stateful, Iterable<ComponentPassive>
{
	public Set<ComponentPassive> all;        // Everything in the net
	public Set<ComponentPassive> writeable;  // Everything except pins leading into the net  
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
		writeable  = Util.createIdentityHashSet();
		junctions  = Util.createIdentityHashSet();
		traces     = Util.createIdentityHashSet();
		pins       = Util.createIdentityHashSet();
		pinInputs  = Util.createIdentityHashSet();
		pinOutputs = Util.createIdentityHashSet();
		
		accumulateFromPin( pin, true );
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
	
	
	
	/**
	 * Get the net's logic level, as defined by the inputs into it.
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
		for (Stateful s: writeable)
			s.setState( state );
	}
	
	
	
	/**
	 * OR the state of the net's outputs.  (No effect on output pins leading into the net)
	 */
	public void orState( boolean state ) {
		for (Stateful s: writeable)
			s.orState( state );
	}
	
	
	
	public Iterator<ComponentPassive> iterator() {
		return all.iterator();
	}
	
	
	
	private void add( Junction junc ) {
		all      .add( junc );
		junctions.add( junc );
	}
	
	
	
	private void add( Trace trace ) {
		all   .add( trace );
		traces.add( trace );
	}
	
	
	
	private void add( Pin pin ) {
		all .add( pin );
		pins.add( pin );
		
		if      (pin.isInput ()) pinInputs .add( pin );
		else if (pin.isOutput()) pinOutputs.add( pin );
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
}

















