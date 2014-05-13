


package logicBox.gui.editor;

import logicBox.gui.editor.graphics.GraphicGen;
import logicBox.gui.editor.graphics.GraphicSevenSeg;
import logicBox.sim.component.DisplaySevenSeg;
import logicBox.util.Vec2;



/**
 * Specialised for the hex display.
 * @author Lee Coakley
 */
public class EditorComponentSevenSeg extends EditorComponentActive
{
	private static final long serialVersionUID = 1L;
	
	
	
	public EditorComponentSevenSeg( DisplaySevenSeg com, Vec2 pos, double angle ) {
		super( com, GraphicGen.generateDisplaySevenSeg(), pos, angle );
	}
	
	
	
	public DisplaySevenSeg getComponent() {
		return (DisplaySevenSeg) super.getComponent();
	}
	
	
	
	public GraphicSevenSeg getGraphic() {
		return (GraphicSevenSeg) super.getGraphic();
	}
	
	
	
	public void onWorldChange() {
		getGraphic().setSegmentStates( getComponent().getSegmentStates() );
	}
}
