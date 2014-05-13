


package logicBox.gui.editor.components;

import java.awt.Color;
import logicBox.gui.editor.graphics.EditorStyle;
import logicBox.gui.editor.graphics.GraphicComActive;
import logicBox.sim.component.complex.DisplayLed;
import logicBox.util.Vec2;



/**
 * Specialised for the LED.
 * @author Lee Coakley
 */
public class EditorComponentLed extends EditorComponentActive
{
	private static final long serialVersionUID = 1L;
	
	private DisplayLed com;
	
	
	public EditorComponentLed( DisplayLed com, GraphicComActive gca, Vec2 pos, double angle ) {
		super( com, gca, pos, angle );
		this.com = com;
		onWorldChange();
	}
	
	
	
	public DisplayLed getComponent() {
		return com;
	}
	
	
	
	public void onWorldChange() {
		boolean lit = com.isLit();
		Color   col = (lit) ? EditorStyle.colLedOn : EditorStyle.colLedOff;

		getGraphic().setFillOverride( true, col );
	}
}
