


package logicBox.sim;



/**
 * Performs the logic simulation.
 * @author Lee Coakley
 */
public class Simulation
{
	/**
	 * Insert an interconnect into a trace.
	 * Two new traces are created and connected in place of the former one.
	 */
	public static Interconnect insertInterconnect( Trace trace ) {
		Interconnect ic = new Interconnect();
		
		Pin sourcePin   = trace.getPinSource();
		Pin destPin     = trace.getPinDest();
		
		Pin sourcePinIc = ic.createPin();
		Pin destPinIc   = ic.createPin();
		
		Trace sourceToIc = new Trace( sourcePin, sourcePinIc );
		Trace destToIc   = new Trace( destPinIc, destPin     );
		
		sourcePin  .connectTrace( sourceToIc );
		sourcePinIc.connectTrace( sourceToIc );
		destPin    .connectTrace( destToIc   );
		destPinIc  .connectTrace( destToIc   );
		
		return ic;
	}
}
