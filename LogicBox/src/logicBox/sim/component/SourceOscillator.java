


package logicBox.sim.component;

import logicBox.gui.editor.GraphicComActive;
import logicBox.gui.editor.GraphicGen;



/**
 * A clock generator.
 * To make the sim easier to program, its frequency is always a multiple of a common base frequency.
 * @author Lee Coakley
 */
public class SourceOscillator extends Source
{
	private static final long serialVersionUID = 1L;
	public  static final int  baseFrequencyHz  = 200;
	
	private int frequencyDivisor;
	
	
	
	public SourceOscillator( int frequencyDivisor ) {
		super( false );
		this.frequencyDivisor = frequencyDivisor;
	}
	
	
	
	public void toggleState() {
		setState( ! getState() );
	}
	
	
	
	public int getFrequencyDivisor() {
		return frequencyDivisor;
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.sourceOscillator;
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateSourceOscillator();
	}
	
	
	
	public String getName() {
		return "Oscillator (" + (baseFrequencyHz * frequencyDivisor) + "hz)";
	}
}
