


package logicBox.sim;



public class HalfAdder
{
	public static void main( String[] args ) {
		SourceToggle a = new SourceToggle( false );
		SourceToggle b = new SourceToggle( false );
		
		GateXor xor = new GateXor();
		GateAnd and = new GateAnd( 2 );
		
		Solder sa = new Solder();
		Solder sb = new Solder();
		
		Led sum   = new Led();
		Led carry = new Led();
		
		Trace aToXor = connectPins( a.pinOut, xor.pinInputs.get(0) );
		Trace bToXor = connectPins( a.pinOut, xor.pinInputs.get(1) );
	}
	
	
	
	public static Trace connectPins( Pin a, Pin b ) {
		Trace trace = new Trace( a, b );
		a.connectPin( trace );
		b.connectPin( trace );
		return trace;
	}
}
