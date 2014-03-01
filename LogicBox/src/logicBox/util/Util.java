


package logicBox.util;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;



/**
 * Generic utility functions.
 */
public abstract class Util
{
	public static <T> Set<T> createIdentityHashSet() {
		return Collections.newSetFromMap( new IdentityHashMap<T,Boolean>() );
	}
}