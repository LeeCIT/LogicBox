


package logicBox.sim;



/**
 * Logic levels.
 *         ___
 *        |   |
 *     ___|   |___
 * 
 *    The diagram shows the sequence for a clock pulse:
 *    Low > positive edge > high > negative edge > low.
 *    
 *    Some devices, like flipflops, only change state on the rising edge of a clock pulse.
 *    This must be accounted for in the simulation.
 *    
 * @author Lee Coakley
 */
public enum LogicLevel
{
	low,
	high,
	edgePos,
	edgeNeg
}
