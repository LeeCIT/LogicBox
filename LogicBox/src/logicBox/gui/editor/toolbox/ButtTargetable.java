


package logicBox.gui.editor.toolbox;

import javax.swing.JButton;
import logicBox.gui.editor.tools.ToolManager;
import logicBox.util.Evaluator;



public interface ButtTargetable {
	public void        setToolManagerEvaluator( Evaluator<ToolManager> targetEvaluator );
	public ToolManager getTargetToolManager();
	public JButton     getButton();
}
