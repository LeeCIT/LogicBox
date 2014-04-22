


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
	
	private List<Component>       comps;     // All sim components
	private List<ComponentActive> actives;   // Event-generating components
	private List<Source>          sources;   // Primary/const inputs
	private List<BlackBoxPin>     externals; // Components accessible from outside the sim
	
	// Caches are regenerated at deserialisation time.
	transient private Set<Net>         cacheNets;
	transient private List<Updateable> cacheUpdateables;
	transient private boolean          cacheInvalidated;
	
	
	
	public Simulation() {
		comps     = new ArrayList<>();
		actives   = new ArrayList<>();
		sources   = new ArrayList<>();
		externals = new ArrayList<>();
	}
	
	
	
	/**
	 * Get all oscillators in the simulation.
	 * This includes the oscs inside black-boxes, as they all need to be synced.
	 */
	public synchronized Set<SourceOscillator> getOscillators() {
		Set<SourceOscillator> oscs = Util.createIdentityHashSet();
		
		for (Source source: sources)
			if (source instanceof SourceOscillator)
				oscs.add( (SourceOscillator) source );
		
		for (Component com: comps)
			if (com instanceof BlackBox)
				oscs.addAll( ((BlackBox) com).getSimulation().getOscillators() );
		
		return oscs;
	}
	
	
	
	/**
	 * Get all black-box pins in the top-level simulation.
	 */
	public synchronized Set<BlackBoxPin> getBlackboxPins() {
		Set<BlackBoxPin> bbpins = Util.createIdentityHashSet();
		bbpins.addAll( externals );
		return bbpins;
	}
	
	
	
	/**
	 * Add a component to the simulation.
	 * At the moment only active components need to be added, though it does no harm to add everything.
	 */
	public synchronized void add( Component...coms ) {
		for (Component com: coms) {
			comps.add( com );
			
			if (com instanceof ComponentActive) actives  .add( (ComponentActive) com );
			if (com instanceof Source)          sources  .add( (Source)          com );
			if (com instanceof BlackBoxPin)     externals.add( (BlackBoxPin)     com ); // TODO can be a source... argh
		}
		
		cacheInvalidated = true;
	}
	
	
	
	/**
	 * Remove an element from the simulation.
	 * You have to disconnect it separately, or the results will not be what you expect.
	 */
	public synchronized void remove( Component com ) {
		comps    .remove( com );
		actives  .remove( com );
		sources  .remove( com );
		externals.remove( com );
		
		resetIsolatedInputPins();
		
		cacheInvalidated = true;
	}
	
	
	
	private void resetIsolatedInputPins() {
		for (ComponentActive com: actives)
			for (Pin pin: com.getPinInputs())
				if ( ! pin.hasTrace())
					pin.setState( false );
	}
	
	
	
	/**
	 * Remove all elements from the simulation.
	 */
	public synchronized void clear() {
		comps  .clear();
		actives.clear();
		sources.clear();
	}
	
	
	
	/**
	 * True if the sim contains no components.
	 */
	public boolean isEmpty() {
		return comps.isEmpty();
	}
	
	
	
	/**
	 * Reset the simulation to its initial state, as if simulate() were never called.
	 */
	public synchronized void reset() {		
		for (Net net: findNets())
			for (Component com: net)
				com.reset();
		
		for (Component com: comps)
			com.reset();
	}
	
	
	
	/**
	 * Run the simulation for one time step.
	 */
	public synchronized void simulate() {
		if (isEmpty())
			return;
		
		validateCache();
		
		for (Updateable up: cacheUpdateables)
			up.update();
	}
	
	
	
	private void validateCache() {
		if (cacheInvalidated)
			regenerateCaches();
	}
	
	
	
	private void regenerateCaches() {
		Map<ComponentActive,Integer> comLevelMap; 
		Map<Net,            Integer> netLevelMap; 
		
		cacheNets   = findNets();
		comLevelMap = leveliseActives( actives );
		netLevelMap = pruneNets( leveliseNets(cacheNets,comLevelMap) );
		
		cacheUpdateables = sortByEvaluationOrder( comLevelMap, netLevelMap );
		cacheInvalidated = false;
	}
	
	
	
	/**
	 * Group components into islands - regions of the circuit that aren't connected to one another.
	 */
	private List<Island> findIslands() {
		Map<Pin,Net>         pinMap  = mapPinsToNets( findNets() );
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
	 * Find all nets in the circuit.
	 * Components in a net are guaranteed not to be present in any other net (if there are no loops).
	 */
	private Set<Net> findNets() {
		Set<Net> netSet = new HashSet<>();
		
		for (ComponentActive com: actives)
			for (Pin pin: com.getPins())
				netSet.add( new Net(pin) );
			
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
	 * @return True if there are no feedback loops.
	 */
	public synchronized boolean isLevelisable() {
		for (Island island: findIslands())
			if ( ! isLevelisable(island))
				return false;
		
		return true;
	}
	
	
	
	/**
	 * Test whether an island is levelisable.
	 * @see #isLevelisable()
	 */
	private boolean isLevelisable( Island island ) {
		for (ComponentActive com: island)
			if (com.hasInputsConnected())
				for (Pin pin: com.getPinOutputs())
					if ( ! isLevelisableHelper( pin ))
						return false;
		
		return true;
	}
	
	
	
	private boolean isLevelisableHelper( Pin pin ) {
		Set<Pin> set = Util.createIdentityHashSet();
		return isLevelisableHelper( set, pin );
	}
	
	
	
	/**
	 * Recursively search for feedback loops.
	 * If we never touch the same pin twice, we're good.
	 */
	private boolean isLevelisableHelper( Set<Pin> set, Pin origin ) {
		if (set.contains( origin ))
			return false;
		
		set.add( origin );
		
		for (ComponentActive com: new Net(origin).getFanout())
			for (Pin pin: com.getPinOutputs())
				if ( ! isLevelisableHelper( set, pin ))
					return false;
		
		return true;
	}
	
	
	
	/**
	 * Test whether the circuit can be reduced to a truth table. (both levelisable and combinational)
	 * This is still infeasible if there are a lot of inputs though.
	 */
	public synchronized boolean isOptimisable() {
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
			throw new NonLevelisableCircuitException( "Can't levelise: circuit contains feedback loops." );
		
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
	
	
	
	/**
	 * Remove nets with a level of -1 (they can't affect the simulation).
	 */
	private Map<Net,Integer> pruneNets( Map<Net,Integer> netLevels ) { 
		Map<Net,Integer> prune   = new IdentityHashMap<>( netLevels ); 
		Integer          useless = -1;
		
		for (Map.Entry<Net,Integer> en: netLevels.entrySet())
			if (en.getValue().equals( useless ))
				prune.remove( en.getKey() );
		
		return prune;
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
	
	
	
	public String toString() {
		String str = "Simulation with " + actives.size() + " actives.\n\n";
		
		for (Island island: findIslands()) {
			str += "=== Island ===\n";
			
			for (Component com: island)
				str += "\t" + com + "\n";
			
			str += "\n";
		}
		
		return str;
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
		
		if (pinOut != null)	pinOut.connectTrace( trace );
		if (pinIn  != null) pinIn .connectTrace( trace );
		
		return trace;
	}
	
	
	
	
	
	public class NonLevelisableCircuitException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public NonLevelisableCircuitException() {
			super();
		}

		public NonLevelisableCircuitException( String message ) {
			super( message );
		}
	}
}






















