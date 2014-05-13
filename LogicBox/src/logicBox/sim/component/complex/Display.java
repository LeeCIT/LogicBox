


package logicBox.sim.component.complex;

import logicBox.sim.SimUtil;
import logicBox.sim.component.ComponentActive;
import logicBox.sim.component.PinIoMode;



/**
 * Powered component which displays feedback based on its input.
 * Has no output pins.
 * @author Lee Coakley
 */
public abstract class Display extends ComponentActive
{
	private static final long serialVersionUID = 1L;
	
	
	
	public Display() {
		super();
	}
	
	
	
	public Display( int inputPinCount ) {
		super();
		SimUtil.addPins( pinInputs, this, PinIoMode.input, inputPinCount );
	}
}
