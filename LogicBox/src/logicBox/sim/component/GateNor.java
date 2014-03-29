


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
		return "NOR gate";
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateGateNor( getPinInputCount() );
	}
}
