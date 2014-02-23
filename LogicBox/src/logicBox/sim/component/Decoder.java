


package logicBox.sim.component;

import java.util.ArrayList;
import java.util.List;
import logicBox.sim.SimUtil;



/**
 * Decoder.
 * Activates one output pin corresponding to the unsigned integer bit-pattern formed by the inputs.
 * LSB is pin 0.
 * @author Lee Coakley
 */
public class Decoder extends ComponentActive
{
	private List<Pin> pinInputs;
	private List<Pin> pinOutputs;
	
	
	
	public Decoder( int inputPinCount ) {
		super();
		
		pinInputs  = new ArrayList<>();
		pinOutputs = new ArrayList<>();
		
		int outputPinCount = (int) Math.pow( 2, inputPinCount );
		
		SimUtil.addPins( pinInputs,  this, PinIoMode.input,  inputPinCount  );
		SimUtil.addPins( pinOutputs, this, PinIoMode.output, outputPinCount );
	}
	
	
	
	public List<Pin> getPinInputs() {
		return pinInputs;
	}
	
	
	
	public List<Pin> getPinOutputs() {
		return pinOutputs;
	}
	
	
	
	public String getName() {
		return "" + getPinInputCount() + "-to-" + getPinOutputCount() + " Decoder";
	}
	
	
	
	public void update() {
		for (Pin pin: pinOutputs)
			pin.setState( false );
		
		int activeIndex = SimUtil.decodePinsToInt( pinInputs );
		Pin activePin   = pinOutputs.get( activeIndex );
		
		activePin.setState( true );
	}
	
	
	
	
	
	public static void main( String[] args ) {
		Decoder dec = new Decoder( 2 );
		
		System.out.println( dec.getName() );
		
		dec.getPinInputs().get(0).setState( true );
		dec.getPinInputs().get(1).setState( true );
		
		dec.update();
		
		System.out.println();
		for (Pin pin: dec.getPinInputs())
			System.out.println( "in : " + pin.getState() );		
		
		System.out.println();
		for (Pin pin: dec.getPinOutputs())
			System.out.println( "out: " + pin.getState() );
	}
}









