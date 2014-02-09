


package logicBox.sim;



/**
 * A gate which outputs false if all its inputs are true.
 * @author Lee Coakley
 */
public class GateNand extends GateAnd
{
	public GateNand() {
		super();
	}
	
	
	
	public GateNand( int inputPinCount ) {
		super( inputPinCount );
	}
	
	
	
	public void update() {
		pinOut.setState( ! evaluate() );
	}
}
