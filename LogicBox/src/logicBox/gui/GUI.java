


package logicBox.gui;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import logicBox.gui.editor.EditorFrame;
import logicBox.gui.editor.EditorPanel;
import logicBox.gui.editor.Toolbox;
import logicBox.gui.menubar.EditorMenuBar;



/**
 * Global GUI functions.
 * @author Lee Coakley
 */
public class GUI
{
	/**
	 * Makes the UI style take on the appearance of the underlying platform.
	 * Must be called before any UI elements are created.
	 */
	public static void setNativeStyle() {
		try {
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		}
		catch (Exception ex) {
			System.out.println( ex );
		}	
	}
	
	
	
	/**
	 * Create the main GUI components and link them together.
	 */
	public static void create() {
		SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				constructGUI();
			}
		});
	}



	private static void constructGUI() {
		EditorPanel   panel = new EditorPanel();
		JFrame        frame = new EditorFrame( panel );
		EditorMenuBar menu  = new EditorMenuBar();
		Toolbox       box   = new Toolbox();
		
		frame.setJMenuBar( menu );
		box.setActiveEditorPanel( panel );
		
		frame.pack();
		frame.setSize( 720, 640 );
		frame.add( box, "west" );
		frame.setVisible( true );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
}
