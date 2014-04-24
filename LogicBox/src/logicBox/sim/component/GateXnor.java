


package logicBox.sim.component;

import logicBox.gui.editor.GraphicComActive;
import logicBox.gui.editor.GraphicGen;



/**
 * Outputs high if an even number of inputs are high.
 * 0,0 -> 1
 * 0,1 -> 0
 * 1,0 -> 0
 * 1,1 -> 1
 * @author Lee Coakley
 */
public class GateXnor extends GateXor
{
	private static final long serialVersionUID = 1L;
	
	
	
	public GateXnor() {
		super( 2 );
	}
	
	
	
	public GateXnor( int inputPinCount ) {
		super( inputPinCount );
	}
	
	
	
	public boolean evaluate() {
		return ! super.evaluate();
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.gateXnor;
	}
	
	
	
	public String getName() {
		return "" + getPinInputCount() + "-input XNOR gate";
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateGateXnor( getPinInputCount() );
	}
}
