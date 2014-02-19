


package logicBox.sim.component;

import logicBox.sim.Pin;



/**
 * A 1-to-N demultiplexer.
 * @author Lee Coakley
 */
public class Demux extends Plexer
{
	public Demux( int outputPinCount ) {
		super();
		createPins( 1, computeSelectPinCount(outputPinCount), outputPinCount );
	}
	
	
	
	protected Pin getSourcePin() {
		return pinInputs.get( 0 );
	}
	
	
	
	protected Pin getDestinationPin() {
		int select = decodeSelectPins();
		
		if (select < getPinOutputCount())
			 return pinOutputs.get( select );
		else return null;
	}
	
	
	
	public String getName() {
		return "1-to-" + getPinOutputCount() + " Demultiplexer";
	}
	
	
	
	
	
	public static void main( String[] args ) {
		Demux demux = new Demux( 4 );
		
		System.out.println( demux.getName() );
		
		// Select pin index 3 (fourth pin)
		demux.getPinSelects().get(0).setState( true );
		demux.getPinSelects().get(1).setState( true );
		
		demux.getPinInputs().get(0).setState( true );
		
		// Now output index 3 should be true
		demux.update();
		
		for (Pin pin: demux.getPinSelects())
			System.out.println( "sel: " + pin.getState() );
		
		for (Pin pin: demux.getPinInputs())
			System.out.println( "in : " + pin.getState() );		
		
		for (Pin pin: demux.getPinOutputs())
			System.out.println( "out: " + pin.getState() );
	}
}













































