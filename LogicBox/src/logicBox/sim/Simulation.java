


package logicBox.sim;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import logicBox.sim.component.Component;
import logicBox.sim.component.ComponentActive;
import logicBox.sim.component.Junction;
import logicBox.sim.component.Pin;
import logicBox.sim.component.PinIo;
import logicBox.sim.component.Source;
import logicBox.sim.component.Trace;
import logicBox.sim.component.Updateable;
import logicBox.util.Util;



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
	
	private List<Updateable> cacheUpdateables;
	private boolean          cacheInvalidated;
	
	
	
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
		
		cacheInvalidated = true;
	}
	
	
	
	public void reset() {
		for (Component com: comps)
			com.reset();
	}
	
	
	
	public void simulate() {
		if (cacheInvalidated)
			regenerateCaches();
		
		for (Updateable up: cacheUpdateables)
			up.update();
	}
	
	
	
	/**
	 * Test whether the circuit contains any feedback loops.
	 * Doesn't test for the presence of memory.
	 * @return True if there are no feedback loops.
	 */
	public boolean isLevelisable() {
		Set<Component> set = Util.createIdentityHashSet();
		return isLevelisable( set, sources.get(0) );
	}
	
	
	
	private boolean isLevelisable( Set<Component> set, ComponentActive origin ) {
		if (set.contains( origin ))
			return false;
		
		set.add( origin );
		
		for (Pin pin: origin.getPinOutputs()) {
			Net net = new Net( pin );
			
			for (ComponentActive com: net.getFanout()) {
				if ( ! isLevelisable( set, com ))
					return false;
			}
		}
		
		return true;
	}
	
	
	
	/**
	 * Test whether the circuit can be reduced to a truth table. (levelisable and combinational)
	 */
	public boolean isOptimisable() {
		for (ComponentActive com: actives)
			if ( ! com.isCombinational())
				return false;
		
		return isLevelisable();
	}
	
	
	
	private void regenerateCaches() {
		Set<Net>     nets      = getUniqueNetSet();
		Map<Pin,Net> pinNetMap = mapPinsToNets( nets );
		
		Map<ComponentActive,Integer> comLevelMap = leveliseActives( actives );
		Map<Net,            Integer> netLevelMap = leveliseNets   ( nets, comLevelMap );
		
		cacheUpdateables = sortByEvaluationOrder( comLevelMap, netLevelMap );
		cacheInvalidated = false;
	}
	
	
	
	private Map<Pin,Net> mapPinsToNets( Set<Net> nets ) {
		Map<Pin,Net> map = new IdentityHashMap<>();
		
		for (ComponentActive com: actives) {
			List<Pin> pins = new ArrayList<>();
			
			pins.addAll( com.getPinInputs () );
			pins.addAll( com.getPinOutputs() );
			
			for (Pin pin: pins)
				for (Net net: nets)
					if (net.contains( pin ))
						map.put( pin, net );
		}
	
		return map;
	}
	
	
	
	/**
	 * Get all nets in the circuit.
	 * Components in a net are guaranteed not to be present in any other net.
	 */
	private Set<Net> getUniqueNetSet() {
		Set<Net> netSet = new HashSet<>();
		
		for (ComponentActive com: actives) {
			List<Pin> pins = new ArrayList<>( com.getPinInputs() );
			pins.addAll( com.getPinOutputs() );
			
			for (Pin pin: pins)
				netSet.add( new Net(pin) );
		}
			
		return netSet;
	}
	
	
	
	private <T> Map<T,Integer> genBaseLevelMap( Iterable<T> items ) {
		Map<T,Integer> map = new IdentityHashMap<>();
		
		for (T item: items)
			map.put( item, -1 );
		
		return map;
	}
	
	
	
	/**
	 * Generate a list of active components sorted in evaluation order.
	 */
	private Map<ComponentActive,Integer> leveliseActives( List<ComponentActive> actives ) {
		Map<ComponentActive,Integer> levels   = genBaseLevelMap( actives );
		Deque<ComponentActive>       deferred = new ArrayDeque<>( actives );
		
		while ( ! deferred.isEmpty()) {
			ComponentActive com = deferred.removeFirst();
			
			boolean allInputsHaveLevels = true;
			int     maxLevel            = -1;
			
			for (Pin comPinInput: com.getPinInputs()) {
				Net net = new Net( comPinInput );
				
				for (ComponentActive comDependency: net.getFanin()) {
					int comLevel = levels.get( comDependency );
					maxLevel = Math.max( maxLevel, comLevel );
					allInputsHaveLevels &= (comLevel != -1);
				}
			}
			
			if (allInputsHaveLevels)
				levels.put( com, maxLevel + 1 );
			else deferred.addLast( com );
		}
		
		return levels;
	}
	
	
	
	private Map<Net,Integer> leveliseNets( Iterable<Net> nets, Map<ComponentActive,Integer> activeLevels ) {
		Map<Net,Integer> levels = genBaseLevelMap( nets );
		
		for (Net net: nets) {
			int maxLevel = -1;
			
			for (ComponentActive com: net.getFanin())
				maxLevel = Math.max( maxLevel, activeLevels.get(com) );
			
			levels.put( net, maxLevel );
		}
		
		return levels;
	}
	
	
	
	private <T> List<T> sortByLevel( Iterable<T> sortThis, final Map<T,Integer> map ) {
		List<T> sorted = new ArrayList<>();
		
		for (T item: sortThis)
			sorted.add( item );
		
		Collections.sort( sorted, new Comparator<T>() {
			public int compare( T a, T b ) {
				return map.get(a) - map.get(b);
			}
		});
		
		return sorted;
	}
	
	
	
	private List<Updateable> sortByEvaluationOrder( Map<ComponentActive,Integer> comLevels, Map<Net,Integer> netLevels ) {
		final Map<Updateable,Integer> map = new IdentityHashMap<>();
		map.putAll( comLevels );
		map.putAll( netLevels );
		
		List<Updateable> sorted = new ArrayList<>();
		sorted.addAll( map.keySet() );
		
		Collections.sort( sorted, new Comparator<Updateable>() {
			public int compare( Updateable a, Updateable b ) {
				int     levA   = map.get( a );
				int     levB   = map.get( b );
				boolean aIsCom = (a instanceof ComponentActive);
				boolean bIsCom = (b instanceof ComponentActive);
				
				if (levA != levB) {
					return levA - levB;
				} else { // Coms must come before nets of the same level
					if (aIsCom == bIsCom) { // Both are the same type and level, so order doesn't matter
						return 0;
					} else { 
						if (aIsCom && !bIsCom) // Prioritise coms
							 return -1;
						else return +1;
					}
				}
			}
		});
		
		for (Updateable u: sorted)
			System.out.println( "Level " + map.get(u) + ": " + u );
		
		return sorted;
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
