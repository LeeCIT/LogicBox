


package logicBox.sim.component;

import java.util.ArrayList;
import java.util.List;
import logicBox.sim.Pin;
import logicBox.sim.PinIoMode;
import logicBox.sim.SimUtil;
import logicBox.util.Geo;



/**
 * Defines the functionality common to the multiplexer and demultiplexer.
 * @author Lee Coakley
 */
public abstract class Plexer extends ComponentActive
{
	protected List<Pin> pinInputs;
	protected List<Pin> pinSelects;
	protected List<Pin> pinOutputs;
	
	
	
	public Plexer() {
		super();
		pinInputs  = new ArrayList<>();
		pinSelects = new ArrayList<>();
		pinOutputs = new ArrayList<>();
	}
	
	
	
	protected void createPins( int inputs, int selects, int outputs ) {
		SimUtil.addPins( pinInputs,  this, PinIoMode.input,  inputs  );
		SimUtil.addPins( pinSelects, this, PinIoMode.input,  selects );
		SimUtil.addPins( pinOutputs, this, PinIoMode.output, outputs );
	}
	
	
	
	public List<Pin> getPinInputs() {
		return pinInputs;
	}
	
	
	
	public List<Pin> getPinSelects() {
		return pinSelects;
	}
	
	
	
	public List<Pin> getPinOutputs() {
		return pinOutputs;
	}
	
	
	
	public void update() {
		for (Pin pin: pinOutputs)
			pin.setState( false );
		
		Pin src  = getSourcePin();
		Pin dest = getDestinationPin();
		
		if (src  != null
		&&  dest != null)
			dest.setState( src.getState() );
	}
	
	
	
	protected abstract Pin getSourcePin();
	
	
	
	protected abstract Pin getDestinationPin();
	
	
	
	protected int computeSelectPinCount( int basisPinCount ) {
		return (int) Math.ceil( Geo.log2(basisPinCount) );
	}
	
	
	
	protected int decodeSelectPins() {
		return SimUtil.decodeToInt( pinSelects );
	}
}
