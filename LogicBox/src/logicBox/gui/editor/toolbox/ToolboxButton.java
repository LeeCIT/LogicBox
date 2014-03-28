


package logicBox.gui.editor.toolbox;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import logicBox.gui.editor.tools.ToolManager;
import logicBox.util.Evaluator;



/**
 * A button specialised for use in the Toolbox class.
 * @author Lee Coakley
 */
public class ToolboxButton extends JButton
{
	private Evaluator<ToolManager> targetEvaluator;
	
	
	
	public ToolboxButton() {
		super();
	}
	
	
	
	public ToolboxButton( Action a ) {
		super( a );
	}
	
	
	
	public ToolboxButton( Icon icon ) {
		super( icon );
	}
	
	
	
	public ToolboxButton( String text, Icon icon ) {
		super( text, icon );
	}
	
	
	
	public ToolboxButton( String text ) {
		super( text );
	}
	
	
	
	protected void setToolManagerEvaluator( Evaluator<ToolManager> targetEvaluator ) {
		this.targetEvaluator = targetEvaluator;
	}
	
	
	
	protected ToolManager getTargetToolManager() {
		return targetEvaluator.evaluate();
	}
}























