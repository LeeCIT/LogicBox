


package logicBox.util;

import java.util.Collection;
import java.util.HashSet;



/**
 * A composite set of callbacks.  It's handy for an event listener system.
 * To execute all the callbacks call execute().
 * @author Lee Coakley
 */
public class CallbackSet extends HashSet<Callback> implements Callback
{
	private static final long serialVersionUID = 1L;
	
	
	
	public CallbackSet() {
		super();
	}
	
	
	
	public CallbackSet( Collection<? extends Callback> c ) {
		super( c );
	}
	
	
	
	/**
	 * Execute all callbacks in the set.
	 */
	public void execute() {
		for (Callback cb: this)
			cb.execute();
	}
	
}
