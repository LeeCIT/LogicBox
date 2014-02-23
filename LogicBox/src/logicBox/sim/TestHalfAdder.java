


package logicBox.sim;

import logicBox.sim.component.DisplayLed;
import logicBox.sim.component.Gate;
import logicBox.sim.component.GateAnd;
import logicBox.sim.component.GateXor;
import logicBox.sim.component.Junction;
import logicBox.sim.component.Source;
import logicBox.sim.component.SourceFixed;
import logicBox.sim.component.Trace;



public class TestHalfAdder
{
	public static void main( String[] args ) {
		Source sourceA = new SourceFixed( false );
		Source sourceB = new SourceFixed( true  );
		
		Gate gateXor = new GateXor();
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
		sim.addSource( sourceA );
		sim.addSource( sourceB );
		sim.run();
		
		System.out.println( "Sum:   " + ledSum  .getState() );
		System.out.println( "Carry: " + ledCarry.getState() );
	}
}