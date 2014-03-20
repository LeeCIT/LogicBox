


package logicBox.sim;

import logicBox.sim.component.*;



/**
 * An unstable circuit where an XOR has been fed into itself.
 * The simulator will never handle this kind of circuit, but it's useful to see what happens.
 * @author Lee Coakley
 */
public class TestUnstableLoop
{
	public static void main( String[] args ) {
		Source sourceA    = new SourceFixed( true );
		Gate   gateXorA   = new GateXor( 2 );
		Gate   gateXorB   = new GateXor( 2 );
		Trace  aToXorA    = Simulation.connect( sourceA,  0, gateXorA, 0 );
		Trace  xorAToXorB = Simulation.connect( gateXorA, 0, gateXorB, 0 );
		Trace  xorBToXorA = Simulation.connect( gateXorB, 0, gateXorA, 1 );
		
		Simulation sim = new Simulation();
		sim.add( gateXorA );
		sim.add( gateXorB );
		sim.add( sourceA  );
		
		System.out.println( "isLevelisable: " + sim.isLevelisable() );
		System.out.println( "isOptimisable: " + sim.isOptimisable() );
		System.out.println();
		
		sim.simulate();
	}
}













