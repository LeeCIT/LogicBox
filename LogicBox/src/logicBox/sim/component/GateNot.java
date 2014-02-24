


package logicBox.sim.component;

import logicBox.gui.editor.GraphicComActive;
import logicBox.gui.editor.GraphicGen;



/**
 * Inverter gate.
 * 0 -> 1
 * 1 -> 0
 * @author Lee Coakley
 */
public class GateNot extends GateBuffer
{
	public GateNot() {
		super();
	}
	
	
	
	public boolean evaluate() {
		return ! super.evaluate();
	}



	public String getName() {
		return "NOT gate";
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateGateNot();
	}
}
