


package logicBox.sim.component.complex;

import logicBox.gui.editor.graphics.GraphicComActive;
import logicBox.gui.editor.graphics.GraphicGen;
import logicBox.sim.component.ComponentType;
import logicBox.sim.component.connective.Pin;
import logicBox.util.Geo;



/**
 * A 1-to-N demultiplexer.
 * @author Lee Coakley
 */
public class Demux extends Plexer
{
	private static final long serialVersionUID = 1L;
	
	
	
	public Demux( int outputPinCount ) {
		super();
		createPins( 1, Geo.log2i(outputPinCount), outputPinCount );
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
	
	
	
	public ComponentType getType() {
		return ComponentType.demux;
	}
	
	
	
	public String getName() {
		return "1-to-" + getPinOutputCount() + " Demultiplexer";
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateDemux( 1, pinSelects.size(), getPinOutputCount() );
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
		
		System.out.println();
		for (Pin pin: demux.getPinSelects())
			System.out.println( "sel: " + pin.getState() );
		
		System.out.println();
		for (Pin pin: demux.getPinInputs())
			System.out.println( "in : " + pin.getState() );		
		
		System.out.println();
		for (Pin pin: demux.getPinOutputs())
			System.out.println( "out: " + pin.getState() );
	}
}













































