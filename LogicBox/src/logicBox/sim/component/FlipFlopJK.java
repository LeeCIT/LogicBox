


package logicBox.sim.component;

import java.util.Arrays;
import logicBox.gui.editor.GraphicComActive;
import logicBox.gui.editor.GraphicGen;



/**
 * JK-type flip-flop.
 * A 1-bit memory with inputs [J, C, K] and outputs [Q, !Q].
 * J sets, K resets.  J+K toggles.
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
public class FlipFlopJK extends FlipFlop
{
	private static final long serialVersionUID = 1L;
	
	
	
	public FlipFlopJK() {
		super( 3 );
	}
	
	
	
	public Pin getPinClock() {
		return getPinInput( 1 );
	}
	
	
	
	public Pin getPinJ() {
		return getPinInput( 0 );
	}
	
	
	
	public Pin getPinK() {
		return getPinInput( 2 );
	}
	
	
	
	protected boolean evaluateNextQ() {
		boolean j = getPinJ().getState();
		boolean k = getPinK().getState();
		
		if ( ! (j || k)) {
			return false;
		} else {
			if      (j && k) return ! getPinQ().getState();
			else if (j)      return true;
			else  /* k */    return false;
		}
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.flipFlopJK;
	}
	
	
	
	public String getName() {
		return "JK flip-flop";
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateFlipFlopJK(); 
	}
	
	
	
	
	
	public static void main( String[] args ) {
		FlipFlopJK f = new FlipFlopJK();
		
		int[] patternExpected = {
			1,1,0,0,1,1,0,0, // Toggle
			0,0,0,0,0,0,0,0, // Set 0
			1,1,1,1,1,1,1,1, // Set 1
			1,1,1,1,1,1,1,1, // No change
			1,1,1,1,1,1,1,1  // No change
		};
		
		int[] patternActual = new int[ patternExpected.length ];
		
		cycle( f, patternActual,  0, true,  true,  true,  4 );
		cycle( f, patternActual,  8, false, true,  true,  4 );
		cycle( f, patternActual, 16, true,  false, true,  4 );
		cycle( f, patternActual, 24, true,  false, false, 4 );
		cycle( f, patternActual, 32, false, false, true,  4 );
		
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
				f.getPinClock().setState( clockSignal );
			
			f.update();
			
			int v = f.getPinQ().getState() ? 1 : 0;
			pattern[ offset + i ] = v;
			System.out.print( v );
		}
	}
}































