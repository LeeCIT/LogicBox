


package logicBox.sim.component;

import logicBox.gui.editor.GraphicComActive;
import logicBox.gui.editor.GraphicGen;
import logicBox.sim.SimUtil;



/**
 * An up counter.
 * Counts up one every clock pulse.
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































