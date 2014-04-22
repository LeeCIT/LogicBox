


package logicBox.sim.component;

import logicBox.gui.editor.GraphicComActive;
import logicBox.gui.editor.GraphicGen;
import logicBox.sim.SimUtil;



/**
 * A special type of component which connects a black-box to the outside world.
 * TODO for usability's sake, the graphic should probably indicate on/off like the LED
 * TODO could use the socket part for that
 * TODO also be toggleable
 * @author Lee Coakley
 */
public class BlackBoxPin extends ComponentActive
{
	private static final long serialVersionUID = 1L;
	
	private boolean state;
	private Pin     pin;
	
	
	
	public BlackBoxPin( PinIoMode mode ) {
		super();
		
		SimUtil.addPins( (mode==PinIoMode.input) ? pinInputs : pinOutputs, this, mode, 1 );
		this.pin = getPins().get( 0 );
		
		if ( ! isInput() && ! isOutput())
			throw new RuntimeException( "Bad mode: " + mode );
	}
	
	
	
	public PinIoMode getIoMode() {
		return pin.getIoMode();
	}
	
	
	
	public boolean isInput() {
		return getIoMode() == PinIoMode.input;
	}
	
	
	
	public boolean isOutput() {
		return getIoMode() == PinIoMode.output;
	}
	
	
	
	public void setState( boolean state ) {
		this.state = state;
	}
	
	
	
	public boolean getState() {
		return state;
	}
	
	
	
	public void reset() {
		super.reset();
		setState( false );
	}
	
	
	
	public void update() {	
		if (isOutput())
			 setState( pin.getState() );
		else pin.setState( getState() );
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateBlackboxPin( isOutput() );
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.blackBoxPin;
	}
	
	
	
	public String getName() {
		return "Black-box pin (" + (isOutput() ? "output" : "input") + ")";
	}
}
