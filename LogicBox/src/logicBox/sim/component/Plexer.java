


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
	
	
	
	public String getPinName( PinIoMode mode, int index ) {
		String  str      = super.getPinName( mode, index );
		boolean isSelect = pinSelects.contains( getPin(mode,index) );
		
		if (isSelect) {
			int lowSel  = getPinInputCount() - getPinSelects().size();
			int highSel = getPinInputCount() - 1;
			
			str = "Pin: select " + (index - lowSel);
			
			if (index == lowSel)  str += " (LSB)";
			if (index == highSel) str += " (MSB)";
		}
		
		return str;
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
