


package logicBox.sim.component;



/**
 * Components which change state should implement this. 
 * @author Lee Coakley
 */
public interface Updateable {
	/**
	 * Update internal state and output pin states in response to a simulation update.
	 */
	public void update();
}
