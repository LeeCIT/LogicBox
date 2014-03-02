


package logicBox.sim.component;

import logicBox.sim.SimUtil;



/**
 * A logic-level source.
 * @author Lee Coakley
 */
public abstract class Source extends ComponentActive
{
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
