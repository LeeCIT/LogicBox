
package logicBox.core;

import java.awt.Dimension;

import javax.swing.JFrame;

import logicBox.gui.CreateMenubar;
import logicBox.gui.ToolboxFrame;
import logicBox.gui.editor.EditorFrame;
import logicBox.gui.editor.EditorPanel;


public class CreateGUI extends EditorFrame
{
	// Reference to main frame
	public static CreateGUI currentInstance; 
	
	public CreateGUI() {
		currentInstance = this;
		
		add( new CreateMenubar(),    "north" );
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
