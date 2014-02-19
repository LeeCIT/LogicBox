


package logicBox.sim.component;
import java.util.ArrayList;
import java.util.List;
import logicBox.sim.Pin;
import logicBox.sim.PinIoMode;
import logicBox.sim.SimUtil;



/**
 * A logic gate.
 * @author Lee Coakley
 */
public abstract class Gate extends ComponentActive
{
	protected List<Pin> pinInputs;
	protected Pin       pinOut;
	
	
	
	public Gate() {
		super();
		pinInputs = new ArrayList<>();
		pinOut    = new Pin( this, PinIoMode.output );
	}
	
	
	
	public Gate( int inputPinCount ) {
		this();
		SimUtil.addPins( pinInputs, this, PinIoMode.input, inputPinCount );
	}
	
	
	
	public List<Pin> getPinInputs() {
		return pinInputs;
	}
	
	
	
	public List<Pin> getPinOutputs() {
		return SimUtil.wrapInList( pinOut );
	}
	
	
	
	protected abstract boolean evaluate();
	
	
	
	public void update() {
		pinOut.setState( evaluate() );
	}
}
