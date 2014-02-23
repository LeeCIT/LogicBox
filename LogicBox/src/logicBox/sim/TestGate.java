


package logicBox.sim;

import logicBox.sim.component.Gate;
import logicBox.sim.component.GateXor;



public class TestGate
{
	public static void main( String[] args ) {
		Gate xor = new GateXor();
		
		xor.getPinInputs().get(0).setState( true  );
		xor.getPinInputs().get(1).setState( false );
		xor.update();
		
		System.out.println( xor.getPinOutputs().get(0).getState() );
	}
}
