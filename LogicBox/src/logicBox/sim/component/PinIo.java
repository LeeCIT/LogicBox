


package logicBox.sim.component;
import java.util.List;



/**
 * Interface for active components with pins.
 * Components should return an empty list if they have no pins.
 * @author Lee Coakley
 */
public interface PinIo {
	public List<Pin> getPinInputs();
	public List<Pin> getPinOutputs();
}
