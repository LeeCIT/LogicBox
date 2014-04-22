


package logicBox.gui.editor;

import logicBox.sim.component.BlackBoxPin;
import logicBox.util.CallbackParam;
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
	
	
	
	public boolean onMouseClick( CallbackParam<String> onMod ) {
		boolean simChanged = super.onMouseClick( onMod );
		
		boolean isPoweredOn = getComponent().getState();
		getGraphic().setFillOverride( isPoweredOn, EditorStyle.colLedOn );
		
		return simChanged;
	}
}
