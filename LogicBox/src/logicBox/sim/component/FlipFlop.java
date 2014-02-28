


package logicBox.sim.component;

import logicBox.sim.LogicLevel;
import logicBox.sim.SimUtil;



/**
 * An edge-triggered flip-flop.
 * Can only change state on the rising edge of a clock pulse.
 * @author Lee Coakley
 */
public abstract class FlipFlop extends ComponentActive
{
	private boolean lastClock;
	
	
	
	public FlipFlop( int inputPinCount ) {
		super();
		SimUtil.addPins( pinInputs,  this, PinIoMode.input,  inputPinCount );
		SimUtil.addPins( pinOutputs, this, PinIoMode.output, 2             );
		
		reset();
	}
	
	
	
	public void reset() {
		super.reset();
		lastClock = false;
		getPinQinv().setState( false );
		getPinQinv().setState( true  );
	}
	
	
	
	public abstract Pin getPinClock();
	
	
	
	public Pin getPinQ() {
		return getPinOutput( 0 );
	}
	
	
	
	public Pin getPinQinv() {
		return getPinOutput( 1 );
	}
	
	
	
	protected boolean updateClock() {
		boolean clock     = getPinClock().getState();
		boolean isEdgePos = LogicLevel.isEdgePos( lastClock, clock );
		lastClock = clock;
		
		return isEdgePos;
	}
	
	
	
	protected void setQ( boolean state ) {
		getPinQ   ().setState(   state );
		getPinQinv().setState( ! state );
	}
	
	
	
	public boolean isCombinational() {
		return false;
	}
}































