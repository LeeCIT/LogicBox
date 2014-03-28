


package logicBox.gui.editor.menubar;

import java.awt.Event;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
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
	
	public JMenu     menuView;
	public JMenuItem itemViewGrid;
	public JMenuItem itemViewCamera;
	
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
		setupViewMenu();
		setupCloudMenu();
		setupHelpMenu();
	}
	
	
	
	private void setupFileMenu() {
		JMenu m = menuFile = createMenu( "File", 'F' );
		itemFileNew    = add( m, "New"       , false, 'N', KeyEvent.VK_N, true, false, IconEnum.newFile  );
		itemFileOpen   = add( m, "Open"      , false, 'O', KeyEvent.VK_O, true, false, IconEnum.openFile );
		itemFileSave   = add( m, "Save"      , false, 'S', KeyEvent.VK_S, true, false, IconEnum.saveFile );
		itemFileSaveAs = add( m, "Save as...", true , 'A', KeyEvent.VK_S, true, true , IconEnum.saveFile );
		itemFilePrint  = add( m, "Print..."  , true , 'P', KeyEvent.VK_N, true, false, IconEnum.print    );
		itemFileExit   = add( m, "Exit"      , false, 'X' );
		add( m );
	}
	
	
	
	private void setupEditMenu() {
		JMenu m = menuEdit = createMenu( "Edit", 'E' );
		itemEditUndo  = add( m, "Undo" , false, 'U', KeyEvent.VK_Z, true, false, IconEnum.undo  );
		itemEditRedo  = add( m, "Redo" , true , 'R', KeyEvent.VK_Y, true, false, IconEnum.redo  );
		itemEditCut   = add( m, "Cut"  , false, 'T', KeyEvent.VK_X, true, false, IconEnum.cut   );
		itemEditCopy  = add( m, "Copy" , false, 'C', KeyEvent.VK_C, true, false, IconEnum.copy  );
		itemEditPaste = add( m, "Paste", false, 'P', KeyEvent.VK_V, true, false, IconEnum.paste );
		add( m );
	}
	
	
	
	private void setupViewMenu() {
		JMenu m = menuView = createMenu( "View", 'V' );
		itemViewGrid   = add( m, "Grid On/Off",      false, 'G', KeyEvent.VK_G, true, false, IconEnum.grid   );
		itemViewCamera = add( m, "Camera Recentre" , true , 'C', KeyEvent.VK_R, true, false, IconEnum.camera );
		add( m );
	}
	
	
	
	private void setupCloudMenu() {
		JMenu m = menuCloud = createMenu( "Cloud", 'C' );
		itemCloudLogin  = add( m, "Login"       );
		itemCloudFiles 	= add( m, "My Circuits" );
		itemCloudLogout = add( m, "Logout"      );
		add( m );
	}
	
	
	
	private void setupHelpMenu() {
		JMenu m = menuHelp = createMenu( "Help", 'H' );
		itemHelpHelp  = add( m, "Help..." , true,  'H', KeyEvent.VK_F1, false, false, IconEnum.help );
		itemHelpAbout = add( m, "About...", false, 'A' );
		add( m );
	}
	
	
	
	private JMenu createMenu( String name, char mnemonic ) {
		JMenu menu = new JMenu( name );
		menu.setMnemonic( mnemonic );
		return menu;
	}
	
	
	
	private JMenuItem add( JMenu menu, String name ) {
		return add( menu, name, false, (char) 0, 0, false, false );
	}
	
	
	
	private JMenuItem add( JMenu menu, String name, boolean sep, char mnemonic ) {
		return add( menu, name, sep, mnemonic, 0, false, false, null );
	}
	
	
	
	private JMenuItem add( JMenu menu, String name, boolean sep, char mnemonic, int accelKey, boolean ctrl, boolean shift ) {
		return add( menu, name, sep, mnemonic, accelKey, ctrl, shift, null );
	}
	
	
	
	private JMenuItem add( JMenu menu, String name, boolean sep, char mnemonic, int accelKey, boolean ctrl, boolean shift, IconEnum icon ) {
		JMenuItem item = new JMenuItem( name );
		
		menu.add( item );
		
		if (mnemonic != 0) item.setMnemonic( mnemonic );
		if (icon != null)  item.setIcon( IconLoader.load(icon) );
		if (accelKey != 0) setAccelerator( item, accelKey, ctrl, shift );
		if (sep)           menu.addSeparator();
		
		return item;
	}
	
	
	
	private void setAccelerator( JMenuItem item, int accelKey, boolean ctrl, boolean shift ) {
		if (accelKey != 0) {
			int       mask = (ctrl ? Event.CTRL_MASK : 0) | (shift ? Event.SHIFT_MASK : 0);
			KeyStroke ks   = KeyStroke.getKeyStroke( accelKey, mask );
			item.setAccelerator( ks );
		}
	}
}



























