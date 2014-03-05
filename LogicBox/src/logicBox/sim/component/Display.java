


package logicBox.sim.component;

import logicBox.sim.SimUtil;



/**
 * Powered component which displays feedback based on its input.
 * Has no output pins.
 * @author Lee Coakley
 */
public abstract class Display extends ComponentActive
{
	public Display() {
		super();
	}
	
	
	
	public Display( int inputPinCount ) {
		super();
		SimUtil.addPins( pinInputs, this, PinIoMode.input, inputPinCount );
	}
}
