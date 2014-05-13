


package logicBox.sim.component.complex;

import logicBox.gui.editor.graphics.GraphicComActive;
import logicBox.gui.editor.graphics.GraphicGen;
import logicBox.sim.component.ComponentType;
import logicBox.sim.component.connective.Pin;
import logicBox.util.Geo;



/**
 * An N-to-1 multiplexer.
 * @author Lee Coakley
 */
public class Mux extends Plexer
{
	private static final long serialVersionUID = 1L;
	
	
	
	public Mux( int inputPinCount ) {
		super();
		createPins( inputPinCount, Geo.log2i(inputPinCount), 1 );
	}
	
	
	
	protected Pin getSourcePin() {
		int select = decodeSelectPins();
		
		if (select < getPinInputCount())
			 return getPinInputs().get( select );
		else return null;
	}
	
	
	
	protected Pin getDestinationPin() {
		return pinOutputs.get( 0 );
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.mux;
	}
	
	
	
	public String getName() {
		return (getPinInputCount() - getPinSelects().size()) + "-to-1 Multiplexer";
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateMux( getPinInputCount()- getPinSelects().size(), pinSelects.size(), getPinOutputCount() );
	}
	
	
	
	
	
	public static void main( String[] args ) {
		Mux mux = new Mux( 16 );
		
		System.out.println( mux.getName() );
		
		// Select pin index 3 (fourth pin)
		mux.getPinSelects().get(0).setState( true );
		mux.getPinSelects().get(1).setState( true );
		
		mux.getPinInputs().get(3).setState( true );
		
		// Now output index 3 should be true
		mux.update();
		
		System.out.println();
		for (Pin pin: mux.getPinSelects())
			System.out.println( "sel: " + pin.getState() );
		
		System.out.println();
		for (Pin pin: mux.getPinInputs())
			System.out.println( "in : " + pin.getState() );		
		
		System.out.println();
		for (Pin pin: mux.getPinOutputs())
			System.out.println( "out: " + pin.getState() );
	}
}













































