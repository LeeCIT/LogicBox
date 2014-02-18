


package logicBox.sim.component;



/**
 * Implemented by passive classes which have a logic level.
 * Traces, Pins, and Junctions.
 * @author Lee Coakley
 */
public interface Stateful
{
	public boolean getState();
	public void    setState( boolean state );
}
