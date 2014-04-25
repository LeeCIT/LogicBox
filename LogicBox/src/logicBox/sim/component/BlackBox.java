


package logicBox.sim.component;

import java.util.IdentityHashMap;
import java.util.Map;
import logicBox.gui.editor.GraphicComActive;
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
	
	
	
	public BlackBox( String name, Simulation sim, int inputPinCount, int outputPinCount ) {
		super();
		this.name = name;
		this.sim  = sim;
		
		SimUtil.addPins( pinInputs,  this, PinIoMode.input,  inputPinCount  );
		SimUtil.addPins( pinOutputs, this, PinIoMode.output, outputPinCount );
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
	
	
	
	public void setGraphic( GraphicComActive graphic ) {
		this.graphic = graphic;
	}
	
	
	
	public Simulation getSimulation() {
		return sim;
	}
	
	
	
	public Map<Pin,BlackBoxPin> getPinMap() {
		return pinMap;
	}
	
	
	
	public void update() {
		applyPinStates( pinMapIn, true );
		sim.simulate();
		applyPinStates( pinMapOut, false );
	}
	
	
	
	public void reset() {
		super.reset();
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
