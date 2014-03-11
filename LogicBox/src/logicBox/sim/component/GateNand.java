


package logicBox.sim.component;

import logicBox.gui.editor.GraphicComActive;
import logicBox.gui.editor.GraphicGen;



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
	
	
	
	public String getName() {
		return "NAND gate";
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateGateNand( getPinInputCount() );
	}
}
