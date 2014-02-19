


package logicBox.gui;

import javax.swing.JFrame;

import logicBox.gui.editor.GraphicGen;
import logicBox.gui.editor.Toolbox;
import logicBox.gui.editor.ToolboxButton;


/**
 * Create the toolbox
 * @author John
 *
 */
public class ToolboxFrame extends Toolbox
{
	/**
	 * Creates the toolbox
	 * @param frame	    The frame to snap to
	 */
	public ToolboxFrame(JFrame attachedFrame) {
		super(attachedFrame);
		ToolboxButton[] buttons = {
				new ToolboxButton( GraphicGen.generateGateBuffer(), "test", null ),
				new ToolboxButton( GraphicGen.generateGateNot(),    "test", null ),
				new ToolboxButton( GraphicGen.generateGateAnd(2),   "test", null ),
				new ToolboxButton( GraphicGen.generateGateNand(2),  "test", null ),
				new ToolboxButton( GraphicGen.generateGateOr(2),    "test", null ),
				new ToolboxButton( GraphicGen.generateGateNor(2),   "test", null ),
				new ToolboxButton( GraphicGen.generateGateXor(2),   "test", null ),
				new ToolboxButton( GraphicGen.generateGateXnor(2),  "test", null )
		};

		this.addCategory( "Gates", buttons );		
	}
}
