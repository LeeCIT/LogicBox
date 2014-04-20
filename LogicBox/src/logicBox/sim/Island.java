


package logicBox.sim;

import java.util.Iterator;
import java.util.Set;
import logicBox.sim.component.*;
import logicBox.util.Util;



/**
 * A group of components which are logically isolated.
 * An island can be simulated in a separate thread, for example.
 * @author Lee Coakley
 */
public class Island implements Iterable<ComponentActive>
{
	private Set<ComponentActive> set;
	
	
	
	public Island() {
		set = Util.createIdentityHashSet();
	}
	
	
	
	public void add( ComponentActive com ) {
		set.add( com );
	}
	
	
	
	public boolean contains( ComponentActive com ) {
		return set.contains( com );
	}
	
	
	
	public Iterator<ComponentActive> iterator() {
		return set.iterator();
	}
}
