


package logicBox.sim.component;

import java.util.Map;
import logicBox.sim.Simulation;



/**
 * A component which encapsulates a whole other simulation.
 * TODO map pins to sim blackbox pins
 * @author Lee Coakley
 */
public class BlackBox extends ComponentActive
{
	private static final long serialVersionUID = 1L;
	
	private String               name;
	private Simulation           sim;
	private Map<Pin,BlackBoxPin> pinMap;
	
	
	
	public BlackBox( String name, Simulation sim, Map<Pin,BlackBoxPin> pinMap ) {
		super();
		this.name   = name;
		this.sim    = sim;
		this.pinMap = pinMap;
	}
	
	
	
	public Simulation getSimulation() {
		return sim;
	}
	
	
	
	public Map<Pin,BlackBoxPin> getPinMap() {
		return pinMap;
	}
	
	
	
	public void update() {
		sim.simulate();
		
		for (Map.Entry<Pin,BlackBoxPin> entry: pinMap.entrySet()) {
			Pin         pin   = entry.getKey();
			BlackBoxPin bbpin = entry.getValue();
			pin.setState( bbpin.getState() );
		}
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
}
