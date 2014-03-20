


package logicBox.sim;

import logicBox.sim.component.*;



/**
 * Simulation of a 4-bit binary counter using T flip-flops.
 * @author Lee Coakley
 */
public class TestCounter
{
	public static void main( String[] args ) {
		int bits = 4;
		
		SourceToggle    source = new SourceToggle( false );
		SourceFixed[]   ones   = new SourceFixed[bits];
		FlipFlopT  []   ffts   = new FlipFlopT  [bits];
		DisplaySevenSeg dss    = new DisplaySevenSeg();
		
		for (int i=0; i<bits; i++) {
			ones[i] = new SourceFixed( true );
			ffts[i] = new FlipFlopT();
			Simulation.connectPins( ones[i].getPinOutput(0), ffts[i].getPinT()      ); // 1 -> T
			Simulation.connectPins( ffts[i].getPinQinv(),    dss    .getPinInput(i) ); // !Q -> Display[i]
		}
		
		for (int i=0; i<bits-1; i++)
			Simulation.connectPins( ffts[i].getPinQ(), ffts[i+1].getPinClock() ); //  Q -> Clock
		
		Simulation.connect( source, 0, ffts[0], 1 ); // SRC -> Clock
		
		
		
		Simulation sim = new Simulation();
		sim.add( source );
		sim.add( ones   );
		sim.add( ffts   );
		sim.add( dss    );
		
		
		System.out.println( "isLevelisable: " + sim.isLevelisable() );
		System.out.println( "isOptimisable: " + sim.isOptimisable() );
		System.out.println();
		
		for (int i=0; i<1<<20; i++) {
			sim.simulate();
			source.toggleState();
			
			System.out.println( dss.getNumber() );
			
			try {
				Thread.sleep( 100 );
			}
			catch (InterruptedException ex) {
				Thread.interrupted();
			}
		}
	}
}
