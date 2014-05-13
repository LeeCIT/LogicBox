


package logicBox.sim.component.simple;

import logicBox.gui.editor.graphics.GraphicComActive;
import logicBox.gui.editor.graphics.GraphicGen;
import logicBox.sim.component.ComponentType;



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
