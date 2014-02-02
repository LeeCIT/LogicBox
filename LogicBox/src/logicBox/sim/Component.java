


package logicBox.sim;
import logicBox.util.Vec2;



/**
 * A circuit component.  Gates, pins, traces, LEDs, etc.
 * @author Lee Coakley
 */
public abstract class Component
{
	public Vec2   pos;     // Position of the component for drawing.
	public double angle;   // Angle of rotation for drawing.
	
	protected long simStep; // Simulation step the component was last updated on.
}
