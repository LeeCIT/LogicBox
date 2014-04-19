


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
	private static Selection selection;
	
	
	
	public static void set( Selection sel ) {
		Selection      copy = Util.deepCopy( sel );
		Set<Component> coms = Util.createIdentityHashSet();
		Simulation     tsim = new Simulation();
		
		for (EditorComponent ecom: sel.ecoms) {
			ecom.linkToWorld( null );
			coms.add( ecom.getComponent() );
			tsim.add( ecom.getComponent() );
		}
		
		tsim.disconnectAllNotIn( coms );
		
		selection = copy;
	}
	
	
	
	public static Selection get() {
		if (isEmpty())
			throw new RuntimeException( "Clipboard is empty!" );
		
		return Util.deepCopy( selection );
	}
	
	
	
	public static boolean isEmpty() {
		return selection == null;
	}
}
