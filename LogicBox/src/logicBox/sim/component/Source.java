


package logicBox.sim.component;

import logicBox.sim.SimUtil;



/**
 * A logic-level source.
 * @author Lee Coakley
 */
public abstract class Source extends ComponentActive
{
	public Source( boolean state ) {
		super();
		SimUtil.addPins( pinOutputs, this, PinIoMode.output, 1 );
		setState( state );
	}
	
	
	
	public void setState( boolean state ) {
		setPinOutputState( 0, state );
	}
	
	
	
	public boolean getState() {
		return getPinOutputState( 0 );
	}
}
