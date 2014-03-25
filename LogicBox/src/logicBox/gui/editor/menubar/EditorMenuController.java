


package logicBox.gui.editor.menubar;
import logicBox.gui.editor.CommonActions;
import logicBox.gui.editor.EditorFrame;
import logicBox.gui.editor.EditorWorld;
import logicBox.gui.editor.HistoryManager;
import logicBox.util.Callback;



/**
 * Adds actions to an EditorMenuBar.
 * @author Lee Coakley
 */
public class EditorMenuController
{
	public EditorMenuController( final EditorMenuBar menu, final EditorFrame frame ) {
		CommonActions.addOpenCircuitListener ( menu.itemFileOpen,  frame );
		CommonActions.addPrintCircuitListener( menu.itemFilePrint, frame );
		CommonActions.addHelpListener        ( menu.itemHelpHelp,  frame );
		
		setupUndoRedo( menu, frame );
	}
	
	
	
	private void setupUndoRedo( final EditorMenuBar menu, final EditorFrame frame ) {
		CommonActions.addUndoListener( menu.itemEditUndo, frame );
		CommonActions.addRedoListener( menu.itemEditRedo, frame );
		
		menu.itemEditUndo.setEnabled( false );
		menu.itemEditRedo.setEnabled( false );
		
		final HistoryManager<EditorWorld> manager = frame.getEditorPanel().getHistoryManager();
		
		manager.addOnChangeCallback( new Callback() {
			public void execute() {
				menu.itemEditUndo.setEnabled( manager.canUndo() );
				menu.itemEditRedo.setEnabled( manager.canRedo() );
			}
		});
	}
}
