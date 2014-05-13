


package logicBox.sim.component.simple;

import logicBox.gui.editor.graphics.GraphicComActive;
import logicBox.gui.editor.graphics.GraphicGen;
import logicBox.sim.component.ComponentType;
import logicBox.sim.component.connective.Pin;



/**
 * Gate which outputs true if any of its inputs are true.
 * 0,0 -> 0
 * 0,1 -> 1
 * 1,0 -> 1
 * 1,1 -> 1
 * @author Lee Coakley
 */
public class GateOr extends Gate
{
	private static final long serialVersionUID = 1L;
	
	
	
	public GateOr() {
		super( 2 );
	}
	
	
	
	public GateOr( int inputPinCount ) {
		super( inputPinCount );
	}
	
	
	
	public boolean evaluate() {
		boolean state = false;
		
		for (Pin pin: getPinInputs())
			state |= pin.getState();
		
		return state;
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.gateOr;
	}
	
	
	
	public String getName() {
		return "" + getPinInputCount() + "-input OR gate";
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateGateOr( getPinInputCount() );
	}
}
