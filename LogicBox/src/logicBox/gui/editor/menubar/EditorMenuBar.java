


package logicBox.gui.editor.menubar;

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
	public JMenuItem itemEditDelete;
	public JMenuItem itemEditSelectAll;
	public JMenuItem itemEditSelectNone;
	public JMenuItem itemEditSelectInvert;
	public JMenuItem itemEditSelectBlackBox;
	
	public JMenu     menuView;
	public JMenuItem itemViewGrid;
	public JMenuItem itemViewToolbox;
	public JMenuItem itemViewCamera;
	public JMenuItem itemViewZoomReset;
	public JMenuItem itemViewZoomIn;
	public JMenuItem itemViewZoomOut;
	
	
	public JMenu     menuCloud;
	public JMenuItem itemCloudLogin;
	public JMenuItem itemCloudLogout;
	public JMenuItem itemCloudFiles;
	public JMenuItem itemCloudRegister;
	
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
		itemFileNew    = add( m, "New"       , false, 'N', "control       N", IconEnum.newFile  );
		itemFileOpen   = add( m, "Open"      , false, 'O', "control       O", IconEnum.openFile );
		itemFileSave   = add( m, "Save"      , false, 'S', "control       S", IconEnum.saveFile );
		itemFileSaveAs = add( m, "Save as...", false, 'A', "control shift S", IconEnum.saveFile );
		itemFilePrint  = add( m, "Print..."  , true , 'P', "control       P", IconEnum.print    );
		itemFileExit   = add( m, "Exit"      , true , 'X', "alt F4"         , IconEnum.cancel   );
		add( m );
	}
	
	
	
	private void setupEditMenu() {
		JMenu m = menuEdit = createMenu( "Edit", 'E' );
		itemEditUndo           = add( m, "Undo"              , false, 'U', "control       Z", IconEnum.undo          );
		itemEditRedo           = add( m, "Redo"              , false, 'R', "control       Y", IconEnum.redo          );
		itemEditCut            = add( m, "Cut"               , true , 'T', "control       X", IconEnum.cut           );
		itemEditCopy           = add( m, "Copy"              , false, 'C', "control       C", IconEnum.copy          );
		itemEditPaste          = add( m, "Paste"             , false, 'P', "control       V", IconEnum.paste         );
		itemEditDelete         = add( m, "Delete"            , true,  'D', "DELETE",          IconEnum.delete        );
		itemEditSelectAll      = add( m, "Select All"        , true , 'A', "control       A", IconEnum.selectAll     );
		itemEditSelectNone     = add( m, "Select None"       , false, 'N', "control shift A", IconEnum.selectNone    );
		itemEditSelectInvert   = add( m, "Invert Selection"  , false, 'I', null             , IconEnum.selectInverse );
		itemEditSelectBlackBox = add( m, "Blackbox Selection", false, 'B', "control B"      , IconEnum.selectBlack   );
		add( m );
	}
	
	
	
	private void setupViewMenu() {
		JMenu m = menuView = createMenu( "View", 'V' );
		itemViewGrid      = add( m, "Grid On/Off",       false, 'G', "control G",      IconEnum.grid    );
		itemViewToolbox   = add( m, "Toolbox Show/Hide", false, 'T', "control T",      IconEnum.toolbox );
		itemViewCamera    = add( m, "Camera Recentre",   true,  'C', "control R",      IconEnum.camera  );
		itemViewZoomReset = add( m, "Zoom Reset",        false, 'R', "control 0",      IconEnum.zoom    );
		itemViewZoomIn    = add( m, "Zoom In",           false, 'I', "control EQUALS", IconEnum.zoomIn  );
		itemViewZoomOut   = add( m, "Zoom Out",          false, 'O', "control MINUS",  IconEnum.zoomOut );
		add( m );
	}
	
	
	
	private void setupCloudMenu() {
		JMenu m = menuCloud = createMenu( "Cloud", 'C' );
		itemCloudFiles 	   = add( m, "My Circuits", false, 'C', null, IconEnum.title      );
		itemCloudRegister  = add( m, "Register"   , true,  'R', null, IconEnum.newFile    );
		itemCloudLogin     = add( m, "Login"      , false, 'L', null, IconEnum.connect    );
		itemCloudLogout    = add( m, "Logout"     , false, 'O', null, IconEnum.disconnect );
		
		itemCloudLogout.setEnabled( false );
		itemCloudFiles .setEnabled( false );
		
		add( m );
	}
	
	
	
	private void setupHelpMenu() {
		JMenu m = menuHelp = createMenu( "Help", 'H' );
		itemHelpHelp  = add( m, "Help..." , false, 'H', "F1", IconEnum.help );
		itemHelpAbout = add( m, "About...", true , 'A' );
		add( m );
	}
	
	
	
	private JMenu createMenu( String name, char mnemonic ) {
		JMenu menu = new JMenu( name );
		menu.setMnemonic( mnemonic );
		return menu;
	}
	
	
	
	private JMenuItem add( JMenu menu, String name ) {
		return add( menu, name, false, (char) 0, null );
	}
	
	
	
	private JMenuItem add( JMenu menu, String name, boolean sep, char mnemonic ) {
		return add( menu, name, sep, mnemonic, null, null );
	}
	
	
	
	private JMenuItem add( JMenu menu, String name, boolean sep, char mnemonic, String accel ) {
		return add( menu, name, sep, mnemonic, accel, null );
	}
	
	
	
	private JMenuItem add( JMenu menu, String name, boolean sep, char mnemonic, String accel, IconEnum icon ) {
		JMenuItem item = new JMenuItem( name );
		item.setMnemonic( mnemonic );
		item.setAccelerator(KeyStroke.getKeyStroke(accel));
		
		if (mnemonic != 0) item.setMnemonic( mnemonic );
		if (icon != null)  item.setIcon( IconLoader.load(icon) );
		if (accel != null) item.setAccelerator( KeyStroke.getKeyStroke(accel) );
		if (sep)           menu.addSeparator();
		
		menu.add( item );
		return item;
	}
}



























