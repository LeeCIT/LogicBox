


package logicBox.sim.component;



/**
 * A powered component with pins that can respond to input and/or provide output.
 * @author Lee Coakley
 */
public abstract class ComponentActive extends Component implements Updateable, PinIo
{
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
}
