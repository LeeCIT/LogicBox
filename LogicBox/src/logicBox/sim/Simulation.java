


package logicBox.sim;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



/**
 * Performs the logic simulation.
 * @author Lee Coakley
 */
public class Simulation
{
	private List<Source> sources;
	private long         simStep;
	
	
	
	public Simulation() {
		sources = new ArrayList<>();
	}
	
	
	
	public void addSource( Source source ) {
		sources.add( source );
	}
	
	
	
	public void run() {
		++simStep;
		
		// TODO
	}
	
	
	
	public void propogateSignal( PinOut out ) {
		for (Pin pin: out.getPinOutputs())
			pin.trace.setState( true );
	}
	
	
	
	public class AffectedComponentSet {
		public Set<Junction> junctions      = new HashSet<>();
		public Set<Trace>    traces         = new HashSet<>();
		public Set<Pin>      pins           = new HashSet<>();
		public Set<Pin>      pinTerminators = new HashSet<>();
	}
	
	
	
	/**
	 * Find all pins, junctions and traces connected to the given pin.
	 * These are all the components which have their level set by the pin.
	 */
	public AffectedComponentSet getAffectedComponents( Pin pin ) {
		AffectedComponentSet set = new AffectedComponentSet();
		getAffectedComponents( pin, set );
		return set;
	}
	
	
	
	private void getAffectedComponents( Pin pin, AffectedComponentSet set ) {
		if ( ! pin.hasTrace())
			return;
		
		Trace trace    = pin.getTrace();
		Pin   otherPin = trace.getPinOtherSide( pin );
		
		set.pins  .add( pin );
		set.traces.add( trace );
		set.pins  .add( otherPin );
		
		Component com = otherPin.getAttachedComponent();
		if (com instanceof Junction)
			traverseJunction( com, set, otherPin );
		
		if ( ! isTransport( com ))
			set.pinTerminators.add( otherPin );
	}
	
	
	
	private void traverseJunction( Component com, AffectedComponentSet set, Pin sourcePin ) {
		Junction junction = (Junction) com;
		
		set.junctions.add( junction );
		
		for (Pin pin: junction.getPinsExcept( sourcePin ))
			getAffectedComponents( pin, set );
	}
	
	
	
	private boolean isTransport( Component com ) {
		return com instanceof Junction 
			|| com instanceof Trace;
	}
	
	
	
	/**
	 * Insert a junction into a trace.
	 * Two new traces are created and connected in place of the former one.
	 */
	public static Junction insertJunction( Trace trace ) {
		Junction junc = new Junction();
		
		Pin sourcePin = trace.getPinSource();
		Pin destPin   = trace.getPinDest();
		
		Pin sourcePinJunc = junc.createPin();
		Pin destPinJunc   = junc.createPin();
		
		Trace sourceToJunc = new Trace( sourcePin,   sourcePinJunc );
		Trace destToJunc   = new Trace( destPinJunc, destPin       );
		
		sourcePin    .connectTrace( sourceToJunc );
		sourcePinJunc.connectTrace( sourceToJunc );
		destPin      .connectTrace( destToJunc   );
		destPinJunc  .connectTrace( destToJunc   );
		
		return junc;
	}
	
	
	
	public static Trace connect( PinOut outComp, int outPinIndex, PinIn inComp, int inPinIndex ) {
		Pin pinOut = outComp.getPinOutputs().get( outPinIndex );
		Pin pinIn  = inComp .getPinInputs() .get( inPinIndex  );
		return connectPins( pinOut, pinIn );
	}



	public static Trace connect( Junction outJunc, PinIn inComp, int inPinIndex ) {
		Pin pinOut = outJunc.createPin();
		Pin pinIn  = inComp.getPinInputs().get( inPinIndex );
		return connectPins( pinOut, pinIn );
	}
	
	
	
	public static Trace connectPins( Pin pinOut, Pin pinIn ) {
		Trace trace = new Trace( pinOut, pinIn );
		pinOut.connectTrace( trace );
		pinIn .connectTrace( trace );
		return trace;
	}
}
