


package logicBox.gui.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import logicBox.gui.CommonActionEvents;



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
	public JMenuItem itemCloudFiles;
	
	public JMenu     menuHelp;
	public JMenuItem itemHelpHelp;
	public JMenuItem itemHelpAbout;
	
	private static EditorMenuBar instance = null;
	
	public EditorMenuBar() {
		super();
		setupComponents();
		setUpActions();
		instance = this;
	}
	
	public static EditorMenuBar getInstance()
	{
		return instance;
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
		itemCloudFiles 	= add( m, "My Circuits");
		itemCloudLogout = add( m, "Logout" );
		
		setAuthState(false);
		
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
	 * Set authentication state
	 * @param status
	 */
	public void setAuthState(Boolean status) {
		itemCloudLogout.setVisible(status);
		itemCloudLogin.setVisible(!status);
		itemCloudFiles.setVisible(status);
	}
	
	
	
	/**
	 * Actions for the menubar are here
	 */
	private void setUpActions() {
		EditorMenuBarEvent.handleLoginEvent(itemCloudLogin);
		EditorMenuBarEvent.handleLogoutEvent(itemCloudLogout);
		itemFileOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CommonActionEvents.openFileAction();  //TODO do something with the file got back
			}
		});
	}
}



























