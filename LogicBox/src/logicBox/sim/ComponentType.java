


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
	
	junction,
	trace,
	
	mux,
	demux,
	flipflop,
	
	blackBoxPin // Need IoMode and which side of the box they'll be on...
}
