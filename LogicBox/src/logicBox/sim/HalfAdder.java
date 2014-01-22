


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
	}
}
