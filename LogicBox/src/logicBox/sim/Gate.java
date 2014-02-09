


package logicBox.sim;
import java.util.ArrayList;
import java.util.List;



/**
 * A logic gate.
 * @author Lee Coakley
 */
public abstract class Gate extends Component implements PinIn, PinOut, Updateable
{
	protected ArrayList<Pin> pinInputs;
	protected Pin            pinOut;
	
	
	
	public Gate() {
		super();
		pinInputs = new ArrayList<>();
		pinOut    = new Pin( this, false );
	}
	
	
	
	public Gate( int inputPinCount ) {
		this();
		
		for (int i=0; i<inputPinCount; i++)
			pinInputs.add( new Pin( this, true ) );
	}
	
	
	
	public List<Pin> getPinInputs() {
		return pinInputs;
	}
	
	
	
	public List<Pin> getPinOutputs() {
		return Util.wrapInList( pinOut );
	}



	public abstract void update();
}
