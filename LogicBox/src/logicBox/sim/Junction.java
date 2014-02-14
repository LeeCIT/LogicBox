


package logicBox.sim;
import java.util.List;
import java.util.ArrayList;



/**
 * Joins traces together.  Used to be called 'Solder'.
 * @author Lee Coakley
 */
public class Junction extends ComponentPassive
{
	private ArrayList<Pin> pins;
	private boolean        state;
	
	
	
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
	 */
	public Pin createPin() {
		Pin pin = new Pin( this, PinIoMode.bidi );
		pins.add( pin );
		return pin;
	}



	public boolean getState() {
		return state;
	}



	public void setState( boolean state ) {
		this.state = state;
	}



	public String getName() {
		return "Junction";
	}
}
