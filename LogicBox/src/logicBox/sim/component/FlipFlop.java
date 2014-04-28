


package logicBox.sim.component;

import logicBox.sim.LogicLevel;
import logicBox.sim.SimUtil;



/**
 * An edge-triggered flip-flop.
 * Can only change state on the rising edge of a clock pulse.
 * The change is applied to the output pins on the falling edge of the pulse.
 * @author Lee Coakley
 */
public abstract class FlipFlop extends ComponentActive
{
	private static final long serialVersionUID = 1L;
	
	private boolean lastClock;
	private boolean nextQ;
	
	
	
	public FlipFlop( int inputPinCount ) {
		super();
		SimUtil.addPins( pinInputs,  this, PinIoMode.input,  inputPinCount );
		SimUtil.addPins( pinOutputs, this, PinIoMode.output, 2             );
		reset();
	}
	
	
	
	public void reset() {
		super.reset();
		lastClock = false;
		nextQ     = false;
		setQ( false );
	}
	
	
	
	public abstract Pin getPinClock();
	
	
	
	public Pin getPinQ() {
		return getPinOutput( 0 );
	}
	
	
	
	public Pin getPinQinv() {
		return getPinOutput( 1 );
	}
	
	
	
	public void update() {
		boolean clock     = getPinClock().getState();
		boolean isEdgePos = LogicLevel.isPositiveEdge( lastClock, clock );
		boolean isEdgeNeg = LogicLevel.isNegativeEdge( lastClock, clock );
		lastClock = clock;
		
		if (isEdgePos)
			nextQ = evaluateNextQ();
		
		if (isEdgeNeg)
			setQ( nextQ );
	}
	
	
	
	protected abstract boolean evaluateNextQ();
	
	
	
	protected void setQ( boolean state ) {
		getPinQ   ().setState(   state );
		getPinQinv().setState( ! state );
	}
	
	
	
	public boolean isCombinational() {
		return false;
	}
}































