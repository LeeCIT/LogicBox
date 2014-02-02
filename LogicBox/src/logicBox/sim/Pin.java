


package logicBox.sim;



/**
 * A pin is an attachment point for traces on a component.
 * @author Lee Coakley
 */
public class Pin extends Component implements Stateful
{
	protected Component component; // Component this is physically attached to.
	protected Trace     trace;
	protected boolean   isInput;
	protected boolean   state;
	
	
	
	public Pin( Component attachTo, boolean isInput ) {
		super();
		this.component = attachTo;
	}
	
	
	
	/**
	 * Connect a trace to this pin.
	 * Any existing connection is lost.
	 */
	public void connectTrace( Trace trace ) {
		this.trace = trace;
	}
	
	
	
	public Trace getTrace() {
		return trace;
	}
	
	
	
	public boolean hasTrace() {
		return trace != null;
	}
	
	
	
	/**
	 * Get the component this pin is physically attached to.
	 */
	public Component getAttachedComponent() {
		return component;
	}
	
	
	
	public boolean isInput() {
		return this.isInput;
	}
	
	
	
	public boolean isOutput() {
		return ! isInput();
	}
	
	
	
	/**
	 * Check whether this pin has Input/Output status.
	 * If not, the isInput() and isOutput() functions are meaningless and the pins are bidirectional.
	 */
	public boolean hasIoState() {
		return ! (component instanceof Junction);
	}
	
	
	
	public boolean getState() {
		return state;
	}
	
	
	
	public void setState( boolean state ) {
		this.state = state;
	}
}
