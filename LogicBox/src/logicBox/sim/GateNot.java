


package logicBox.sim;



/**
 * Inverter gate.
 * 0 -> 1
 * 1 -> 0
 * @author Lee Coakley
 */
public class GateNot extends GateRelay
{
	public GateNot() {
		super();
	}
	
	
	
	public boolean evaluate() {
		return ! super.evaluate();
	}



	public String getName() {
		return "Not gate";
	}
}
