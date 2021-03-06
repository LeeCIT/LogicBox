


package logicBox.sim.component.connective;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import logicBox.sim.component.Component;
import logicBox.sim.component.ComponentPassive;
import logicBox.sim.component.ComponentType;
import logicBox.sim.component.PinIoMode;
import logicBox.util.Util;



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
	
	
	
	public void disconnect() {
		for (Pin pin: vpins)
			pin.disconnect();
		
		vpins.clear();
	}
	
	
	
	public Set<Component> getConnectedComponents() {
		Set<Component> set = Util.createIdentityHashSet();
		
		for (Pin pin: getPins())
			if (pin.hasTrace())
				set.add( pin.getTrace() );
		
		return set;
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.junction;
	}
	
	
	
	public String getName() {
		return "Junction";
	}
}












