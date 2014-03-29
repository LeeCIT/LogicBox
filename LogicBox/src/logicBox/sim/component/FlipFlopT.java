


package logicBox.sim.component;



/**
 * T-type flip-flop.
 * Toggles on rising edge if T is high.
 * @author Lee Coakley
 */
public class FlipFlopT extends FlipFlop
{
	private static final long serialVersionUID = 1L;
	
	
	
	public FlipFlopT() {
		super( 2 );
	}
	
	
	
	public Pin getPinClock() {
		return getPinInput( 1 );
	}
	
	
	
	public Pin getPinT() {
		return getPinInput( 0 );
	}
	
	
	
	public void update() {
		if (updateClock())		
			if (getPinT().getState())
				setQ( ! getPinQ().getState() );
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.flipFlopT;
	}
	
	
	
	public String getName() {
		return "T flip-flop";
	}
}































