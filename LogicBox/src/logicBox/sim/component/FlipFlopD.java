


package logicBox.sim.component;



/**
 * D-type flip-flop. 
 * @author Lee Coakley
 */
public class FlipFlopD extends FlipFlop
{
	private static final long serialVersionUID = 1L;
	
	
	
	public FlipFlopD() {
		super( 2 );
	}
	
	
	
	public Pin getPinD() {
		return pinInputs.get( 0 );
	}
	
	
	
	public Pin getPinClock() {
		return pinInputs.get( 1 );
	}
	
	
	
	public void update() {
		if ( ! updateClock())
			return;
		
		setQ( getPinD().getState() );
	}
	
	
	
	public String getName() {
		return "D flip-flop";
	}
}































