


package logicBox.sim;



public class TestHalfAdder
{
	public static void main( String[] args ) {
		SourceToggle sourceA = new SourceToggle( false );
		SourceToggle sourceB = new SourceToggle( false );
		
		GateXor gateXor = new GateXor();
		GateAnd gateAnd = new GateAnd( 2 );
		
		Led ledSum   = new Led();
		Led ledCarry = new Led();
		
		Trace aToXor   = connect( sourceA, 0, gateXor, 0 );
		Trace bToXor   = connect( sourceB, 0, gateXor, 1 );
		Trace xorToLed = connect( gateXor, 0, ledSum,  0 );
		
		Interconnect icA = Simulation.insertInterconnect( aToXor );
		Interconnect icB = Simulation.insertInterconnect( bToXor );
		
		Trace aToAnd   = connect( icA, gateAnd, 0 );
		Trace bToAnd   = connect( icB, gateAnd, 1 );
		Trace andToLed = connect( gateXor, 0, ledCarry, 0 );
		
		System.out.println( "Okay" );
	}
	
	
	
	public static Trace connect( PinOut outComp, int outPinIndex, PinIn inComp, int inPinIndex ) {
		Pin pinOut = outComp.getPinOutputs().get( outPinIndex );
		Pin pinIn  = inComp .getPinInputs() .get( inPinIndex  );

		Trace trace = new Trace( pinOut, pinIn );
		
		pinOut.connectTrace( trace );
		pinIn .connectTrace( trace );
		
		return trace; 
	}
	
	
	
	public static Trace connect( Interconnect outIc, PinIn inComp, int inPinIndex ) {
		Pin pinOut = outIc.createPin();
		Pin pinIn  = inComp.getPinInputs().get( inPinIndex );

		Trace trace = new Trace( pinOut, pinIn );
		
		pinOut.connectTrace( trace );
		pinIn .connectTrace( trace );
		
		return trace; 
	}
}
