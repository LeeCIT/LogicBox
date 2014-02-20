


package logicBox.sim.component;



/**
 * A pin is an attachment point for traces on a component.
 * @author Lee Coakley
 */
public class Pin extends ComponentPassive
{
	protected Component comp; // Component this is physically attached to.
	protected Trace     trace;
	protected PinIoMode mode;
	protected boolean   state;
	
	
	
	public Pin( Component attachTo, PinIoMode mode ) {
		super();
		this.comp = attachTo;
		this.mode = mode;
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
		return comp;
	}
	
	
	
	public PinIoMode getIoMode() {
		return mode;
	}
	
	
	
	public boolean isInput() {
		return mode == PinIoMode.input;
	}
	
	
	
	public boolean isOutput() {
		return mode == PinIoMode.output;
	}
	
	
	
	public boolean isBidirectional() {
		return mode == PinIoMode.bidi;
	}
	
	
	
	public boolean getState() {
		return state;
	}
	
	
	
	public void setState( boolean state ) {
		this.state = state;
	}
	
	
	
	public String getName() {
		return "Pin";
	}
	
	
	
	public String toString() {
		String onWhat = "";
		
		if (comp != null)
			 onWhat = " [attached: " + comp.getName() + "]";
		
		return "Pin [mode: " + mode + "] [level: " + (state?1:0) + "]" + onWhat;
	}
}














