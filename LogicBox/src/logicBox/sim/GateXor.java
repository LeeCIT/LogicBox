


package logicBox.sim;



/**
 * A gate which outputs true if its inputs are different.
 * @author Lee Coakley
 */
public class GateXor extends Gate
{
	public GateXor() {
		super( 2 );
	}
	
	
	
	public void update() {
		boolean a = pinInputs.get( 0 ).getState();
		boolean b = pinInputs.get( 1 ).getState();
		
		pinOut.setState( a ^ b );
	}
}
