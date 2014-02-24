


package logicBox.sim.component;

import logicBox.gui.editor.GraphicComActive;
import logicBox.gui.editor.GraphicGen;



/**
 * A powered component with pins that can respond to input and/or provide output.
 * TODO considering unifying the whole separate I/O thing.
 * @author Lee Coakley
 */
public abstract class ComponentActive extends Component implements Updateable, PinIo, Graphical
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
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generatePlaceholder();
	}
}
