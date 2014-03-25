


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
		CommonActions.addOpenCircuitListener   ( toolbar.buttOpen,           frame );
		CommonActions.addPrintCircuitListener  ( toolbar.buttPrint,          frame );
		CommonActions.addRecentreCameraListener( toolbar.buttCameraRecentre, frame );
		CommonActions.addGridToggleListener    ( toolbar.buttToggleGrid,     frame );
		CommonActions.addHelpListener          ( toolbar.buttHelp,           frame );
		
		setupUndoRedo( toolbar, frame );
	}
	
	
	
	private void setupUndoRedo( final EditorToolbar toolbar, final EditorFrame frame ) {
		CommonActions.addUndoListener( toolbar.buttUndo, frame );
		CommonActions.addRedoListener( toolbar.buttRedo, frame );
		
		toolbar.buttUndo.setEnabled( false );
		toolbar.buttRedo.setEnabled( false );
		
		final HistoryManager<EditorWorld> manager = frame.getEditorPanel().getHistoryManager();
		
		manager.addOnChangeCallback( new Callback() {
			public void execute() {
				toolbar.buttUndo.setEnabled( manager.canUndo() );
				toolbar.buttRedo.setEnabled( manager.canRedo() );
			}
		});
	}
}
