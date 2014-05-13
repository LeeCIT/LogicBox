


package logicBox.gui.editor;

import logicBox.gui.editor.graphics.EditorStyle;
import logicBox.gui.editor.graphics.GraphicComActive;
import logicBox.sim.component.SourceToggle;
import logicBox.util.CallbackParam;
import logicBox.util.Vec2;



/**
 * Ecom for the switchable source.
 * @author Lee Coakley
 */
public class EditorComponentToggle extends EditorComponentActive
{
	private static final long serialVersionUID = 1L;
	
	
	
	public EditorComponentToggle( SourceToggle com, GraphicComActive gca, Vec2 pos, double angle ) {
		super( com, gca, pos, angle );
	}
	
	
	
	public EditorComponentToggle( SourceToggle com, GraphicComActive gca, Vec2 pos ) {
		this( com, gca, pos, gca.getAngle() );
	}
	
	
	
	public SourceToggle getComponent() {
		return (SourceToggle) super.getComponent();
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
