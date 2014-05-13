


package logicBox.sim;

import logicBox.sim.component.*;
import logicBox.sim.component.memory.FlipFlopJK;
import logicBox.sim.component.simple.SourceToggle;



/**
 * Tests the JK flip-flop.
 * Doesn't use the sim - this is a pure component test.
 * @author Lee Coakley
 */
public class TestFlipFlopJK
{
	public static void main( String[] args ) {
		SourceToggle osc = new SourceToggle( false );
		
		FlipFlopJK[] ffs = {
			new FlipFlopJK(),
			new FlipFlopJK(),
			new FlipFlopJK(),
			new FlipFlopJK()
		};
		
		for (FlipFlopJK f: ffs) {
			f.getPinJ().setState( true );
			f.getPinK().setState( true );
		}
		
		for (int i=0; i<64; i++) {
			for (int r=0; r<2; r++) {
				ffs[0].getPinClock().setState( osc.getState() );
				ffs[0].update();
				
				for (int z=0; z<ffs.length-1; z++) {
					FlipFlopJK cur  = ffs[ z     ];
					FlipFlopJK next = ffs[ z + 1 ];
					
					next.getPinClock().setState( cur.getPinQ().getState() );
					next.update();
				}
				
				osc.toggleState();
			}
			
			int value = 0;
			for (int z=ffs.length-1; z>=0; z--) {
				int level = ffs[z].getPinQ().getState() ? 1 : 0;
				value |= level << z;
			}
			
			System.out.println( ~value & 0xF ); // Count up instead of down, by complement, then chop irrelevant bits
		}
	}
}
