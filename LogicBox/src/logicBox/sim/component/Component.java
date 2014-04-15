


package logicBox.sim.component;

import java.io.Serializable;



/**
 * A circuit component.  Gates, pins, traces, LEDs, etc.
 * @author Lee Coakley
 */
public abstract class Component implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	
	
	/**
	 * Reset the component to its initial state, as if the simulation was never run.
	 */
	public abstract void reset();
	
	
	
	/**
	 * Detach from all connected components.
	 */
	public abstract void disconnect();
	
	
	
	/**
	 * Get the enum type of the component.
	 */
	public abstract ComponentType getType();
	
	
	
	/**
	 * Get the human-readable name of the component.
	 */
	public abstract String getName();
	
	
	
	public String toString() {
		return getName();
	}
}
