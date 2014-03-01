


package logicBox.sim.component;
import java.util.List;
import java.util.ArrayList;



/**
 * Joins traces together.
 * @author Lee Coakley
 */
public class Junction extends ComponentPassive
{
	/**
	 * Junctions don't really have pins.  This is an artifact of the sim structure
	 * requiring that traces connect only to pins.  It simplifies things everywhere else.
	 */
	private List<Pin> pins;
	
	
	
	public Junction() {
		super();
		pins = new ArrayList<>();
	}
	
	
	
	public List<Pin> getPins() {
		return pins;
	}
	
	
	
	public List<Pin> getPinsExcept( Pin pin ) {
		List<Pin> pinsCopy = new ArrayList<>( pins );
		pinsCopy.remove( pin );
		return pinsCopy;
	}
	
	
	
	/**
	 * Create a new pin, add it to the junction and return it.
	 * The pin inherits the state of the junction.
	 */
	public Pin createPin() {
		Pin pin = new Pin( this, PinIoMode.bidi );
		pin.setState( getState() );
		pins.add( pin );
		return pin;
	}
	
	
	
	public void setState( boolean state ) {
		super.setState( state );
		
		for (Pin pin: pins)
			pin.setState( state );
	}
	
	
	
	public void reset() {
		super.reset();
		
		setState( false );
	}
	
	
	
	public String getName() {
		return "Junction";
	}
}












