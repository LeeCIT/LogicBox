


package logicBox.sim.component.simple;

import logicBox.gui.editor.graphics.GraphicComActive;
import logicBox.gui.editor.graphics.GraphicGen;
import logicBox.sim.component.ComponentType;



/**
 * Inverted AND gate.
 * 0,0 -> 1
 * 0,1 -> 1
 * 1,0 -> 1
 * 1,1 -> 0
 * @author Lee Coakley
 */
public class GateNand extends GateAnd
{
	private static final long serialVersionUID = 1L;
	
	
	
	public GateNand() {
		super( 2 );
	}
	
	
	
	public GateNand( int inputPinCount ) {
		super( inputPinCount );
	}
	
	
	
	public boolean evaluate() {
		return ! super.evaluate();
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.gateNand;
	}
	
	
	
	public String getName() {
		return "" + getPinInputCount() + "-input NAND gate";
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateGateNand( getPinInputCount() );
	}
}
