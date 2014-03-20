


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
	private static final long serialVersionUID = 1L;
	
	
	
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
