


package logicBox.sim.component;

import logicBox.gui.editor.GraphicComActive;
import logicBox.gui.editor.GraphicGen;



/**
 * Outputs high if an odd number of inputs are high.
 * 0,0 -> 0
 * 0,1 -> 1
 * 1,0 -> 1
 * 1,1 -> 0
 * @author Lee Coakley
 */
public class GateXor extends Gate
{
	private static final long serialVersionUID = 1L;
	
	
	
	public GateXor() {
		super( 2 );
	}
	
	
	
	public GateXor( int inputPinCount ) {
		super( inputPinCount );
	}
	
	
	
	// Source: Digital Logic and Computer Design, p59.
	public boolean evaluate() {
		boolean a     = getPinInputState( 0 );
		boolean b     = getPinInputState( 1 );
		boolean state = a ^ b;
		
		for (int i=2; i<pinInputs.size(); i++)
			state ^= getPinInputState( i );
		
		return state;
	}
	
	
	
	public String getName() {
		return "XOR gate";
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateGateXor( getPinInputCount() );
	}
	
	
	
	
	
	public static void main( String[] args ) {
		Gate xor = new GateXor( 4 );
		
		System.out.println( "A B C D | F" );
		System.out.println( "-----------" );
		
		for (int a=0; a<=1; a++)
		for (int b=0; b<=1; b++)
		for (int c=0; c<=1; c++)
		for (int d=0; d<=1; d++) {
			xor.getPinInputs().get(0).setState( a==1 );
			xor.getPinInputs().get(1).setState( b==1 );
			xor.getPinInputs().get(2).setState( c==1 );
			xor.getPinInputs().get(3).setState( d==1 );
			
			System.out.println( "" + a + " " + b + " " + c + " " + d + " | " + (xor.evaluate() ? 1 : 0) );
		}
	}
}



































