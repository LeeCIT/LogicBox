


package logicBox.sim.component;

import logicBox.gui.editor.GraphicComActive;
import logicBox.gui.editor.GraphicGen;



/**
 * A fixed logic-level source.
 * @author Lee Coakley
 */
public class SourceFixed extends Source
{
	private static final long serialVersionUID = 1L;
	
	
	
	public SourceFixed( boolean state ) {
		super( state );
		setState( state );
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.sourceFixed;
	}
	
	
	
	public String getName() {
		return "Fixed source (" + (getState()?1:0) + ")";
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateSourceFixed( getState() );
	}
}
