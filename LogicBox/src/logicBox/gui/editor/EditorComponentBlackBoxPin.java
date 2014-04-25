


package logicBox.gui.editor;

import logicBox.sim.component.BlackBoxPin;
import logicBox.util.CallbackParam;
import logicBox.util.Vec2;



public class EditorComponentBlackBoxPin extends EditorComponentActive
{
	private static final long serialVersionUID = 1L;
	
	
	
	public EditorComponentBlackBoxPin( BlackBoxPin com, GraphicComActive gca, Vec2 pos, double angle ) {
		super( com, gca, pos, angle );
	}
	
	
	
	public EditorComponentBlackBoxPin( BlackBoxPin com, GraphicComActive gca, Vec2 pos ) {
		this( com, gca, pos, gca.getAngle() );
	}
	
	
	
	public BlackBoxPin getComponent() {
		return (BlackBoxPin) super.getComponent();
	}
	
	
	
	public void onWorldChange() {
		updateGraphic();
	}
	
	
	
	public boolean onMouseClick( CallbackParam<String> onMod ) {
		boolean simChanged = super.onMouseClick( onMod );
		updateGraphic();
		return simChanged;
	}
	
	
	
	private void updateGraphic() {
		boolean isPoweredOn = getComponent().getState();
		getGraphic().setFillOverride( isPoweredOn, EditorStyle.colLedOn );
	}
}
