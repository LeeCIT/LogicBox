


package logicBox.sim;

import java.util.ArrayList;
import java.util.List;



/**
 * Performs the logic simulation.
 * @author Lee Coakley
 */
public class Simulation
{
	List<Source> sources;
	
	
	public Simulation() {
		sources = new ArrayList<>();
	}
	
	
	
	public void addSource( Source source ) {
		sources.add( source );
	}
	
	
	
	public void run() {
		// TODO
	}
	
	
	
	/**
	 * Insert a junction into a trace.
	 * Two new traces are created and connected in place of the former one.
	 */
	public static Junction insertJunction( Trace trace ) {
		Junction ic = new Junction();
		
		Pin sourcePin = trace.getPinSource();
		Pin destPin   = trace.getPinDest();
		
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
