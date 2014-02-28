


package logicBox.sim.component;

import java.util.ArrayList;
import java.util.List;
import logicBox.sim.SimUtil;



/**
 * D-type flip-flop.
 * A 1-bit memory with inputs [D, E] and outputs [Q, !Q].
 * When E is high D determines Q.
 * When E is low the component's state does not change.
 * !Q is always the inverse of Q. 
 * @author Lee Coakley
 */
public class FlipFlopD extends ComponentActive
{
	private List<Pin> pinInputs;
	private List<Pin> pinOutputs;
	
	
	
	public FlipFlopD() {
		super();
		pinInputs  = new ArrayList<>();
		pinOutputs = new ArrayList<>();
		SimUtil.addPins( pinInputs,  this, PinIoMode.input,  2 );
		SimUtil.addPins( pinOutputs, this, PinIoMode.output, 2 );
		getPinQinv().setState( true ); // Ensure valid initial state
	}
	
	
	
	public List<Pin> getPinInputs() {
		return pinInputs;
	}
	
	
	
	public List<Pin> getPinOutputs() {
		return pinOutputs;
	}
	
	
	
	public Pin getPinD() {
		return pinInputs.get( 0 );
	}
	
	
	
	public Pin getPinE() {
		return pinInputs.get( 1 );
	}
	
	
	
	public Pin getPinQ() {
		return pinOutputs.get( 0 );
	}
	
	
	
	public Pin getPinQinv() {
		return pinOutputs.get( 1 );
	}
	
	
	
	public void update() {
		if ( ! getPinE().getState())
			return;
		
		boolean state = getPinD().getState();
		getPinQ   ().setState(   state );
		getPinQinv().setState( ! state );
	}
	
	
	
	public String getName() {
		return "D flip-flop";
	}
	
	
	
	
	
	public static void main( String[] args ) {
		FlipFlopD f = new FlipFlopD();
		
		f.getPinE().setState( false );
		f.getPinD().setState( true  );
		f.update();
		System.out.println( "\n" + f.getPinQ() + "\n" + f.getPinQinv() ); // Should still be [0,1]
		
		f.getPinE().setState( true );
		f.getPinD().setState( true );
		f.update();
		System.out.println( "\n" + f.getPinQ() + "\n" + f.getPinQinv() ); // Should be [1,0]
		
		f.getPinD().setState( false );
		f.update();
		System.out.println( "\n" + f.getPinQ() + "\n" + f.getPinQinv() ); // Should be [0,1]
	}
}































