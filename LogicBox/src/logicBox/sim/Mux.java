


package logicBox.sim;

import java.util.ArrayList;
import java.util.List;
import logicBox.util.Geo;


/**
 * An N-to-1 multiplexer.
 * @author Lee Coakley
 */
public class Mux extends ComponentActive
{
	private List<Pin> pinInputs;
	private List<Pin> pinSelects;
	private List<Pin> pinOutputs;
	
	
	
	public Mux( int inputPinCount ) {
		super();
		pinInputs  = new ArrayList<>();
		pinSelects = new ArrayList<>();
		pinOutputs = new ArrayList<>();
		
		int selectPinCount = (int) Math.ceil( Geo.log2(inputPinCount) );
		
		SimUtil.addPins( pinInputs,  this, PinIoMode.input,  inputPinCount  );
		SimUtil.addPins( pinSelects, this, PinIoMode.input,  selectPinCount );
		SimUtil.addPins( pinOutputs, this, PinIoMode.output, 1              );
	}
	
	
	
	public List<Pin> getPinInputs() {
		return pinInputs;
	}
	
	
	
	public List<Pin> getPinSelects() {
		return pinSelects;
	}
	
	
	
	public List<Pin> getPinOutputs() {
		return pinOutputs;
	}
	
	
	
	public void update() {
		for (Pin pin: pinOutputs)
			pin.setState( false );
		
		int select = 0;
		for (int i=0; i<pinSelects.size(); i++)
			if (pinSelects.get(i).getState())
				select |= (1 << i);
		
		boolean routeLevel = pinInputs.get( select ).getState();
		pinOutputs.get(0).setState( routeLevel );
	}
	
	
	
	public String getName() {
		return "" + getPinInputCount() + "-to-1 Multiplexer";
	}
	
	
	
	
	
	public static void main( String[] args ) {
		Mux mux = new Mux( 8 );
		
		// Select pin index 3 (fourth pin)
		mux.getPinSelects().get(0).setState( true );
		mux.getPinSelects().get(1).setState( true );
		
		mux.getPinInputs().get(3).setState( true );
		
		// Now output should be true
		mux.update();
		
		for (Pin pin: mux.getPinSelects())
			System.out.println( "sel: " + pin.getState() );
		
		for (Pin pin: mux.getPinInputs())
			System.out.println( "in : " + pin.getState() );		
		
		for (Pin pin: mux.getPinOutputs())
			System.out.println( "out: " + pin.getState() );
	}
}













































