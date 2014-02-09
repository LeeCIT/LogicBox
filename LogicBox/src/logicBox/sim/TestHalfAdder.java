


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
		
		Trace aToXor   = connect( sourceA, 0, gateXor, 0 );
		Trace bToXor   = connect( sourceB, 0, gateXor, 1 );
		Trace xorToLed = connect( gateXor, 0, ledSum,  0 );
		
		Junction juncA = Simulation.insertJunction( aToXor );
		Junction juncB = Simulation.insertJunction( bToXor );
		
		Trace aToAnd   = connect( juncA, gateAnd, 0 );
		Trace bToAnd   = connect( juncB, gateAnd, 1 );
		Trace andToLed = connect( gateXor, 0, ledCarry, 0 );
		
		Simulation sim = new Simulation();
		sim.addSource( sourceA );
		sim.addSource( sourceB );
		sim.run();
		
		System.out.println( "Sum:   " + ledSum  .getState() );
		System.out.println( "Carry: " + ledCarry.getState() );
	}
	
	
	
	public static Trace connect( PinOut outComp, int outPinIndex, PinIn inComp, int inPinIndex ) {
		Pin pinOut = outComp.getPinOutputs().get( outPinIndex );
		Pin pinIn  = inComp .getPinInputs() .get( inPinIndex  );
		return connectPins( pinOut, pinIn );
	}



	public static Trace connect( Junction outJunc, PinIn inComp, int inPinIndex ) {
		Pin pinOut = outJunc.createPin();
		Pin pinIn  = inComp.getPinInputs().get( inPinIndex );
		return connectPins( pinOut, pinIn );
	}
	
	
	
	public static Trace connectPins( Pin pinOut, Pin pinIn ) {
		Trace trace = new Trace( pinOut, pinIn );
		pinOut.connectTrace( trace );
		pinIn .connectTrace( trace );
		return trace;
	}
}