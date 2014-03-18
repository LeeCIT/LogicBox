


package logicBox.gui.editor.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import logicBox.gui.CommonActions;


/**
 * Adds actions to an EditorMenuBar.
 * @author Lee Coakley
 */
public class EditorMenuController
{
	public EditorMenuController( EditorMenuBar menu ) {
		// Add actions here
	}
	
	
	
	/**
	 * Set authentication state
	 * @param state
	 */
	public void setAuthState( boolean state ) {
		itemCloudLogout.setEnabled( state);
		itemCloudLogin .setEnabled(!state);
		itemCloudFiles .setEnabled( state);
	}
	
	
	
	/**
	 * Actions for the menubar are here
	 */
	private void setUpActions() {
		EditorMenuBarEvent.handleLoginEvent ( itemCloudLogin  );
		EditorMenuBarEvent.handleLogoutEvent( itemCloudLogout );
		
		itemFileOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CommonActions.openFile();  //TODO do something with the file got back
			}
		});
		
		itemFilePrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CommonActions.print();
			}
		});
	}
}
