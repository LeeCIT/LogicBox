


package logicBox.gui.editor.graphics;
import java.io.Serializable;
import logicBox.sim.component.PinIoMode;
import logicBox.util.Line2;
import logicBox.util.Vec2;



/**
 * Maps a pin's graphical representation to an IoMode and pin index.
 * Allows the graphical display to be mapped onto the simulator.
 * @author Lee Coakley
 */
public class GraphicPinMapping implements Serializable
{
	private static final long serialVersionUID = 1L;
	
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
	
	
	
	public String toString() {
		return getClass().getSimpleName() + ": " + mode + " " + index + " " + line;
	}
}
