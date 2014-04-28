


package logicBox.sim.component;

import logicBox.gui.editor.GraphicComActive;
import logicBox.gui.editor.GraphicGen;
import logicBox.sim.LogicLevel;



/**
 * T-type flip-flop.
 * Toggles on rising edge if T is high.
 * @author Lee Coakley
 */
public class FlipFlopT extends FlipFlop
{
	private static final long serialVersionUID = 1L;
	
	
	
	public FlipFlopT() {
		super( 2 );
	}
	
	
	
	public Pin getPinT() {
		return getPinInput( 0 );
	}
	
	
	
	public Pin getPinClock() {
		return getPinInput( 1 );
	}
	
	
	
	protected boolean evaluateNextQ() {
		boolean t = getPinT().getState();
		boolean q = getPinQ().getState();
		
		return (t) ? !q : q;
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.flipFlopT;
	}
	
	
	
	public String getName() {
		return "T flip-flop";
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateFlipFlopT();
	}
}































