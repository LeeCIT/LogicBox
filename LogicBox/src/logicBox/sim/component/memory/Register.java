


package logicBox.sim.component.memory;

import java.util.ArrayList;
import java.util.List;
import logicBox.gui.editor.graphics.GraphicComActive;
import logicBox.gui.editor.graphics.GraphicGen;
import logicBox.sim.SimUtil;
import logicBox.sim.component.ComponentType;
import logicBox.sim.component.PinIoMode;
import logicBox.sim.component.connective.Pin;



/**
 * A register which stores between 1 and 32 bits.
 * @author Lee Coakley
 */
public class Register extends EdgeTriggered
{
	private static final long serialVersionUID = 1L;
	
	private List<Pin> pinReads;
	private int       nextState;
	
	
	
	public Register( int bits ) {
		super();
		
		pinReads = new ArrayList<>();
		
		SimUtil.addPins( pinInputs,  this, PinIoMode.input,  3    );
		SimUtil.addPins( pinReads,   this, PinIoMode.input,  bits );
		SimUtil.addPins( pinOutputs, this, PinIoMode.output, bits );
		pinInputs.addAll( pinReads );
	}
	
	
	
	public void reset() {
		super.reset();
		nextState = 0;
	}
	
	
	
	public Pin getPinWrite() {
		return getPinInput( 0 ); 
	}
	
	
	
	public Pin getPinClock() {
		return getPinInput( 1 ); 
	}
	
	
	
	public Pin getPinReset() {
		return getPinInput( 2 );
	}
	
	
	
	protected void onPositiveEdge() {
		if (getPinWrite().getState())
			nextState = SimUtil.decodePinsToInt( pinReads );
		
		if (getPinReset().getState())
			nextState = 0;
	}
	
	
	
	protected void onNegativeEdge() {
		SimUtil.encodeIntToPins( nextState, pinOutputs );
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.register;
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateRegister( getPinOutputCount() );
	}
	
	
	
	public String getName() {
		return "" + pinOutputs.size() + "-bit Register";
	}
}































