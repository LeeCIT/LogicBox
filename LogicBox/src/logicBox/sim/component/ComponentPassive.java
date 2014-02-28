


package logicBox.sim.component;




/**
 * Passive components such as traces and junctions.  These are only signal carriers.
 * @author Lee Coakley
 */
public abstract class ComponentPassive extends Component implements Stateful
{
	protected boolean state;
	
	
	
	public boolean getState() {
		return state;
	}
	
	
	
	public void setState( boolean state ) {
		this.state = state;
	}
	
	
	
	public void reset() {
		setState( false );
	}
}
