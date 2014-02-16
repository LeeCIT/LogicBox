


package logicBox.sim;



/**
 * Outputs true if its inputs are the same.
 * 0,0 -> 1
 * 0,1 -> 0
 * 1,0 -> 0
 * 1,1 -> 1
 * @author Lee Coakley
 */
public class GateXnor extends GateXor
{
	public GateXnor() {
		super();
	}
	
	
	
	public boolean evaluate() {
		return ! evaluate();
	}
	
	
	
	public String getName() {
		return "Xnor gate";
	}
}
