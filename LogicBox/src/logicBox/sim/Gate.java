


package logicBox.sim;
import java.util.ArrayList;



/**
 * A logic gate.
 * @author Lee Coakley
 */
public abstract class Gate
{
	protected ArrayList<Pin> pinInputs;
	protected Pin            pinOut;
	
	
	
	public Gate() {
		pinInputs = new ArrayList<>();
		pinOut    = new Pin();
	}
	
	
	
	public Gate( int inputPinCount ) {
		this();
		
		for (int i=0; i<inputPinCount; i++)
			pinInputs.add( new Pin() );
	}
	
	
	
	public abstract boolean update();
}
