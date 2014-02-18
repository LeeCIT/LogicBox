


package logicBox.sim;



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
}
