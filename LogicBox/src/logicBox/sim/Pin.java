


package logicBox.sim;



/**
 * A pin is an attachment point for traces on a component.
 * @author Lee Coakley
 */
public class Pin extends Component implements Stateful
{
	protected Component component; // Component this is physically attached to.
	protected Trace     trace;
	protected IoMode    mode;
	protected boolean   state;
	
	
	
	public Pin( Component attachTo, IoMode mode ) {
		super();
		this.component = attachTo;
		this.mode      = mode;
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
		return mode == IoMode.input;
	}
	
	
	
	public boolean isOutput() {
		return mode == IoMode.output;
	}
	
	
	
	public boolean isBidirectional() {
		return mode == IoMode.bidi;
	}
	
	
	
	public boolean getState() {
		return state;
	}
	
	
	
	public void setState( boolean state ) {
		this.state = state;
	}
}
