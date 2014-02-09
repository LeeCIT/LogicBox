


package logicBox.sim;
import java.util.List;



/**
 * Interface for components with output pins.
 * Complex components can have multiple pins.
 * @author Lee Coakley
 */
public interface PinIn {
	public List<Pin> getPinInputs();
}
