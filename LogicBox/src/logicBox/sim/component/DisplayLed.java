


package logicBox.sim.component;



/**
 * Light.  Shows state of input pin.
 * @author Lee Coakley
 */
public class DisplayLed extends Display
{
	public DisplayLed() {
		super( 1 );
	}
	
	
	
	public boolean getState() {
		return getPinInputs().get(0).getState();
	}



	public String getName() {
		return "LED display";
	}
}
