


package logicBox.sim;



/**
 * Implemented by classes that have a logic level.  Traces, Pins, and Junctions.
 * @author Lee Coakley
 */
public interface Stateful
{
	public boolean getState();
	public void    setState( boolean state );
}
