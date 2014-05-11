


package logicBox.sim.component;

import logicBox.gui.editor.GraphicComActive;
import logicBox.gui.editor.GraphicGen;
import logicBox.sim.SimUtil;



/**
 * Priority Encoder.
 * Takes one bit input and outputs corresponding binary pattern.
 * If multiple inputs are set the highest one is used.
 * @author Lee Coakley
 */
public class Encoder extends ComponentActive
{
	private static final long serialVersionUID = 1L;
	
	
	
	public Encoder( int outputPinCount ) {
		super();
		
		int inputPinCount = (int) Math.pow( 2, outputPinCount );
		
		SimUtil.addPins( pinInputs,  this, PinIoMode.input,  inputPinCount  );
		SimUtil.addPins( pinOutputs, this, PinIoMode.output, outputPinCount );
	}
	
	
	
	public void update() {
		for (Pin pin: pinOutputs)
			pin.setState( false );
		
		for (int i=getPinInputCount()-1; i>=0; i--) {
			if (getPinInput( i ).getState()) {
				SimUtil.encodeIntToPins( i, pinOutputs );
				return;
			}
		}
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.encoder;
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateEncoder( getPinInputCount(), getPinOutputCount() );
	}
	
	
	
	public String getName() {
		return "" + getPinInputCount() + "-to-" + getPinOutputCount() + " Priority Encoder";
	}
}









