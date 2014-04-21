


package logicBox.sim.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import logicBox.gui.editor.GraphicComActive;
import logicBox.gui.editor.GraphicGen;
import logicBox.util.Util;



/**
 * A powered component with pins that can respond to input and/or provide output.
 * @author Lee Coakley
 */
public abstract class ComponentActive extends Component implements Updateable, PinIo, Graphical
{
	private static final long serialVersionUID = 1L;
	
	protected List<Pin> pinInputs;
	protected List<Pin> pinOutputs;
	
	
	
	public ComponentActive() {
		pinInputs  = new ArrayList<>( 8 );
		pinOutputs = new ArrayList<>( 8 );
	}
	
	
	
	public List<Pin> getPins() {
		List<Pin> pins = new ArrayList<>();
		pins.addAll( getPinInputs () );
		pins.addAll( getPinOutputs() );
		return pins;
	}
	
	
	
	public Pin getPinInput( int index ) {
		return pinInputs.get( index );
	}
	
	
	
	public Pin getPinOutput( int index ) {
		return pinOutputs.get( index );
	}
	
	
	
	public List<Pin> getPinInputs() {
		return pinInputs;
	}
	
	
	
	public List<Pin> getPinOutputs() {
		return pinOutputs;
	}
	
	
	
	public boolean getPinInputState( int index ) {
		return getPinInput(index).getState();
	}
	
	
	
	public boolean getPinOutputState( int index ) {
		return getPinOutput(index).getState();
	}
	
	
	
	public void setPinInputState( int index, boolean state ) {
		getPinInput(index).setState( state );
	}
	
	
	
	public void setPinOutputState( int index, boolean state ) {
		getPinOutput(index).setState( state );
	}
	
	
	
	public boolean hasPinInput() {
		return getPinInputCount() > 0;
	}
	
	
	
	public int getPinInputCount() {
		return getPinInputs().size();
	}
	
	
	
	public boolean hasPinOutput() {
		return getPinOutputCount() > 0;
	}
	
	
	
	public int getPinOutputCount() {
		return getPinOutputs().size();
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generatePlaceholder();
	}
	
	
	
	public void reset() {
		for (Pin pin: getPins())
			pin.reset();
	}
	
	
	
	public void disconnect() {
		for (Pin pin: getPins())
			pin.disconnect();
	}
	
	
	
	public Set<Component> getConnectedComponents() {
		Set<Component> set = Util.createIdentityHashSet();
		
		for (Pin pin: getPins())
			if (pin.hasTrace())
				set.add( pin.getTrace() );
		
		return set;
	}
	
	
	
	public boolean hasInputsConnected() {
		for (Pin pin: getPinInputs())
			if (pin.hasTrace())
				return true;
		
		return false;
	}
	
	
	
	/**
	 * True if the component's state is not dependent on previous inputs.
	 * Override this for components with memory.
	 */
	public boolean isCombinational() {
		return true;
	}
}















