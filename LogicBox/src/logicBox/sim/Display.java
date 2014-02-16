


package logicBox.sim;

import java.util.ArrayList;
import java.util.List;



/**
 * Powered component which displays feedback based on its input.
 * Has no output pins.
 * @author Lee Coakley
 */
public abstract class Display extends ComponentActive
{
	protected ArrayList<Pin> pinInputs;
	
	
	
	public Display( int inputPinCount ) {
		super();
		pinInputs = new ArrayList<>();
		SimUtil.addPins( pinInputs, this, PinIoMode.input, inputPinCount );
	}
	
	
	
	public List<Pin> getPinInputs() {
		return pinInputs;
	}
	
	
	
	public List<Pin> getPinOutputs() {
		return new ArrayList<>();
	}
	
	
	
	public void update() {
		// Do nothing
	}
}
