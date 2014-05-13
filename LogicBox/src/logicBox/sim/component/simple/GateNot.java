


package logicBox.sim.component.simple;

import logicBox.gui.editor.graphics.GraphicComActive;
import logicBox.gui.editor.graphics.GraphicGen;
import logicBox.sim.component.ComponentType;



/**
 * Inverter gate.
 * 0 -> 1
 * 1 -> 0
 * @author Lee Coakley
 */
public class GateNot extends GateBuffer
{
	private static final long serialVersionUID = 1L;
	
	
	
	public GateNot() {
		super();
	}
	
	
	
	public boolean evaluate() {
		return ! super.evaluate();
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.gateNot;
	}



	public String getName() {
		return "NOT gate";
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateGateNot();
	}
}
