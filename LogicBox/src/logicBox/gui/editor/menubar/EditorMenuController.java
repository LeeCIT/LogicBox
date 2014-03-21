


package logicBox.gui.editor.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import logicBox.gui.editor.CommonActions;
import logicBox.gui.editor.EditorFrame;
import logicBox.gui.editor.EditorPanel;



/**
 * Adds actions to an EditorMenuBar.
 * @author Lee Coakley
 */
public class EditorMenuController
{
	public EditorMenuController( EditorMenuBar menu, EditorFrame frame ) {
		CommonActions.addOpenCircuitListener ( menu.itemFileOpen,  frame );
		CommonActions.addPrintCircuitListener( menu.itemFilePrint, frame );
	}
}
