


package logicBox.sim;
import java.util.ArrayList;



/**
 * A logic gate.
 * @author Lee Coakley
 */
public abstract class Gate extends Component
{
	protected ArrayList<Pin> pinInputs;
	protected Pin            pinOut;
	
	
	
	public Gate() {
		pinInputs = new ArrayList<>();
		pinOut    = new Pin( this );
	}
	
	
	
	public Gate( int inputPinCount ) {
		this();
		
		for (int i=0; i<inputPinCount; i++)
			pinInputs.add( new Pin( this ) );
	}
	
	
	
	public ArrayList<Pin> getPinInputs() {
		return pinInputs;
	}



	public Pin getPinOut() {
		return pinOut;
	}



	public abstract void update();
}
