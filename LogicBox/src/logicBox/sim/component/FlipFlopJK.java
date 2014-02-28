


package logicBox.sim.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import logicBox.sim.LogicLevel;
import logicBox.sim.SimUtil;



/**
 * JK-type flip-flop.
 * A 1-bit memory with inputs [J, C, K] and outputs [Q, !Q].
 * J sets, K resets.  J+K toggles.
 * Edge triggered: changes state only at the rising edge of a clock pulse.
 * Table:
 * 		J K C | Q
 * 		---------
 * 		x x x | Latch
 * 		0 0 ^ | Latch
 * 		0 1 ^ | Q = 0
 * 		1 0 ^ | Q = 1
 * 		1 1 ^ | Q = !Q
 * 
 * @author Lee Coakley
 */
public class FlipFlopJK extends ComponentActive
{
	private List<Pin> pinInputs;
	private List<Pin> pinOutputs;
	private boolean   lastClock;
	
	
	
	public FlipFlopJK() {
		super();
		pinInputs  = new ArrayList<>();
		pinOutputs = new ArrayList<>();
		SimUtil.addPins( pinInputs,  this, PinIoMode.input,  3 );
		SimUtil.addPins( pinOutputs, this, PinIoMode.output, 2 );
		
		lastClock = false;
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
		boolean clock     = getPinC().getState();
		boolean isEdgePos = LogicLevel.edgePos == LogicLevel.toLogicLevel( lastClock, clock );
		lastClock = clock;
		
		if ( ! isEdgePos)
			return;
		
		boolean j = getPinJ().getState();
		boolean k = getPinK().getState();
		boolean state;
		
		if      (j && k) state = ! getPinQ().getState();
		else if (j)      state = true;
		else /*k*/       state = false;
		
		getPinQ   ().setState(   state );
		getPinQinv().setState( ! state );
	}
	
	
	
	public String getName() {
		return "JK flip-flop";
	}
	
	
	
	
	
	public static void main( String[] args ) {
		FlipFlopJK f = new FlipFlopJK();
		
		int[] patternExpected = {
			1,1,0,0,1,1,0,0, // Toggle
			0,0,0,0,0,0,0,0, // Set 0
			1,1,1,1,1,1,1,1, // Set 1
			1,1,1,1,1,1,1,1  // No change
		};
		
		int[] patternActual = new int[ patternExpected.length ];
		
		cycle( f, patternActual,  0, true,  true,  true,  4 );
		cycle( f, patternActual,  8, false, true,  true,  4 );
		cycle( f, patternActual, 16, true,  false, true,  4 );
		cycle( f, patternActual, 24, true,  false, false, 4 );
		
		boolean good = Arrays.equals( patternExpected, patternActual );
		
		System.out.print( "\nTest " + (good ? "OK" : "Failed") );
	}
	
	
	
	private static void cycle( FlipFlopJK f, int[] pattern, int offset, boolean J, boolean K, boolean clockIO, int cycles ) {
		System.out.print( "\nQ: " );
		
		f.getPinJ().setState( J );
		f.getPinK().setState( K );
		
		for (int i=0; i<cycles*2; i++) {
			boolean clockSignal = (i & 0b1) == 0;
			
			if (clockIO)
				f.getPinC().setState( clockSignal );
			
			f.update();
			
			int v = f.getPinQ().getState() ? 1 : 0;
			pattern[ offset + i ] = v;
			System.out.print( v );
		}
	}
}































