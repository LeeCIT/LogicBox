


package logicBox.sim;
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
	
	
	
	public ArrayList<Pin> getPins() {
		return pins;
	}
	
	
	
	/**
	 * Create a new pin and add it to the interconnect.
	 */
	public Pin createPin() {
		Pin pin = new Pin( this, false );
		pins.add( pin );
		return pin;
	}



	public void update() {
		
	}
}
