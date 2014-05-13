


package logicBox.sim.component;
import java.util.List;
import logicBox.sim.component.connective.Pin;



/**
 * Interface for active components with pins.
 * Components should return an empty list if they have no pins.
 * @author Lee Coakley
 */
public interface PinIo {
	public List<Pin> getPins();
	
	public List<Pin> getPinInputs();
	public List<Pin> getPinOutputs();
	
	public Pin getPinInput ( int index );
	public Pin getPinOutput( int index );
	
	public boolean getPinInputState ( int index );
	public boolean getPinOutputState( int index );
	
	public void setPinInputState ( int index, boolean state );
	public void setPinOutputState( int index, boolean state );
}
