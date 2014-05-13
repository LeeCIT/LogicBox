


package logicBox.sim.component.simple;

import logicBox.sim.SimUtil;
import logicBox.sim.component.ComponentActive;
import logicBox.sim.component.PinIoMode;



/**
 * A logic-level source.
 * @author Lee Coakley
 */
public abstract class Source extends ComponentActive
{
	private static final long serialVersionUID = 1L;
	
	private boolean state;
	
	
	
	public Source( boolean state ) {
		super();
		SimUtil.addPins( pinOutputs, this, PinIoMode.output, 1 );
		setState( state );
	}
	
	
	
	public void setState( boolean state ) {
		this.state = state;
	}
	
	
	
	public boolean getState() {
		return state;
	}
	
	
	
	public void update() {
		setPinOutputState( 0, state );
	}
}
