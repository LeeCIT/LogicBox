


package logicBox.sim;



/**
 * Duplicates its input.
 * 0 -> 0
 * 1 -> 1
 * @author Lee Coakley
 */
public class GateRelay extends Gate
{
	public GateRelay() {
		super( 1 );
	}
	
	
	
	public boolean evaluate() {
		return getPinInputs().get(0).getState();
	}



	public String getName() {
		return "Relay gate";
	}
}
