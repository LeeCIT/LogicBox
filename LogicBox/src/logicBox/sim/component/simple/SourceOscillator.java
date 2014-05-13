


package logicBox.sim.component.simple;

import logicBox.gui.editor.graphics.GraphicComActive;
import logicBox.gui.editor.graphics.GraphicGen;
import logicBox.sim.component.ComponentType;
import logicBox.util.Callback;
import logicBox.util.CallbackRepeater;
import logicBox.util.Geo;



/**
 * A clock generator.
 * To make the sim easier to program, its frequency is always a multiple of a common base frequency.
 * All that needs to happen for everything to remain synced properly is for the sim to call
 * sendClockSignal on all oscillators at the same time at the given base frequency.
 * @author Lee Coakley
 */
public class SourceOscillator extends Source
{
	private static final long serialVersionUID = 1L;
	public  static final int  baseFrequencyHz  = 120;
	
	private int cycleCounter;
	private int frequencyDivisor;
	
	
	
	public SourceOscillator( int frequencyDivisor ) {
		super( false );
		setFrequencyDivisor( frequencyDivisor );
	}
	
	
	
	public boolean sendClockSignal() {
		cycleCounter++;
		
		if (cycleCounter < 0) // Handle signed wrap
			cycleCounter = 0;
		
		if (cycleCounter % frequencyDivisor == 0) {
			toggleState();
			return true;
		}
		
		return false;
	}
	
	
	
	private void toggleState() {
		setState( ! getState() );
	}
	
	
	
	public void setFrequencyDivisor( int frequencyDivisor ) {
		checkFrequencyDivisor( frequencyDivisor );
		this.frequencyDivisor = frequencyDivisor;
	}
	
	
	
	private void checkFrequencyDivisor( int frequencyDivisor ) {
		if (frequencyDivisor < 1
		||  frequencyDivisor > baseFrequencyHz)
			throw new RuntimeException( "Bad frequency divider: " + frequencyDivisor );
	}
	
	
	
	public int getFrequency() {
		return baseFrequencyHz / frequencyDivisor;
	}
	
	
	
	public int getFrequencyDivisor() {
		return frequencyDivisor;
	}
	
	
	
	public void reset() {
		super.reset();
		setState( false );
		cycleCounter = 0;
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.sourceOscillator;
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateSourceOscillator();
	}
	
	
	
	public String getName() {
		return "Oscillator (" + getFrequency() + "hz) (click to change frequency)";
	}
	
	
	
	
	
	public static void main( String[] args ) {
		final SourceOscillator osc = new SourceOscillator( 3 );
		System.out.println( osc );
		
		Callback clockCallback = new Callback() {
			public void execute() {
				osc.sendClockSignal();
				System.out.println( osc.getState() ? "1" : "0" );
			}
		};
		
		int frequency = (int) Geo.hertzToMillisecs( SourceOscillator.baseFrequencyHz );
		
		new CallbackRepeater( frequency, clockCallback );
	}
}














