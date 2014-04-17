


package logicBox.sim.component;

import logicBox.gui.editor.GraphicComActive;
import logicBox.gui.editor.GraphicGen;



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
	
	
	
	public void update() {
		if ( ! updateClock())
			return;
		
		setQ( getPinD().getState() );
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































