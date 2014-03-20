


package logicBox.sim.component;



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
	
	
	
	public String getName() {
		return "LED";
	}
	
	
	
	public void update() {
		lit = getPinInputState( 0 );
	}
	
	
	
	public void reset() {
		super.reset();
		lit = false;
	}
}
