


package logicBox.gui.editor;

import logicBox.sim.component.BlackBoxPin;
import logicBox.util.Vec2;



public class EditorComponentBlackboxPin extends EditorComponentActive
{
	public EditorComponentBlackboxPin( BlackBoxPin com, GraphicComActive gca, Vec2 pos, double angle ) {
		super( com, gca, pos, angle );
	}
	
	
	
	public EditorComponentBlackboxPin( BlackBoxPin com, GraphicComActive gca, Vec2 pos ) {
		this( com, gca, pos, gca.getAngle() );
	}
	
	
	
	public BlackBoxPin getComponent() {
		return (BlackBoxPin) super.getComponent();
	}
}
