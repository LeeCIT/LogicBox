


package logicBox.gui.editor;

import logicBox.sim.component.BlackBoxPin;
import logicBox.util.CallbackParam;
import logicBox.util.Geo;
import logicBox.util.Vec2;



public class EditorComponentBlackboxPin extends EditorComponentActive
{
	private static final long serialVersionUID = 1L;
	
	
	
	public EditorComponentBlackboxPin( BlackBoxPin com, GraphicComActive gca, Vec2 pos, double angle ) {
		super( com, gca, pos, angle );
	}
	
	
	
	public EditorComponentBlackboxPin( BlackBoxPin com, GraphicComActive gca, Vec2 pos ) {
		this( com, gca, pos, gca.getAngle() );
	}
	
	
	
	public BlackBoxPin getComponent() {
		return (BlackBoxPin) super.getComponent();
	}
	
	
	
	public void setAngle( double angle ) {
		super.setAngle( snapAngle(angle) );
	}
	
	
	
	public void setPosAngle( Vec2 pos, double angle ) {
		super.setPosAngle( pos, snapAngle(angle) );
	}
	
	
	
	private double snapAngle( double angle ) {
		return Geo.roundToMultiple( angle % 360.0, 90 );
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
