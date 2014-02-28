


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
		setupCloudMenu();
		setupHelpMenu();
		setupMnemonics();
	}
	
	
	
	private void setupFileMenu() {
		menuFile = new JMenu( "File" );
		itemFileNew    = add( menuFile, "New" );
		itemFileOpen   = add( menuFile, "Open" );
		itemFileSave   = add( menuFile, "Save" );
		itemFileSaveAs = add( menuFile, "Save as..." );
		menuFile.addSeparator();
		itemFilePrint  = add( menuFile, "Print..." );
		menuFile.addSeparator();
		itemFileExit   = add( menuFile, "Exit" );
		add( menuFile );
	}
	
	
	
	private void setupCloudMenu() {
		menuCloud = new JMenu( "Cloud" );
		itemCloudLogin  = add( menuCloud, "Login" );
		itemCloudLogout = add( menuCloud, "Logout" );
		add( menuCloud );
	}
	
	
	
	private void setupHelpMenu() {
		menuHelp = new JMenu( "Help" );
		itemHelpHelp = add( menuHelp, "Help..." );
		menuHelp.addSeparator();
		itemHelpAbout = add( menuHelp, "About..." );
		add( menuHelp );
	}
	
	
	
	private JMenuItem add( JMenu menu, String name ) {
		JMenuItem item = new JMenuItem( name );
		menu.add( item );
		return item;
	}
	
	
	
	private void setupMnemonics() {
		menuFile.setMnemonic( 'F' );
		itemFileNew   .setMnemonic( 'N' );
		itemFileOpen  .setMnemonic( 'O' );
		itemFileSave  .setMnemonic( 'S' );
		itemFileSaveAs.setMnemonic( 'A' );
		itemFilePrint .setMnemonic( 'P' );
		itemFileExit  .setMnemonic( 'X' );
		
		menuHelp.setMnemonic( 'H' );
		itemHelpHelp .setMnemonic( 'H' );
		itemHelpAbout.setMnemonic( 'A' );
	}
}



























