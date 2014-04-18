


package logicBox.gui.editor.toolbox;

import java.util.ArrayList;
import java.util.List;
import logicBox.gui.editor.Graphic;
import logicBox.gui.editor.GraphicComActive;
import logicBox.gui.editor.GraphicTrace;
import logicBox.util.Util;
import logicBox.util.Vec2;



/**
 * A button specialised for use in the Toolbox class.
 * @author Lee Coakley
 */
public class ToolboxButtonTrace extends ToolboxButtonGraphic
{
	public ToolboxButtonTrace() {
		super( makeGraphic(), "Trace" );
	}
	
	
	
	private static Graphic makeGraphic() {
		List<Vec2> points = new ArrayList<>();
		points.add( new Vec2( -24,   0) );
		points.add( new Vec2(   0,  18) );
		points.add( new Vec2(  14, -18) );
		points.add( new Vec2(  28,   0) );
		
		GraphicTrace graphic = new GraphicTrace( points );
		graphic.setConnectedSource( true );
		graphic.setConnectedDest  ( true );
		return graphic;
	}
}























