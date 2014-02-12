


package logicBox.sim;
import java.util.ArrayList;
import java.util.List;



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
}
