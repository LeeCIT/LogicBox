


package logicBox.sim.component.simple;

import logicBox.gui.editor.graphics.GraphicComActive;
import logicBox.gui.editor.graphics.GraphicGen;
import logicBox.sim.component.ComponentType;



/**
 * Duplicates its input.
 * 0 -> 0
 * 1 -> 1
 * @author Lee Coakley
 */
public class GateBuffer extends Gate
{
	private static final long serialVersionUID = 1L;
	
	
	
	public GateBuffer() {
		super( 1 );
	}
	
	
	
	public boolean evaluate() {
		return getPinInputs().get(0).getState();
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.gateBuffer;
	}
	
	
	
	public String getName() {
		return "Buffer";
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateGateBuffer();
	}
}
