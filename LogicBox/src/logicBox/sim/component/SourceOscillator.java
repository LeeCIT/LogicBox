


package logicBox.sim.component;



/**
 * A clock generator.
 * The make the sim easier to program, its frequency is always a multiple of a common base frequency.
 * @author Lee Coakley
 */
public class SourceOscillator extends Source
{
	public static final int baseFrequencyHz = 200;
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
	
	
	
	public String getName() {
		return "Oscillator (" + (baseFrequencyHz * frequencyDivisor) + "hz)";
	}
}
