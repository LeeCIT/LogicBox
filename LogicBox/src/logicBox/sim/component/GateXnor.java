


package logicBox.sim.component;



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
	public GateXnor() {
		super( 2 );
	}
	
	
	
	public GateXnor( int inputPinCount ) {
		super( inputPinCount );
	}
	
	
	
	public boolean evaluate() {
		return ! evaluate();
	}
	
	
	
	public String getName() {
		return "XNOR gate";
	}
}
