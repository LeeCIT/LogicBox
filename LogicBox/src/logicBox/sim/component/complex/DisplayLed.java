


package logicBox.sim.component.complex;

import logicBox.gui.editor.graphics.GraphicComActive;
import logicBox.gui.editor.graphics.GraphicGen;
import logicBox.sim.component.ComponentType;



/**
 * Light.  Shows state of input pin.
 * @author Lee Coakley
 */
public class DisplayLed extends Display
{
	private static final long serialVersionUID = 1L;
	
	private boolean lit;
	
	
	
	public DisplayLed() {
		super( 1 );
		reset();
	}
	
	
	
	public boolean isLit() {
		return lit;
	}
	
	
	
	public void update() {
		lit = getPinInputState( 0 );
	}
	
	
	
	public void reset() {
		super.reset();
		lit = false;
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.displayLed;
	}
	
	
	
	public String getName() {
		return "LED";
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateDisplayLed();
	}
}
