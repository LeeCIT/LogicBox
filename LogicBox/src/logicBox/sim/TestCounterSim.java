


package logicBox.sim;

import logicBox.sim.component.*;



/**
 * Simulation of a 4-bit binary counter using T flip-flops.
 * @author Lee Coakley
 */
public class TestCounterSim
{
	public static void main( String[] args ) {
		int bits = 4;
		
		SourceToggle    source = new SourceToggle( false );
		SourceFixed[]   one    = new SourceFixed[bits];
		FlipFlopT  []   fft    = new FlipFlopT  [bits];
		DisplaySevenSeg dss    = new DisplaySevenSeg();
		
		for (int i=0; i<bits; i++) {
			one[i] = new SourceFixed( true );
			fft[i] = new FlipFlopT();
			Simulation.connectPins( one[i].getPinOutput(0), fft[i].getPinT()      ); // 1 -> T
			Simulation.connectPins( fft[i].getPinQinv(),    dss   .getPinInput(i) ); // !Q -> Display[i]
		}
		
		for (int i=0; i<bits-1; i++)
			Simulation.connectPins( fft[i].getPinQ(), fft[i+1].getPinClock() ); //  Q -> Clock
		
		Simulation.connect( source, 0, fft[0], 1 ); // SRC -> Clock
		
		
		
		Simulation sim = new Simulation();
		sim.add( source );
		sim.add( one    );
		sim.add( fft    );
		sim.add( dss    );
		
		sim.add( new GateXor (2) );
		sim.add( new GateNand(2) );
		
		Gate gateCA = new GateXnor(2);
		Gate gateCB = new GateNor (2);
		Simulation.connect( gateCA, 0, gateCB, 0 );
		
		sim.add( gateCA );
		sim.add( gateCB );
		
		for (Island island: sim.getIslands()) {
			System.out.print( "\n\n==== ISLAND =====" );
			
			for (ComponentActive com: island)
				System.out.print( "\n" + com );
		}
		System.out.println( "\n\n" );
		
		
		System.out.println( "isLevelisable: " + sim.isLevelisable() );
		System.out.println( "isOptimisable: " + sim.isOptimisable() );
		System.out.println();
		
		
		for (int i=0; i<1024; i++) {
			sim.simulate();
			source.toggleState();
			
			System.out.println( dss.getNumber() );
			
			try {
				Thread.sleep( 100 );
			}
			catch (InterruptedException e) {
				Thread.interrupted();
			}
		}
	}
}
