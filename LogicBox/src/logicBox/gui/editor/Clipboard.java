


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
		
		Selection selCopy = Util.deepCopy( sel );
		
		for (EditorComponent com: selCopy.ecoms) {
			com.unlinkFromWorld();
			com.getComponent().reset();
		}
		
		isolateSelection( selCopy );
		clipboardSelection = selCopy;
	}
	
	
	
	private static void isolateSelection( Selection sel ) {
		Set<Component> coms = Util.createIdentityHashSet();
		
		for (EditorComponent ecom: sel)
			coms.add( ecom.getComponent() );
		
		Simulation.isolate( coms );
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
