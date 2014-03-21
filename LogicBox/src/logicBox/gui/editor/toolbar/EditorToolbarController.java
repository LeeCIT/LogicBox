


package logicBox.gui.editor.toolbar;

import logicBox.gui.editor.CommonActions;
import logicBox.gui.editor.EditorFrame;



/**
 * Configure the actions for an editor toolbar.
 * @author Lee Coakley
 */
public class EditorToolbarController
{
	public EditorToolbarController( EditorToolbar toolbar, EditorFrame frame ) {
		CommonActions.addOpenCircuitListener   ( toolbar.openFileButt,  frame );
		CommonActions.addPrintCircuitListener  ( toolbar.printFileButt, frame );
		CommonActions.addRecentreCameraListener( toolbar.centreCamButt, frame );
	}
}
