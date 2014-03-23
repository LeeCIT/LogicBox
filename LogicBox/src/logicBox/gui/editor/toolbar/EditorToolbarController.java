


package logicBox.gui.editor.toolbar;

import logicBox.gui.editor.CommonActions;
import logicBox.gui.editor.EditorFrame;
import logicBox.gui.editor.EditorWorld;
import logicBox.gui.editor.HistoryManager;
import logicBox.gui.editor.menubar.EditorMenuBar;
import logicBox.util.Callback;



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
		
		setupUndoRedo( toolbar, frame );
	}
	
	
	
	private void setupUndoRedo( final EditorToolbar toolbar, final EditorFrame frame ) {
		CommonActions.addUndoListener( toolbar.undoButt, frame );
		CommonActions.addRedoListener( toolbar.redoButt, frame );
		
		toolbar.undoButt.setEnabled( false );
		toolbar.redoButt.setEnabled( false );
		
		final HistoryManager<EditorWorld> manager = frame.getEditorPanel().getHistoryManager();
		
		manager.addOnChangeCallback( new Callback() {
			public void execute() {
				toolbar.undoButt.setEnabled( manager.canUndo() );
				toolbar.redoButt.setEnabled( manager.canRedo() );
			}
		});
	}
}
