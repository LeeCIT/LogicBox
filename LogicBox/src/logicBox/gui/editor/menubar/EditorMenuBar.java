


package logicBox.gui.editor.menubar;

import java.awt.Insets;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import logicBox.gui.IconEnum;
import logicBox.gui.IconLoader;



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
		itemFileNew    = add( m, "New"       , 'N', false, IconEnum.newFile  );
		itemFileOpen   = add( m, "Open"      , 'O', false, IconEnum.openFile );
		itemFileSave   = add( m, "Save"      , 'S', false, IconEnum.saveFile );
		itemFileSaveAs = add( m, "Save as...", 'A', true , IconEnum.saveFile );
		itemFilePrint  = add( m, "Print..."  , 'P', true , IconEnum.print    );
		itemFileExit   = add( m, "Exit"      , 'X', false );
		add( m );
	}
	
	
	
	private void setupEditMenu() {
		JMenu m = menuEdit = new JMenu( "Edit" );
		itemEditUndo  = add( m, "Undo" , 'U', false, IconEnum.undo  );
		itemEditRedo  = add( m, "Redo" , 'R', true , IconEnum.redo  );
		itemEditCut   = add( m, "Cut"  , 'T', false, IconEnum.cut   );
		itemEditCopy  = add( m, "Copy" , 'C', false, IconEnum.copy  );
		itemEditPaste = add( m, "Paste", 'P', false, IconEnum.paste );
		add( m );
	}
	
	
	
	private void setupCloudMenu() {
		JMenu m = menuCloud = new JMenu( "Cloud" );
		itemCloudLogin  = add( m, "Login"       );
		itemCloudFiles 	= add( m, "My Circuits" );
		itemCloudLogout = add( m, "Logout"      );
		add( m );
	}
	
	
	
	private void setupHelpMenu() {
		JMenu m = menuHelp = new JMenu( "Help" );
		itemHelpHelp  = add( m, "Help..." , 'H', true, IconEnum.help );
		itemHelpAbout = add( m, "About...", 'A', false );
		add( m );
	}
	
	
	
	private JMenuItem add( JMenu menu, String name ) {
		return add( menu, name, (char) 0, false );
	}
	
	
	private JMenuItem add( JMenu menu, String name, char mnemonic, boolean sep ) {
		return add( menu, name, mnemonic, sep, null );
	}
	
	
	
	private JMenuItem add( JMenu menu, String name, char mnemonic, boolean sep, IconEnum icon ) {
		JMenuItem item = new JMenuItem( name );
		
		menu.add( item );
		
		if (mnemonic != 0) item.setMnemonic( mnemonic );
		if (icon != null)  item.setIcon( IconLoader.load(icon) );
		if (sep)           menu.addSeparator();
		
		return item;
	}
}



























