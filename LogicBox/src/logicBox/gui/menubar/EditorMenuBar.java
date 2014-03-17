


package logicBox.gui.menubar;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;



/**
 * Menu bar with new/save/print etc.
 * @author Lee Coakley
 */
public class EditorMenuBar extends JMenuBar
{
	public JMenu     menuFile;
	public JMenuItem itemFileNew;
	public JMenuItem itemFileOpen;
	public JMenuItem itemFileSave;
	public JMenuItem itemFileSaveAs;
	public JMenuItem itemFilePrint;
	public JMenuItem itemFileExit;
	
	public JMenu     menuEdit;
	public JMenuItem itemEditUndo;
	public JMenuItem itemEditRedo;
	public JMenuItem itemEditCut;
	public JMenuItem itemEditCopy;
	public JMenuItem itemEditPaste;
	
	public JMenu     menuCloud; // Test
	public JMenuItem itemCloudLogin;
	public JMenuItem itemCloudLogout;
	
	public JMenu     menuHelp;
	public JMenuItem itemHelpHelp;
	public JMenuItem itemHelpAbout;
	
	
	
	public EditorMenuBar() {
		super();
		setupComponents();
	}
	
	
	
	private void setupComponents() {
		setupFileMenu();
		setupEditMenu();
		setupCloudMenu();
		setupHelpMenu();
	}
	
	
	
	private void setupFileMenu() {
		JMenu m = menuFile = new JMenu( "File" );
		itemFileNew    = add( m, "New"       , 'N', false );
		itemFileOpen   = add( m, "Open"      , 'O', false );
		itemFileSave   = add( m, "Save"      , 'S', false );
		itemFileSaveAs = add( m, "Save as...", 'A', true  );
		itemFilePrint  = add( m, "Print..."  , 'P', true  );
		itemFileExit   = add( m, "Exit"      , 'X', false );
		add( m );
	}
	
	
	
	private void setupEditMenu() {
		JMenu m = menuEdit = new JMenu( "Edit" );
		itemEditUndo  = add( m, "Undo" , 'U', false );
		itemEditRedo  = add( m, "Redo" , 'R', true  );
		itemEditCut   = add( m, "Cut"  , 'T', false );
		itemEditCopy  = add( m, "Copy" , 'C', false );
		itemEditPaste = add( m, "Paste", 'P', false );
		add( m );
	}
	
	
	
	private void setupCloudMenu() {
		JMenu m = menuCloud = new JMenu( "Cloud" );
		itemCloudLogin  = add( m, "Login" );
		itemCloudLogout = add( m, "Logout" );
		add( m );
	}
	
	
	
	private void setupHelpMenu() {
		JMenu m = menuHelp = new JMenu( "Help" );
		itemHelpHelp  = add( m, "Help..." , 'H', true  );
		itemHelpAbout = add( m, "About...", 'A', false );
		add( m );
	}
	
	
	
	private JMenuItem add( JMenu menu, String name ) {
		JMenuItem item = new JMenuItem( name );
		menu.add( item );
		return item;
	}
	
	
	
	private JMenuItem add( JMenu menu, String name, char mnemonic, boolean sep ) {
		JMenuItem item = add( menu, name );
		item.setMnemonic( mnemonic );
		
		if (sep)
			menu.addSeparator();
		
		return item;
	}
	
	
	
	/**
	 * Set whether the log in item is enabled or not
	 * @param status
	 */
	public void setLogInItemstatus(Boolean status) {
		itemCloudLogin.setEnabled(status);
	}
	
	
	
	/**
	 * Set whether the log out item is enabled or not
	 * @param status
	 */
	public void setLogOutItemStatus(Boolean status) {
		itemCloudLogout.setEnabled(status);
	}
}



























