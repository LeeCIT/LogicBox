


package logicBox.sim.component;



/**
 * Types of components the simulation should implement.
 * This is more for reference than anything.
 * @author Lee Coakley
 */
public enum ComponentType
{
	gateBuffer,
	gateNot,
	gateAnd,
	gateNand,
	gateOr,
	gateNor,
	gateXor,
	gateXnor,
	
	sourceFixed,
	sourceToggle,
	sourceOscillator,
	
	junction,
	trace,
	
	displayLed,
	displaySevenSeg,
	displayLCD,
	
	decoder,
	mux,
	demux,
	
	flipFlopD,
	flipFlopJK,
	
	comparator,
	shifter,
	counter,
	
	register,
	rom,
	
	blackBox,
	blackBoxPin; // Need IoMode and which side of the box they'll be on
}






















