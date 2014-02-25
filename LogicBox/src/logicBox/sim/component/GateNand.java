


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
	public GateNand() {
		super( 2 );
	}
	
	
	
	public GateNand( int inputPinCount ) {
		super( inputPinCount );
	}
	
	
	
	public void update() {
		pinOut.setState( ! evaluate() );
	}
	
	
	
	public String getName() {
		return "NAND gate";
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateGateNand( getPinInputCount() );
	}
}
