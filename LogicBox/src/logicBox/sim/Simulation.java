


package logicBox.sim;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import logicBox.sim.component.Component;
import logicBox.sim.component.ComponentActive;
import logicBox.sim.component.Junction;
import logicBox.sim.component.Pin;
import logicBox.sim.component.PinIo;
import logicBox.sim.component.Source;
import logicBox.sim.component.Trace;



/**
 * Performs the logic simulation.
 * This version uses a levelisation algorithm and works with combinational circuits only.
 * @author Lee Coakley
 */
public class Simulation
{
	private List<Component>       comps;   // All sim components
	private List<ComponentActive> actives; // Event-generating components
	private List<Source>          sources; // Primary/const inputs
	
	
	
	public Simulation() {
		comps   = new ArrayList<>();
		actives = new ArrayList<>();
		sources = new ArrayList<>();
	}
	
	
	
	public void add( Component com ) {
		comps.add( com );
		
		if (com instanceof ComponentActive)
			actives.add( (ComponentActive) com );
		
		if (com instanceof Source)
			sources.add( (Source) com );
	}
	
	
	
	public void reset() {
		for (Component com: comps)
			com.reset();
	}
	
	
	
	public void simulate() { // TODO optimise away net generation
		List<ComponentActive> evals = levelise();
		
		for (ComponentActive com: evals) {
			com.update();
			
			for (Pin pin: com.getPinOutputs()) {
				Net net = new Net( pin );
				net.orState( pin.getState() );
			}
		}
	}
	
	
	
	private Map<Component,Integer> genBaseLevelMap() {
		Map<Component,Integer> map = new IdentityHashMap<>();
		
		for (Component com: comps) {
			int level = (com instanceof Source) ? 0 : -1; // Sources are always first
			map.put( com, level );
		}
		
		return map;
	}
	
	
	
	private int levelOf( Map<Component,Integer> map, Component com ) {
		return map.get( com );
	}
	
	
	
	private int setLevel( Map<Component,Integer> map, Component com, int level ) {
		return map.put( com, level );
	}
	
	
	
	/**
	 * Generate a list of active components sorted for evaluation order.
	 */
	private List<ComponentActive> levelise() {
		Map<Component,Integer> map      = genBaseLevelMap();
		List<ComponentActive>  deferred = new ArrayList<>( actives );
		
		while ( ! deferred.isEmpty()) {
			ComponentActive com = deferred.remove( 0 );
			
			boolean allInputsHaveLevels = true;
			int     maxLevel            = -1;
			
			for (Pin comPinInput: com.getPinInputs()) {
				Net net = new Net( comPinInput );
				
				for (ComponentActive comDependency: net.getFanin()) {
					int comLevel = levelOf( map, comDependency );
					maxLevel = Math.max( maxLevel, comLevel );
					allInputsHaveLevels &= (comLevel != -1);
				}
			}
			
			if (allInputsHaveLevels)
				 setLevel( map, com, maxLevel + 1 );
			else deferred.add( com );
		}
		
		return sortByLevel( actives, map );
	}
	
	
	
	private List<ComponentActive> sortByLevel( List<ComponentActive> actives, final Map<Component,Integer> map ) {
		List<ComponentActive> awYeahSon = new ArrayList<>( actives );
		
		Collections.sort( awYeahSon, new Comparator<ComponentActive>() {
			public int compare( ComponentActive a, ComponentActive b ) {
				return map.get(a) - map.get(b);
			}
		});
		
		return awYeahSon;
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
		Pin pinOut = outComp.getPinOutput( outPinIndex );
		Pin pinIn  = inComp .getPinInput ( inPinIndex  );
		return connectPins( pinOut, pinIn );
	}



	public static Trace connect( Junction outJunc, PinIo inComp, int inPinIndex ) {
		Pin pinOut = outJunc.createPin();
		Pin pinIn  = inComp.getPinInput( inPinIndex );
		return connectPins( pinOut, pinIn );
	}
	
	
	
	public static Trace connectPins( Pin pinOut, Pin pinIn ) {
		Trace trace = new Trace( pinOut, pinIn );
		pinOut.connectTrace( trace );
		pinIn .connectTrace( trace );
		return trace;
	}
}
