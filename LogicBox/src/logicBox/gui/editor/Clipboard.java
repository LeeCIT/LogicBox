


package logicBox.gui.editor;

import java.util.Set;
import logicBox.gui.editor.tools.Selection;
import logicBox.sim.Simulation;
import logicBox.sim.component.Component;
import logicBox.util.Util;



/**
 * The global clipboard.
 * @author Lee Coakley
 */
public abstract class Clipboard
{
	private static Selection clipboardSelection;
	
	
	
	/**
	 * Set the current clipboard selection.
	 * There is no need to modify it in any way, this is all done internally.
	 */
	public static void set( Selection sel ) {
		if (sel.isEmpty()) {
			clear();
			return;
		}
		
		Selection      copy = Util.deepCopy( sel );
		Set<Component> coms = Util.createIdentityHashSet();
		Simulation     tsim = new Simulation();
		
		for (EditorComponent ecom: sel.ecoms) {
			ecom.linkToWorld( null );
			coms.add( ecom.getComponent() );
			tsim.add( ecom.getComponent() );
		}
		
		tsim.disconnectAllNotIn( coms );
		
		clipboardSelection = copy;
	}
	
	
	
	/**
	 * Get a copy of the clipboard selection.
	 */
	public static Selection get() {
		if (isEmpty())
			throw new RuntimeException( "Clipboard is empty!" );
		
		return Util.deepCopy( clipboardSelection );
	}
	
	
	
	public static void clear() {
		clipboardSelection = null;
	}
	
	
	
	public static boolean isEmpty() {
		return clipboardSelection == null;
	}
}
