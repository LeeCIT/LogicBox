


package logicBox.sim;
import java.util.ArrayList;
import java.util.List;
import logicBox.sim.component.Component;



/**
 * Utility methods for the simulator.
 * @author Lee Coakley
 */
public class SimUtil
{
	public static <T> List<T> wrapInList( T item ) {
		List<T> list = new ArrayList<T>();
		list.add( item );
		return list;
	}
	
	
	
	public static void addPins( List<Pin> list, Component com, PinIoMode mode, int count ) {
		for (int i=0; i<count; i++)
			list.add( new Pin(com,mode) );
	}
}
