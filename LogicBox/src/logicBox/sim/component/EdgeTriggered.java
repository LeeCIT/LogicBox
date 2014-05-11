


package logicBox.sim.component;

import logicBox.sim.LogicLevel;
import logicBox.sim.SimUtil;
import logicBox.util.Callback;



/**
 * An edge-triggered component.
 * Intended for flip-flops and components made up of them, like registers and counters.
 * Can only change state on the rising edge of a clock pulse.
 * The change is applied to the output pins on the falling edge of the pulse.
 * This isn't technically accurate - in real life the delay is much shorter.
 * @author Lee Coakley
 */
public abstract class EdgeTriggered extends ComponentActive
{
	private static final long serialVersionUID = 1L;
	
	private boolean lastClock;
	
	
	
	public EdgeTriggered() {
		super();
		reset();
	}
	
	
	
	public void reset() {
		super.reset();
		lastClock = false;
	}
	
	
	
	public abstract Pin getPinClock();
	
	
	
	public void update() {
		boolean clock     = getPinClock().getState();
		boolean isEdgePos = LogicLevel.isPositiveEdge( lastClock, clock );
		boolean isEdgeNeg = LogicLevel.isNegativeEdge( lastClock, clock );
		lastClock = clock;
		
		if (isEdgePos) onPositiveEdge();
		if (isEdgeNeg) onNegativeEdge();
	}
	
	
	
	protected abstract void onPositiveEdge();
	protected abstract void onNegativeEdge();
	
	
	
	public boolean isCombinational() {
		return false;
	}
}































