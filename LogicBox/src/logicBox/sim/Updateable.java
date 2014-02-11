
package logicBox.sim;



/**
 * Components which change state should implement this. 
 * @author Lee Coakley
 */
public interface Updateable {
	/**
	 * Update output pin states based on inputs.
	 */
	public void update();
}

