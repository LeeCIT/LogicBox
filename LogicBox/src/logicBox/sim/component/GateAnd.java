


package logicBox.sim.component;

import logicBox.gui.editor.GraphicComActive;
import logicBox.gui.editor.GraphicGen;




/**
 * A gate which outputs true if all its inputs are true.
 * 0,0 -> 0
 * 0,1 -> 0
 * 1,0 -> 0
 * 1,1 -> 1
 * @author Lee Coakley
 */
public class GateAnd extends Gate
{
	public GateAnd() {
		super( 2 );
	}
	
	
	
	public GateAnd( int inputPinCount ) {
		super( inputPinCount );
	}
	
	
	
	public boolean evaluate() {
		boolean state = true;
		
		for (Pin pin: pinInputs)
			state &= pin.getState();
			
		return state;
	}
	
	
	
	public String getName() {
		return "AND gate";
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateGateAnd( getPinInputCount() );
	}
}
