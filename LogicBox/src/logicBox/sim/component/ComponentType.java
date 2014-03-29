


package logicBox.sim.component;



/**
 * Types of components the simulation implements.
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
	pin,
	
	displayLed,
	displaySevenSeg,
	displayLCD,
	
	decoder,
	mux,
	demux,
	
	flipFlopD,
	flipFlopJK,
	flipFlopT,
	
	comparator,
	shifter,
	counter,
	
	register,
	rom,
	
	blackBox,
	blackBoxPin; // Need IoMode and which side of the box they'll be on
}






















