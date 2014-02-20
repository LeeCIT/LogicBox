


package logicBox.gui.editor;
import logicBox.sim.component.PinIoMode;
import logicBox.util.Line2;
import logicBox.util.Vec2;



/**
 * Maps a pin's graphical representation to an IoMode and pin index.
 * Allows the graphical display to be mapped onto the simulator.
 * Positions here are in the graphics's local coordinate space, centred on [0,0].
 * @author Lee Coakley
 */
public class GraphicPinMapping
{
	public Line2     line;
	public PinIoMode mode;
	public int       index;
	
	
	
	public GraphicPinMapping( Line2 line, PinIoMode mode, int index ) {
		this.line  = line;
		this.mode  = mode;
		this.index = index;
	}
	
	
	
	public Vec2 getPinPosBody() {
		return line.a;
	}
	
	
	
	public Vec2 getPinPosEnd() {
		return line.b;
	}
}
