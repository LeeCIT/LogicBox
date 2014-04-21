


package logicBox.gui.editor.toolbox;

import logicBox.gui.editor.Graphic;
import logicBox.gui.editor.GraphicJunction;
import logicBox.util.Vec2;



/**
 * A button specialised for use in the Toolbox class.
 * @author Lee Coakley
 */
public class ToolboxButtonJunction extends ToolboxButtonGraphic
{
	public ToolboxButtonJunction() {
		super( makeGraphic(), "Junction" );
		setScaleAux( 0.75 );
	}
	
	
	
	private static Graphic makeGraphic() {
		Graphic graphic = new GraphicJunction( new Vec2(0) );
		graphic.setInverted( true );
		return graphic;
	}
}























