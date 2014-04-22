


package logicBox.sim.component;

import java.util.List;
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
	
	private boolean isInput;
	private boolean state;
	private Pin     pin;
	
	
	
	public BlackBoxPin( boolean isInput ) {
		super();
		
		this.isInput = isInput; // An input bbpin has an opposite actual pin mode
		
		List<Pin> targetList = isInput ? pinOutputs       : pinInputs;
		PinIoMode mode       = isInput ? PinIoMode.output : PinIoMode.input;
		
		SimUtil.addPins( targetList, this, mode, 1 );
		this.pin = getPins().get( 0 );
	}
	
	
	
	public boolean interactClick() {
		if (isOutput()) {
			return false;
		} else {
			setState( ! getState() );
			return true;
		}
	}
	
	
	
	public boolean isInput() {
		return isInput;
	}
	
	
	
	public boolean isOutput() {
		return ! isInput();
	}
	
	
	
	public void setState( boolean state ) {
		this.state = state;
	}
	
	
	
	public boolean getState() {
		return state;
	}
	
	
	
	public void reset() {
		super.reset();
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
		String str = "Black-box pin (" + (isOutput() ? "output" : "input") + ")";
		
		if (isInput())
			str += " (click to toggle on/off)";
			
		return str;
	}
}
