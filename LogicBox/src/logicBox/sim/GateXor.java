


package logicBox.sim;



/**
 * Outputs true if its inputs are different.
 * 0,0 -> 0
 * 0,1 -> 1
 * 1,0 -> 1
 * 1,1 -> 0
 * @author Lee Coakley
 */
public class GateXor extends Gate
{
	public GateXor() {
		super( 2 );
	}
	
	
	
	public boolean evaluate() {
		boolean a = pinInputs.get( 0 ).getState();
		boolean b = pinInputs.get( 1 ).getState();
		
		return a ^ b;
	}
	
	
	
	public String getName() {
		return "Xor gate";
	}
}
