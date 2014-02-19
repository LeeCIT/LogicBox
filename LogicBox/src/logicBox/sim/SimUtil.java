


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
	
	
	
	/**
	 * Interpret a list of pins as an integer based on their states.
	 * The size of the list must be less than 32.
	 * The LSB is the pin with the lowest index, the MSB the highest.
	 */
	public static int decodeToInt( List<Pin> pins ) {
		int value = 0;
		
		for (int i=0; i<pins.size(); i++)
			if (pins.get(i).getState())
				value |= (1 << i);
		
		return value;
	}
}
