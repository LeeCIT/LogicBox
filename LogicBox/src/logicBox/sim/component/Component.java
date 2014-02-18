


package logicBox.sim.component;



/**
 * A circuit component.  Gates, pins, traces, LEDs, etc.
 * @author Lee Coakley
 */
public abstract class Component
{
	protected long simStep; // Simulation step the component was last touched on
	
	
	
	public abstract String getName();
}
