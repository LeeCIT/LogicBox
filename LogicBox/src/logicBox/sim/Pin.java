


package logicBox.sim;



/**
 * A pin is an attachment point for traces on a component.
 * @author Lee Coakley
 */
public class Pin extends Component
{
	protected Component internal; // Component this is attached to.
	protected Component external;
	protected boolean   isInput;
	protected boolean   state;
	
	
	
	public Pin( Component attachTo, boolean isInput ) {
		this.internal = attachTo;
	}
	
	
	
	public void connectPin( Component com ) {
		external = com;
	}
	
	
	
	public boolean isInput() {
		return this.isInput;
	}
	
	
	
	public boolean isOutput() {
		return ! isInput();
	}
	
	
	
	public boolean getState() {
		return state;
	}
	
	
	
	public void setState( boolean state ) {
		this.state = state;
	}
}
