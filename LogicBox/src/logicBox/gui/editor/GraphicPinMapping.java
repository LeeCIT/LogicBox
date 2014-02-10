


package logicBox.gui.editor;
import logicBox.sim.PinIoMode;
import logicBox.util.Vec2;



/**
 * Maps a pin's graphical representation to an IoMode and pin index.
 * Allows the graphical display to be mapped onto the simulator.
 * @author Lee Coakley
 */
public class GraphicPinMapping
{
	public Vec2      pos;
	public PinIoMode mode;
	public int       index;
	
	
	
	public GraphicPinMapping( Vec2 pos, PinIoMode isInput, int index ) {
		this.pos   = pos;
		this.mode  = isInput;
		this.index = index;
	}
}
