


package logicBox.sim;
import java.util.ArrayList;
import java.util.List;
import logicBox.sim.component.Component;
import logicBox.sim.component.Junction;
import logicBox.sim.component.Pin;
import logicBox.sim.component.PinIo;
import logicBox.sim.component.Source;
import logicBox.sim.component.Trace;
import logicBox.sim.component.Updateable;



/**
 * Performs the logic simulation.
 * Note: This is just a placeholder implementation and only works in trivial cases.
 * @author Lee Coakley
 */
public class Simulation
{
	private List<Source>    sources;
	private List<Component> propos;
	private long            simStep;
	
	
	
	public Simulation() {
		sources = new ArrayList<>();
		propos  = new ArrayList<>();
	}
	
	
	
	public void addSource( Source source ) {
		sources.add( source );
	}
	
	
	
	public void run() {
		++simStep;
		
		prime();
		
		while ( ! propos.isEmpty())
			iterate();
	}
	
	
	
	private void prime() {
		propos = new ArrayList<>();
		propos.addAll( sources );
	}
	
	
	
	private void iterate() {
		System.out.println( "Iteration beginning..." );
		List<Component> nextPropos = new ArrayList<>();
		
		while ( ! propos.isEmpty()) {
			Component com = propos.remove( 0 );
			System.out.println( "\tPropogating through: " + com );
			
			if (com instanceof Updateable) {
				Updateable updateable = ((Updateable) com);
				updateable.update();
				System.out.println( "\tUpdated " + com );
			}
			
			if (com instanceof PinIo) {
				PinIo pinIo = ((PinIo) com);
				
				for (Pin pin: pinIo.getPinOutputs()) {
					if (pin.getState()) {
						AffectedPathSet set = getAffectedPath( pin );
						set.setStates( true );
						
						for (Pin term: set.pinTerminators)
							nextPropos.add( term.getAttachedComponent() );
					}
				}
			}
		}
		
		propos.addAll( nextPropos );
		
		System.out.println( "\tNext propogators: " );
		for (Component c: nextPropos)
			System.out.println( "\t\t" + c );
	}
	
	
	
	/**
	 * Find all pins, junctions and traces connected to the given pin.
	 * These are all the components which have their level set by the pin.
	 * The result includes the pin given as input.
	 */
	public AffectedPathSet getAffectedPath( Pin pin ) {
		AffectedPathSet set = new AffectedPathSet();
		getAffectedPath( pin, set );
		return set;
	}
	
	
	
	private void getAffectedPath( Pin pin, AffectedPathSet set ) {
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
		
		if ( ! isConnection(com) && otherPin.isInput())
			set.pinTerminators.add( otherPin );
	}
	
	
	
	private void traverseJunction( Component com, AffectedPathSet set, Pin sourcePin ) {
		Junction junction = (Junction) com;
		
		set.junctions.add( junction );
		
		for (Pin pin: junction.getPinsExcept( sourcePin ))
			getAffectedPath( pin, set );
	}
	
	
	
	private boolean isConnection( Component com ) {
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
	
	
	
	public static Trace connect( PinIo outComp, int outPinIndex, PinIo inComp, int inPinIndex ) {
		Pin pinOut = outComp.getPinOutputs().get( outPinIndex );
		Pin pinIn  = inComp .getPinInputs() .get( inPinIndex  );
		return connectPins( pinOut, pinIn );
	}



	public static Trace connect( Junction outJunc, PinIo inComp, int inPinIndex ) {
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
