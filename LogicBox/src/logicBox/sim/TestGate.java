


package logicBox.sim;



public class TestGate
{
	public static void main( String[] args ) {
		GateXor xor = new GateXor();
		
		xor.getPinInputs().get(0).setState( true  );
		xor.getPinInputs().get(1).setState( false );
		xor.update();
		
		System.out.println( xor.getPinOutputs().get(0).getState() );
	}
	
	
	
	public static Trace connectPins( Pin a, Pin b ) {
		Trace trace = new Trace( a, b );
		a.connectPin( trace );
		b.connectPin( trace );
		return trace;
	}
}
