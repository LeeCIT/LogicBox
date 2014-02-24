
package logicBox.core;

import java.awt.Dimension;
import javax.swing.JFrame;
import logicBox.gui.CreateMenubar;
import logicBox.gui.ToolboxFrame;
import logicBox.gui.editor.EditorFrame;
import logicBox.gui.editor.EditorPanel;
import logicBox.gui.windowPosition.WindowPositionManager;


public class CreateGUI extends EditorFrame
{
	// Reference to main frame
	public static CreateGUI currentInstance;
	
	private EditorPanel editorPane;
	
	public CreateGUI() {
		currentInstance = this;
		
		// Save the window position
		this.addWindowListener(new WindowPositionManager(this));
		
		add( new CreateMenubar(),    "north" );
		add( new ToolboxFrame(this), "west" );
		
		editorPane = new EditorPanel();
		add( editorPane );
		
		// Restore the position of the frame
		//WindowPositionManager.restoreWindowPosition(this);
		
		setUpFrameSettings();
	}
	
	
	
	
	
	
	
	/**
	 * The basic settings of the frame
	 */
	private void setUpFrameSettings() {
		setSize( new Dimension(600,600) );
		setVisible( true );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
	
	
	
	public EditorPanel getEditorPane() {
		return editorPane;
	}
	
	
	
}
