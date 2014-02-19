
package logicBox.core;

import java.awt.Dimension;
import javax.swing.JFrame;
import logicBox.gui.ToolboxFrame;
import logicBox.gui.editor.EditorFrame;
import logicBox.gui.editor.EditorPanel;


public class CreateGUI extends EditorFrame
{
	// Reference to main frame
	private static CreateGUI currentInstance; 
	
	public CreateGUI() {
		add( new ToolboxFrame(this), "west" );
		add( new EditorPanel() );	
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
	
	
	
	
}
