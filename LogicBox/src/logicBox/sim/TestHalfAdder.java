


package logicBox.sim;



public class TestHalfAdder
{
	public static void main( String[] args ) {
		Source sourceA = new SourceFixed( false );
		Source sourceB = new SourceFixed( true  );
		
		Gate gateXor = new GateXor();
		Gate gateAnd = new GateAnd( 2 );
		
		Led ledSum   = new Led();
		Led ledCarry = new Led();
		
		Trace aToXor   = Simulation.connect( sourceA, 0, gateXor, 0 );
		Trace bToXor   = Simulation.connect( sourceB, 0, gateXor, 1 );
		Trace xorToLed = Simulation.connect( gateXor, 0, ledSum,  0 );
		
		Junction juncA = Simulation.insertJunction( aToXor );
		Junction juncB = Simulation.insertJunction( bToXor );
		
		Trace aToAnd   = Simulation.connect( juncA, gateAnd, 0 );
		Trace bToAnd   = Simulation.connect( juncB, gateAnd, 1 );
		Trace andToLed = Simulation.connect( gateXor, 0, ledCarry, 0 );
		
		Simulation sim = new Simulation();
		sim.addSource( sourceA );
		sim.addSource( sourceB );
		Simulation.AffectedPathSet set = sim.getAffectedPath( sourceA.getPinOutputs().get(0) );
		
		System.out.println(
			"Junctions: " + set.junctions     .size() + "\n" +
			"Pins:      " + set.pins          .size() + "\n" +
			"PinTerms:  " + set.pinTerminators.size() + "\n" + 
			"Traces:    " + set.traces        .size()
		);
		
		System.out.println( "Sum:   " + ledSum  .getState() );
		System.out.println( "Carry: " + ledCarry.getState() );
	}
}