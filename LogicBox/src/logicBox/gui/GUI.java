


package logicBox.gui;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import logicBox.gui.editor.EditorFrame;
import logicBox.gui.editor.Toolbox;
import logicBox.gui.editor.menubar.EditorMenuController;
import logicBox.gui.editor.toolbar.EditorToolbarController;
import logicBox.gui.snapping.ComponentSnapper;



/**
 * Global GUI functions.
 * @author Lee Coakley
 */
public abstract class GUI
{
	private static EditorFrame editorFrame;
	private static Toolbox     toolbox;
	
	
	
	/**
	 * Makes the UI style take on the appearance of the underlying platform.
	 * Must be called before any UI elements are created.
	 */
	public static void setNativeStyle() {
		try {
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		}
		catch (Exception ex) {
			ex.printStackTrace();
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
	 * There can only be one main frame, but there can be more than one actual frame. (black boxes etc).
	 */
	public static EditorFrame getMainFrame() {
		return editorFrame;
	}
	
	
	
	/**
	 * Get the toolbox instance.  There can only be one.
	 */
	public static Toolbox getToolbox() {
		return toolbox;
	}
	
	
	
	private static void constructGUI() {
		editorFrame = new EditorFrame();
		toolbox     = new Toolbox( editorFrame );
		
		toolbox.addComponentListener( new ComponentSnapper(editorFrame) );
		toolbox.setActiveEditorPanel( editorFrame.getEditorPanel() );
		
		new EditorMenuController   ( editorFrame.getEditorMenuBar(), editorFrame );
		new EditorToolbarController( editorFrame.getEditorToolbar(), editorFrame );
		
		editorFrame.pack();
		editorFrame.setSize       ( new Dimension(720, 640) );
		editorFrame.setMinimumSize( new Dimension(640, 480) );
		editorFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		editorFrame.setVisible( true );
		
		editorFrame.getEditorPanel().addDebugAndDemoStuff();
	}
}



















