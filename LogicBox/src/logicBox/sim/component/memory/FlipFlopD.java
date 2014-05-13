


package logicBox.sim.component.memory;

import logicBox.gui.editor.graphics.GraphicComActive;
import logicBox.gui.editor.graphics.GraphicGen;
import logicBox.sim.component.ComponentType;
import logicBox.sim.component.connective.Pin;



/**
 * D-type flip-flop. 
 * @author Lee Coakley
 */
public class FlipFlopD extends FlipFlop
{
	private static final long serialVersionUID = 1L;
	
	
	
	public FlipFlopD() {
		super( 2 );
	}
	
	
	
	public Pin getPinD() {
		return pinInputs.get( 0 );
	}
	
	
	
	public Pin getPinClock() {
		return pinInputs.get( 1 );
	}
	
	
	
	protected boolean evaluateNextQ() {
		return getPinD().getState();
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.flipFlopD;
	}
	
	
	
	public String getName() {
		return "D flip-flop";
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateFlipFlopD();
	}
}































