


package logicBox.sim.component;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import logicBox.gui.editor.GraphicComActive;
import logicBox.sim.Optimiser;
import logicBox.sim.SimUtil;
import logicBox.sim.Simulation;
import logicBox.util.Util;



/**
 * A component which encapsulates a whole other simulation.
 * @author Lee Coakley
 */
public class BlackBox extends ComponentActive
{
	private static final long serialVersionUID = 1L;
	
	private String               name;
	private Simulation           sim;
	private Map<Pin,BlackBoxPin> pinMap;
	private Map<Pin,BlackBoxPin> pinMapIn;
	private Map<Pin,BlackBoxPin> pinMapOut;
	private GraphicComActive     graphic; // TODO this is nasty, having a graphic in the sim...
	
	private boolean[][] lookupTable;
	
	
	
	public BlackBox( String name, Simulation sim, int inputPinCount, int outputPinCount ) {
		super();
		this.name = name;
		this.sim  = sim;
		
		SimUtil.addPins( pinInputs,  this, PinIoMode.input,  inputPinCount  );
		SimUtil.addPins( pinOutputs, this, PinIoMode.output, outputPinCount );
	}
	
	
	
	/**
	 * Try to optimise the circuit so it's just a table lookup.
	 * This can fail, in which case nothing changes.
	 */
	public void optimise() {
		try {
			boolean canOptimise = sim.isOptimisable();
		
			if (canOptimise) {
				lookupTable = Optimiser.generateLookupTable( this );
				sim         = null; // Pretty heavyweight object, so throw it away
				pinMap      = null;
				pinMapIn    = null;
				pinMapOut   = null;
			}
		}
		catch (Optimiser.NonOptimisableComponentException ex) {
			// Do nothing.  The BB will just be simulated instead.
		}
	}
	
	
	
	/**
	 * Whether the internal simulation has been reduced to a truth table.
	 * If so, there is no simulation anymore and getSimulation() will return null.
	 */
	public boolean isOptimised() {
		return (lookupTable != null);
	}
	
	
	
	public void setPinMap( Map<Pin,BlackBoxPin> pinMap ) {
		this.pinMap    = pinMap;
		this.pinMapIn  = new IdentityHashMap<>();
		this.pinMapOut = new IdentityHashMap<>();
		
		for (Pin pin: pinMap.keySet()) {
			     if (pin.isInput ()) pinMapIn .put( pin, pinMap.get(pin) );
			else if (pin.isOutput()) pinMapOut.put( pin, pinMap.get(pin) );
		}
	}
	
	
	
	public Map<Pin,BlackBoxPin> getPinMap() {
		return pinMap;
	}
	
	
	
	public void setGraphic( GraphicComActive graphic ) {
		this.graphic = graphic;
	}
	
	
	
	public Simulation getSimulation() {
		return sim;
	}
	
	
	
	public Set<SourceOscillator> getOscillators() {
		if (sim != null)
			sim.getOscillators();
		
		return new HashSet<>();
	}
	
	
	
	public void update() {
		if (isOptimised())
			 updateByTableLookup();
		else updateBySimulation();
	}
	
	
	
	private void updateBySimulation() {
		applyPinStates( pinMapIn, true );
		sim.simulate();
		applyPinStates( pinMapOut, false );
	}
	
	
	
	private void updateByTableLookup() {
		int       in     = SimUtil.decodePinsToInt( pinInputs );
		boolean[] states = lookupTable[ in ];
		
		for (int i=0; i<states.length; i++)
			pinOutputs.get(i).setState( states[i] );
	}
	
	
	
	public void reset() {
		super.reset();
		
		if (sim != null)
			sim.reset();
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.blackBox;
	}
	
	
	
	public String getName() {
		return "Black-box: " + name;
	}
	
	
	
	public GraphicComActive getGraphic() {
		return Util.deepCopy( graphic );
	}
	
	
	
	public boolean isCombinational() {
		if (isOptimised())
			 return true;
		else return sim.isOptimisable();
	}
	
	
	
	private void applyPinStates( Map<Pin,BlackBoxPin> map, boolean in ) {
		for (Map.Entry<Pin,BlackBoxPin> entry: map.entrySet()) {
			Pin         pin   = entry.getKey();
			BlackBoxPin bbPin = entry.getValue();
			
			if (in)
				 bbPin.setState( pin  .getState() ); 
			else pin  .setState( bbPin.getState() ); 
		}
	}
}
