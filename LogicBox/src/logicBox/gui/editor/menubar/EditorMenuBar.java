


package logicBox.gui.editor.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;



/**
 * Menu bar with new/save/print etc.
 * The menu items are public so you can modify them at will.
 * Do not put event handling code in this class!  Use a controller to maintain modularity.
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
	
	public JMenu     menuCloud;
	public JMenuItem itemCloudLogin;
	public JMenuItem itemCloudLogout;
	public JMenuItem itemCloudFiles;
	
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
		itemFileNew    = add( m, "New"       , 'N', false, "control N" );
		itemFileOpen   = add( m, "Open"      , 'O', false, "control O" );
		itemFileSave   = add( m, "Save"      , 'S', false, "control S" );
		itemFileSaveAs = add( m, "Save as...", 'A', true , "");
		itemFilePrint  = add( m, "Print..."  , 'P', true , "control P" );
		itemFileExit   = add( m, "Exit"      , 'X', false, "control E");
		add( m );
	}
	
	
	
	private void setupEditMenu() {
		JMenu m = menuEdit = new JMenu( "Edit" );
		itemEditUndo  = add( m, "Undo" , 'U', false , "control Z");
		itemEditRedo  = add( m, "Redo" , 'R', true  , "control Y");
		itemEditCut   = add( m, "Cut"  , 'T', false , "control X");
		itemEditCopy  = add( m, "Copy" , 'C', false , "control C");
		itemEditPaste = add( m, "Paste", 'P', false , "control V");
		add( m );
	}
	
	
	
	private void setupCloudMenu() {
		JMenu m = menuCloud = new JMenu( "Cloud" );
		itemCloudLogin  = add( m, "Login" );
		itemCloudFiles 	= add( m, "My Circuits");
		itemCloudLogout = add( m, "Logout" );
		add( m );
	}
	
	
	
	private void setupHelpMenu() {
		JMenu m = menuHelp = new JMenu( "Help" );
		itemHelpHelp  = add( m, "Help..." , 'H', true  , "control H");
		itemHelpAbout = add( m, "About...", 'A', false , "control A");
		add( m );
	}
	
	
	
	private JMenuItem add( JMenu menu, String name ) {
		JMenuItem item = new JMenuItem( name );
		menu.add( item );
		return item;
	}
	
	
	
	private JMenuItem add( JMenu menu, String name, char mnemonic, boolean sep, String accel ) {
		JMenuItem item = add( menu, name );
		item.setMnemonic( mnemonic );
		item.setAccelerator(KeyStroke.getKeyStroke(accel));
		
		if (sep)
			menu.addSeparator();
		
		return item;
	}
}



























