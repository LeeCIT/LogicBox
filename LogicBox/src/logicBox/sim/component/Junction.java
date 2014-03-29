


package logicBox.sim.component;
import java.util.List;
import java.util.ArrayList;



/**
 * Joins traces together.
 * @author Lee Coakley
 */
public class Junction extends ComponentPassive
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Junctions don't really have pins.  This is an artifact of the sim structure
	 * requiring that traces connect only to pins.  It simplifies things everywhere else.
	 */
	private List<Pin> vpins;
	
	
	
	public Junction() {
		super();
		vpins = new ArrayList<>();
	}
	
	
	
	public List<Pin> getPins() {
		return vpins;
	}
	
	
	
	public List<Pin> getPinsExcept( Pin pin ) {
		List<Pin> pinsCopy = new ArrayList<>( vpins );
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
		vpins.add( pin );
		return pin;
	}
	
	
	
	public void orState( boolean state ) {
		super.orState( state );
		
		for (Pin pin: vpins)
			pin.orState( state );
	}
	
	
	
	public void setState( boolean state ) {
		super.setState( state );
		
		for (Pin pin: vpins)
			pin.setState( state );
	}
	
	
	
	public void reset() {
		super.reset();
		
		setState( false );
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.junction;
	}
	
	
	
	public String getName() {
		return "Junction";
	}
}












