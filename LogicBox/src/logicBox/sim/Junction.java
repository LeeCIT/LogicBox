


package logicBox.sim;
import java.util.List;
import java.util.ArrayList;



/**
 * Joins traces together.  Used to be called 'Solder'.
 * @author Lee Coakley
 */
public class Junction extends Component implements Updateable
{
	protected ArrayList<Pin> pins;	
	
	
	
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
	 * Create a new pin and add it to the junction.
	 */
	public Pin createPin() {
		Pin pin = new Pin( this, false );
		pins.add( pin );
		return pin;
	}
	
	
	
	public void update() {
		// TODO
	}
}
