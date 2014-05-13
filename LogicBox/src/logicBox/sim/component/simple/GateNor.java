


package logicBox.sim.component.simple;

import logicBox.gui.editor.graphics.GraphicComActive;
import logicBox.gui.editor.graphics.GraphicGen;
import logicBox.sim.component.ComponentType;



/**
 * Inverted OR gate.
 * 0,0 -> 1
 * 0,1 -> 0
 * 1,0 -> 0
 * 1,1 -> 0
 * @author Lee Coakley
 */
public class GateNor extends GateOr
{
	private static final long serialVersionUID = 1L;
	
	
	
	public GateNor() {
		super( 2 );
	}
	
	
	
	public GateNor( int inputPinCount ) {
		super( inputPinCount );
	}
	
	
	
	public boolean evaluate() {
		return ! super.evaluate();
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.gateNor;
	}
	
	
	
	public String getName() {
		return "" + getPinInputCount() + "-input NOR gate";
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateGateNor( getPinInputCount() );
	}
}
