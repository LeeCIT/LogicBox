


package logicBox.sim;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import logicBox.sim.component.Component;
import logicBox.sim.component.PinIoMode;
import logicBox.sim.component.connective.Pin;



/**
 * Utility methods for the simulator.
 * @author Lee Coakley
 */
public abstract class SimUtil
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
	 * There must be <= 32 pins or the result will be truncated.
	 * The LSB is the pin with the lowest index, the MSB the highest.
	 */
	public static int decodePinsToInt( List<Pin> pins ) {
		int value = 0;
		int maxEx = Math.min( pins.size(), 32 );
		
		for (int i=0; i<maxEx; i++)
			if (pins.get(i).getState())
				value |= (1 << i);
		
		return value;
	}
	
	
	
	/**
	 * Same as SimUtil::decodePinsToInt except the pins are decoded in reverse order.
	 */
	public static int decodePinsToIntReverse( List<Pin> pins ) {
		List<Pin> pinsReverse = new ArrayList<>( pins );
		Collections.reverse( pinsReverse );
		return decodePinsToInt( pinsReverse );
	}
	
	
	
	/**
	 * Encode an integer onto a set of pins.
	 * There must be <= 32 pins, or the result will be truncated.
	 * The LSB is assigned to pins[0].
	 */
	public static void encodeIntToPins( int x, List<Pin> pins ) {
		int maxEx = Math.min( pins.size(), 32 );
		
		for (int i=0; i<maxEx; i++) {
			boolean state = ((x>>i) & 1) == 1;
			pins.get(i).setState( state );
		}
	}
}









