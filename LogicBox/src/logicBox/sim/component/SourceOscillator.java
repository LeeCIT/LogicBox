


package logicBox.sim.component;

import logicBox.gui.editor.GraphicComActive;
import logicBox.gui.editor.GraphicGen;



/**
 * A clock generator.
 * To make the sim easier to program, its frequency is always a multiple of a common base frequency.
 * All that needs to happen for everything to remain synced properly is for the sim to call
 * sendClockSignal on all oscillators at the given base frequency.
 * @author Lee Coakley
 */
public class SourceOscillator extends Source
{
	private static final long serialVersionUID = 1L;
	public  static final int  baseFrequencyHz  = 200;
	
	private int cycleCounter;
	private int frequencyDivisor;
	
	
	
	public SourceOscillator( int frequencyDivisor ) {
		super( false );
		setFrequencyDivisor( frequencyDivisor );
	}
	
	
	
	public synchronized void sendClockSignal() {
		if (cycleCounter++ % frequencyDivisor == 0)
			toggleState();
	}
	
	
	private void toggleState() {
		setState( ! getState() );
	}
	
	
	
	public void setFrequencyDivisor( int frequencyDivisor ) {
		this.frequencyDivisor = frequencyDivisor;
	}
	
	
	
	public int getFrequencyDivisor() {
		return frequencyDivisor;
	}
	
	
	
	public void reset() {
		super.reset();
		cycleCounter = 0;
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
