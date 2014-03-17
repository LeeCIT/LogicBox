


package logicBox.sim.component;
import logicBox.sim.SimUtil;



/**
 * A logic gate.
 * @author Lee Coakley
 */
public abstract class Gate extends ComponentActive
{
	private static final long serialVersionUID = 1L;
	
	
	
	public Gate() {
		super();
	}
	
	
	
	public Gate( int inputPinCount ) {
		this();
		SimUtil.addPins( pinInputs,  this, PinIoMode.input,  inputPinCount );
		SimUtil.addPins( pinOutputs, this, PinIoMode.output, 1             );
	}
	
	
	
	protected abstract boolean evaluate();
	
	
	
	public void update() {
		getPinOutput(0).setState( evaluate() );
	}
}
