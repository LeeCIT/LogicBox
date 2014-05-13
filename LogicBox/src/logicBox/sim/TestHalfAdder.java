


package logicBox.sim;

import logicBox.sim.component.connective.*;
import logicBox.sim.component.simple.*;
import logicBox.sim.component.complex.*;



/**
 * Half-adder circuit used as simulation testbed.
 * @author Lee Coakley
 */
public class TestHalfAdder
{
	public static void main( String[] args ) {
		Source sourceA = new SourceFixed( false );
		Source sourceB = new SourceFixed( true  );
		
		Gate gateXor = new GateXor( 2 );
		Gate gateAnd = new GateAnd( 2 );
		
		DisplayLed ledSum   = new DisplayLed();
		DisplayLed ledCarry = new DisplayLed();
		
		Trace aToXor   = Simulation.connect( sourceA, 0, gateXor, 0 );
		Trace bToXor   = Simulation.connect( sourceB, 0, gateXor, 1 );
		Trace xorToLed = Simulation.connect( gateXor, 0, ledSum,  0 );
		
		Junction juncA = Simulation.insertJunction( aToXor );
		Junction juncB = Simulation.insertJunction( bToXor );
		
		Trace aToAnd   = Simulation.connect( juncA, gateAnd, 0 );
		Trace bToAnd   = Simulation.connect( juncB, gateAnd, 1 );
		Trace andToLed = Simulation.connect( gateAnd, 0, ledCarry, 0 );
		
		Simulation sim = new Simulation();
		sim.add( sourceB  );
		sim.add( ledSum   );
		sim.add( gateXor  );
		sim.add( ledCarry );
		sim.add( gateAnd  );
		sim.add( sourceA  );
		
		// Warm up for accurate timing
		for (int i=0; i<32; i++) {
			sim.simulate();
			sim.reset();
		}
		
		long start = System.nanoTime();
		sim.simulate();
		long end   = System.nanoTime();
		long time  = end - start;
		double ms  = time / 1_000_000.0;
		
		System.out.println( "\nSim time: " + ms + " ms\n" );
		
		
		System.out.println( "isLevelisable: " + sim.isLevelisable() );
		System.out.println( "isOptimisable: " + sim.isOptimisable() );
		System.out.println();
		
		
		for (int z=0; z<=1; z++)
		for (int i=0; i<=1; i++) {
			boolean a = (i==0) ? false : true;
			boolean b = (z==0) ? false : true;
			
			sourceA.setState( a );
			sourceB.setState( b );
			sim.simulate();
			
			System.out.println( "\n" + i + " + " + z + ":" );
			System.out.println( "Sum:   " + ledSum  .isLit() );
			System.out.println( "Carry: " + ledCarry.isLit() );
		}
	}
}













