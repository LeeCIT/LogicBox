


package logicBox.gui.editor.toolbox;

import javax.swing.JButton;
import logicBox.gui.editor.tools.ToolManager;
import logicBox.util.Evaluator;



/**
 * A button specialised for use in the Toolbox class.
 * @author Lee Coakley
 */
public abstract class ToolboxButton extends JButton
{
	private Evaluator<ToolManager> targetEvaluator;
	
	
	
	protected void setToolManagerEvaluator( Evaluator<ToolManager> targetEvaluator ) {
		this.targetEvaluator = targetEvaluator;
	}
	
	
	
	protected ToolManager getTargetToolManager() {
		return targetEvaluator.evaluate();
	}
}























