


package logicBox.sim;
import java.util.ArrayList;



/**
 * A pin represents an attachment point for traces on a component.
 * @author Lee Coakley
 */
public class Pin extends Component
{
	protected Component internal; // Component this is attached to.
	protected Component external; // Component this is connected to.
	protected boolean   isInput;
	
	
	
	/**
	 * Get the external components at the other end of the connected trace.
	 * There may be many components.
	 * @return A terminating component, or null if none is connected.
	 */
	public ArrayList<Component> getTerminatingComponents() {
		
	}
}
