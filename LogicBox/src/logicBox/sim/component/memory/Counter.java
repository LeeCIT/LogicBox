


package logicBox.sim.component.memory;

import logicBox.gui.editor.graphics.GraphicComActive;
import logicBox.gui.editor.graphics.GraphicGen;
import logicBox.sim.SimUtil;
import logicBox.sim.component.ComponentType;
import logicBox.sim.component.PinIoMode;
import logicBox.sim.component.connective.Pin;



/**
 * An up counter.
 * Counts up once every clock pulse.
 * @author Lee Coakley
 */
public class Counter extends EdgeTriggered
{
	private static final long serialVersionUID = 1L;
	
	private int nextState;
	
	
	
	public Counter( int bits ) {
		super();
		
		SimUtil.addPins( pinInputs,  this, PinIoMode.input,  1    );
		SimUtil.addPins( pinOutputs, this, PinIoMode.output, bits );
	}
	
	
	
	public void reset() {
		super.reset();
		nextState = 0;
	}
	
	
	
	public Pin getPinClock() {
		return getPinInput( 0 ); 
	}
	
	
	
	protected void onPositiveEdge() {
		nextState++;
	}
	
	
	
	protected void onNegativeEdge() {
		SimUtil.encodeIntToPins( nextState, pinOutputs );
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.register;
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateCounter( getPinOutputCount() );
	}



	public String getName() {
		return "" + pinOutputs.size() + "-bit Up Counter";
	}
}






























