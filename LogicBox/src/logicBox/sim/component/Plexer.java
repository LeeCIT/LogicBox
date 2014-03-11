


package logicBox.sim.component;

import java.util.ArrayList;
import java.util.List;
import logicBox.sim.SimUtil;



/**
 * Defines the functionality common to the multiplexer and demultiplexer.
 * @author Lee Coakley
 */
public abstract class Plexer extends ComponentActive
{
	private static final long serialVersionUID = 1L;
	
	protected List<Pin> pinSelects;
	
	
	
	public Plexer() {
		super();
		pinSelects = new ArrayList<>();
	}
	
	
	
	protected void createPins( int inputs, int selects, int outputs ) {
		SimUtil.addPins( pinInputs,  this, PinIoMode.input,  inputs + selects );
		SimUtil.addPins( pinOutputs, this, PinIoMode.output, outputs );
		
		for (int i=inputs; i<pinInputs.size(); i++)
			pinSelects.add( pinInputs.get(i) );
	}
	
	
	
	public List<Pin> getPinSelects() {
		return pinSelects;
	}
	
	
	
	public void update() {
		for (Pin pin: pinOutputs)
			pin.setState( false );
		
		Pin src  = getSourcePin();
		Pin dest = getDestinationPin();
		
		if (src != null  &&  dest != null)
			dest.setState( src.getState() );
	}
	
	
	
	protected abstract Pin getSourcePin();
	
	
	
	protected abstract Pin getDestinationPin();
	
	
	
	protected int decodeSelectPins() {
		return SimUtil.decodePinsToInt( pinSelects );
	}
}
