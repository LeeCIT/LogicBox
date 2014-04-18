


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
	 * Called when the user clicks on this component.
	 * At the moment only toggles and oscillators need this.
	 * return: did sim change state
	 */
	public boolean interactClick() {
		return false; // Do nothing; most classes don't need this
	}
	
	
	/**
	 * Reset the component to its initial state, as if the simulation was never run.
	 */
	public abstract void reset();
	
	
	
	/**
	 * Disconnect from all logically connected components.
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
