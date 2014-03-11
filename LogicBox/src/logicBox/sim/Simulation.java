


package logicBox.sim;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.*;
import logicBox.sim.component.*;
import logicBox.util.Util;



/**
 * Performs the logic simulation.
 * This version uses a levelisation algorithm and works with combinational circuits only.
 * @author Lee Coakley
 */
public class Simulation implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private List<Component>       comps;   // All sim components
	private List<ComponentActive> actives; // Event-generating components
	private List<Source>          sources; // Primary/const inputs
	
	// Caches are regenerated on deserialisation.
	transient private Set<Net>         cacheNets;
	transient private List<Updateable> cacheUpdateables;
	transient private boolean          cacheInvalidated;
	
	
	
	public Simulation() {
		comps   = new ArrayList<>();
		actives = new ArrayList<>();
		sources = new ArrayList<>();
	}
	
	
	
	/**
	 * Add a component to the simulation.
	 * At the moment, only active components need to be added.
	 */
	public void add( Component...coms ) {
		for (Component com: coms) {
			comps.add( com );
			
			if (com instanceof ComponentActive)
				actives.add( (ComponentActive) com );
			
			if (com instanceof Source)
				sources.add( (Source) com );
		}
		
		cacheInvalidated = true;
	}
	
	
	
	/**
	 * Reset the simulation to its initial state, as if simulate() were never called.
	 */
	public void reset() {		
		for (Net net: getNets())
			for (Component com: net)
				com.reset();
		
		for (Component com: comps)
			com.reset();
	}
	
	
	
	/**
	 * Run the simulation.
	 */
	public void simulate() {
		checkCache();
		
		for (Updateable up: cacheUpdateables) {
			up.update();
			
			if (up instanceof DisplaySevenSeg)
				System.out.println( ((DisplaySevenSeg) up).getNumber() );
		}
	}
	
	
	
	private void checkCache() {
		if (cacheInvalidated)
			regenerateCaches();
	}
	
	
	
	private void regenerateCaches() {
		cacheNets = getNets();
		
		Map<ComponentActive,Integer> comLevelMap = leveliseActives( actives );
		Map<Net,            Integer> netLevelMap = leveliseNets   ( cacheNets, comLevelMap );
		
		cacheUpdateables = sortByEvaluationOrder( comLevelMap, netLevelMap );
		cacheInvalidated = false;
	}
	
	
	
	/**
	 * Group components into islands - regions of the circuit that aren't connected to one another.
	 */
	public List<Island> getIslands() {
		Map<Pin,Net>         pinMap  = mapPinsToNets( getNets() );
		List<Island>         islands = new ArrayList<>();
		Set<ComponentActive> pool    = Util.createIdentityHashSet( actives );
		
		while ( ! pool.isEmpty()) {
			Island          island = new Island();
			ComponentActive origin = pool.iterator().next();
			accumulateIsland( origin, island, pool, pinMap );
			
			islands.add( island );
		}
		
		return islands;
	}
	
	
	
	/**
	 * Recursively accumulate islands by spreading out bidirectionally across nets.
	 */
	private void accumulateIsland( ComponentActive origin, Island island, Set<ComponentActive> pool, Map<Pin,Net> pinMap ) {
		if (island.contains( origin )) // Don't get stuck in feedback loops
			return;
		
		island.add   ( origin );
		pool  .remove( origin );
		
		for (ComponentActive com: getConnectedActives(origin, pinMap))
			accumulateIsland( com, island, pool, pinMap );
	}
	
	
	
	/**
	 * Get all components connected to the one given.
	 */
	private Set<ComponentActive> getConnectedActives( ComponentActive com, Map<Pin,Net> pinMap ) {
		Set<ComponentActive> coms = Util.createIdentityHashSet();
		
		for (Pin pin: com.getPins())
			coms.addAll( pinMap.get(pin).getConnectedActives() );
		
		return coms;
	}
	
	
	
	/**
	 * Map pins to the nets they're in for O(1) lookup.
	 */
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
	 * Components in a net are guaranteed not to be present in any other net (if there are no loops).
	 */
	private Set<Net> getNets() {
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
	 * Test whether the circuit contains any feedback loops.
	 * Doesn't test for the presence of memory.
	 * TODO use islands
	 * @return True if there are no feedback loops.
	 */
	public boolean isLevelisable() {
		Set<Pin> set = Util.createIdentityHashSet();
		return isLevelisable( set, sources.get(0).getPinOutput(0) );
	}
	
	
	
	/**
	 * Recursively search for feedback loops.
	 */
	private boolean isLevelisable( Set<Pin> set, Pin origin ) {
		if (set.contains( origin ))
			return false;
		
		set.add( origin );
		
		for (ComponentActive com: new Net(origin).getFanout())
			for (Pin pin: com.getPinOutputs())
				if ( ! isLevelisable( set, pin ))
					return false;
		
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
	
	
	
	/**
	 * Find the evaluation order for each active component.
	 */
	private Map<ComponentActive,Integer> leveliseActives( List<ComponentActive> actives ) {
		if ( ! isLevelisable())
			throw new RuntimeException( "Can't levelise: circuit contains feedback loops." );
		
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
	
	
	
	/**
	 * Find the evaluation order for nets.
	 * The active component levels must already be known.
	 */
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
		
		return sorted;
	}
	
	
	
	/**
	 * Ensure caches are regenerated when the object is reconstructed.
	 */
	private void readObject( ObjectInputStream in ) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		regenerateCaches();
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
