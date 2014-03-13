


package logicBox.gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import logicBox.gui.editor.EditorFrame;
import logicBox.gui.editor.EditorPanel;
import logicBox.gui.editor.Toolbox;
import logicBox.gui.menubar.EditorMenuBar;
import logicBox.gui.snapping.ComponentSnapper;



/**
 * Global GUI functions.
 * @author Lee Coakley
 */
public abstract class GUI
{
	private static EditorFrame mainFrame;
	
	
	
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
	
	
	
	/**
	 * Get the current instance of the main frame.  If this frame closes the program exits.
	 */
	public EditorFrame getMainFrame() {
		return mainFrame;
	}
	
	
	
	private static void constructGUI() {
		EditorPanel   panel =             new EditorPanel();
		EditorFrame   frame = mainFrame = new EditorFrame( panel );
		EditorMenuBar menu  =             new EditorMenuBar();
		Toolbox       box   =             new Toolbox( frame );
		
		box.addComponentListener( new ComponentSnapper(frame) );
		
		frame.setJMenuBar( menu );
		box.setActiveEditorPanel( panel );
		
		frame.pack();
		frame.setSize( 720, 640 );
		frame.setVisible( true );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
}



















