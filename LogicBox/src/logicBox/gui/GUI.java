


package logicBox.gui;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import logicBox.core.Main;
import logicBox.gui.editor.EditorFrame;
import logicBox.gui.editor.controllers.EditorCreator;
import logicBox.gui.editor.toolbox.Toolbox;



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
	
	
	
	/**
	 * Modally ask a question, with three possible answers.
	 */
	public static DialogueAnswer askYesNoCancel( Component parent, String title, String question ) {
		int answer = JOptionPane.showConfirmDialog( parent, question, title, JOptionPane.YES_NO_CANCEL_OPTION );
		
		switch (answer) {
			case JOptionPane.YES_OPTION: return DialogueAnswer.YES;
			case JOptionPane.NO_OPTION:  return DialogueAnswer.NO;
			default:					 return DialogueAnswer.CANCEL;
		}
	}
	
	
	
	/**
	 * Modally confirm an action.
	 */
	public static boolean askConfirm( Component parent, String title, String question ) {
		int answer = JOptionPane.showConfirmDialog( parent, question, title, JOptionPane.YES_NO_OPTION );
		
		switch (answer) {
			case JOptionPane.YES_OPTION: return true;
			default:					 return false;
		}
	}
	
	
	
	/**
	 * Show a modal message dialogue.
	 */
	public static void showMessage( Component parent, String title, String message ) {
		JOptionPane.showMessageDialog( parent, message, title, JOptionPane.INFORMATION_MESSAGE );
	}
	
	
	
	/**
	 * Show a modal warning dialogue.
	 */
	public static void showWarning( Component parent, String title, String message ) {
		JOptionPane.showMessageDialog( parent, message, title, JOptionPane.WARNING_MESSAGE );
	}
	
	
	
	/**
	 * Show a modal error dialogue.
	 */
	public static void showError( Component parent, String title, String message ) {
		JOptionPane.showMessageDialog( parent, message, title, JOptionPane.ERROR_MESSAGE );
	}
	
	
	
	/**
	 * Print a list of errors
	 * @param f
	 * @param errors
	 * @param title
	 */
	public static void showErrorList(JFrame f, ArrayList<String> errors, String title) {
		String message = "<html><ul>";
		
		for(String error : errors)
			message += "<li>" + error + "</li>";
		
		message += "</ul></html>";
		
		JOptionPane.showMessageDialog(f, message, title, JOptionPane.ERROR_MESSAGE);
	}
	
	
	
	private static void constructGUI() {
		editorFrame = EditorCreator.createEditorFrame( true );
		Main.onStartup();
	}
}



















