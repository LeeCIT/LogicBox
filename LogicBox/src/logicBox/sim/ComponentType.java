


package logicBox.sim;



/**
 * Types of components the simulation should implement.
 * This is more for reference than anything.
 * @author Lee Coakley
 */
public enum ComponentType
{
	gateRelay,
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
	
	mux,
	demux,
	flipFlopD,
	
	blackBox,
	blackBoxPin; // Need IoMode and which side of the box they'll be on...
}
