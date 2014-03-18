


package logicBox.gui.editor.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import logicBox.gui.CommonActions;



/**
 * Configure the actions for an editor toolbar.
 * @author Lee Coakley
 */
public class EditorToolbarController
{
	/**
	 * Set up the action listeners
	 */
	private void setUpActions() {
		openFileButt.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CommonActions.openFile(); // TODO do something with the file got back				
			}
		});
	}
}
