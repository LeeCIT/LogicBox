


package logicBox.sim.component;



/**
 * A circuit component.  Gates, pins, traces, LEDs, etc.
 * @author Lee Coakley
 */
public abstract class Component
{
	public long simStep; // Simulation step the component was last touched on
	
	
	
	/**
	 * Get the human-readable name of the component.
	 */
	public abstract String getName();
	
	
	
	/**
	 * Reset the component to its initial state, as if the simulation was never run.
	 */
	public abstract void reset();
	
	
	
	public String toString() {
		return getName();
	}
}
