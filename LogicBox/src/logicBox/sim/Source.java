


package logicBox.sim;



/**
 * A fixed logic-level source.
 * @author Lee Coakley
 */
public class Source extends Component
{
	private boolean state;
	
	
	
	public Source( boolean state ) {
		setState( state );
	}
	
	
	
	public boolean getState() {
		return state;
	}
	
	
	
	public void setState( boolean state ) {
		this.state = state;
	}
}
