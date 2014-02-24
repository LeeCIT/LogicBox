


package logicBox.sim.component;

import logicBox.gui.editor.GraphicComActive;
import logicBox.gui.editor.GraphicGen;



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
	public GateNor() {
		super( 2 );
	}
	
	
	
	public GateNor( int inputPinCount ) {
		super( inputPinCount );
	}
	
	
	
	public boolean evaluate() {
		return ! super.evaluate();
	}
	
	
	
	public String getName() {
		return "NOR gate";
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateGateNor( getPinInputCount() );
	}
}
