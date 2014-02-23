


package logicBox.sim.component;

import java.util.ArrayList;
import java.util.List;
import logicBox.sim.SimUtil;



/**
 * JK-type flip-flop.
 * A 1-bit memory with inputs [J, C, K] and outputs [Q, !Q].
 * There are four modes of operation:
 * 		J K C | Q
 * 		---------
 * 		x x 0 | No change
 * 		0 0 1 | No change
 * 		0 0 1 | Set Q to 0
 * 		1 0 1 | Set Q to 1
 * 		1 1 1 | Toggle Q
 * 
 * @author Lee Coakley
 */
public class FlipFlopJK extends ComponentActive
{
	private List<Pin> pinInputs;
	private List<Pin> pinOutputs;
	
	
	
	public FlipFlopJK() {
		super();
		pinInputs  = new ArrayList<>();
		pinOutputs = new ArrayList<>();
		SimUtil.addPins( pinInputs,  this, PinIoMode.input,  3 );
		SimUtil.addPins( pinOutputs, this, PinIoMode.output, 2 );
		getPinQinv().setState( true ); // Ensure valid initial state
	}
	
	
	
	public List<Pin> getPinInputs() {
		return pinInputs;
	}
	
	
	
	public List<Pin> getPinOutputs() {
		return pinOutputs;
	}
	
	
	
	public Pin getPinJ() {
		return pinInputs.get( 0 );
	}
	
	
	
	public Pin getPinC() {
		return pinInputs.get( 1 );
	}
	
	
	
	public Pin getPinK() {
		return pinInputs.get( 2 );
	}
	
	
	
	public Pin getPinQ() {
		return pinOutputs.get( 0 );
	}
	
	
	
	public Pin getPinQinv() {
		return pinOutputs.get( 1 );
	}
	
	
	
	public void update() {
		if ( ! getPinC().getState())
			return;
		
		boolean j = getPinJ().getState();
		boolean k = getPinK().getState();
		boolean state;
		
		if (j && k)
			 state = ! getPinQ().getState();
		else state = j;
		
		getPinQ   ().setState(   state );
		getPinQinv().setState( ! state );
	}
	
	
	
	public String getName() {
		return "JK flip-flop";
	}
	
	
	
	
	
	public static void main( String[] args ) {
		FlipFlopJK f = new FlipFlopJK();
		
		// TODO test
	}
}































