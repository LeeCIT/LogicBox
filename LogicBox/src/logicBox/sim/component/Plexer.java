


package logicBox.sim.component;

import java.util.ArrayList;
import java.util.List;
import logicBox.sim.SimUtil;
import logicBox.util.Geo;



/**
 * Defines the functionality common to the multiplexer and demultiplexer.
 * TODO although this works, having three pin groups is incompatible with the graphical pin mapper
 * @author Lee Coakley
 */
public abstract class Plexer extends ComponentActive
{
	protected List<Pin> pinSelects;
	
	
	
	public Plexer() {
		super();
		pinSelects = new ArrayList<>();
	}
	
	
	
	protected void createPins( int inputs, int selects, int outputs ) {
		SimUtil.addPins( pinInputs,  this, PinIoMode.input,  inputs  );
		SimUtil.addPins( pinSelects, this, PinIoMode.input,  selects );
		SimUtil.addPins( pinOutputs, this, PinIoMode.output, outputs );
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
	
	
	
	protected int computeSelectPinCount( int basisPinCount ) {
		return (int) Math.ceil( Geo.log2(basisPinCount) );
	}
	
	
	
	protected int decodeSelectPins() {
		return SimUtil.decodePinsToInt( pinSelects );
	}
}
