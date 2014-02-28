


package logicBox.sim.component;



/**
 * Light.  Shows state of input pin.
 * @author Lee Coakley
 */
public class DisplayLed extends Display
{
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
