


package logicBox.sim.component;

import logicBox.sim.SimUtil;



/**
 * Decoder.
 * Activates one output pin corresponding to the unsigned integer bit-pattern formed by the inputs.
 * LSB is pin 0.
 * @author Lee Coakley
 */
public class Decoder extends ComponentActive
{
	private static final long serialVersionUID = 1L;
	
	
	
	public Decoder( int inputPinCount ) {
		super();
		
		int outputPinCount = (int) Math.pow( 2, inputPinCount );
		
		SimUtil.addPins( pinInputs,  this, PinIoMode.input,  inputPinCount  );
		SimUtil.addPins( pinOutputs, this, PinIoMode.output, outputPinCount );
	}
	
	
	
	public String getName() {
		return "" + getPinInputCount() + "-to-" + getPinOutputCount() + " Decoder";
	}
	
	
	
	public void update() {
		for (Pin pin: pinOutputs)
			pin.setState( false );
		
		int activeIndex = SimUtil.decodePinsToInt( pinInputs );
		setPinOutputState( activeIndex, true );
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.decoder;
	}
	
	
	
	
	
	public static void main( String[] args ) {
		Decoder dec = new Decoder( 2 );
		
		System.out.println( dec.getName() );
		
		dec.setPinInputState( 0, true );
		dec.setPinInputState( 1, true );
		
		dec.update();
		
		System.out.println();
		for (Pin pin: dec.getPinInputs())
			System.out.println( "in : " + pin.getState() );		
		
		System.out.println();
		for (Pin pin: dec.getPinOutputs())
			System.out.println( "out: " + pin.getState() );
	}
}









