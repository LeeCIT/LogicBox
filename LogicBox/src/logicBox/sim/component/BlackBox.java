


package logicBox.sim.component;

import logicBox.sim.Simulation;



/**
 * A component which encapsulates a whole other simulation.
 * // TODO interface with simulation via a new pin class
 * @author Lee Coakley
 */
public class BlackBox extends ComponentActive
{
	private static final long serialVersionUID = 1L;
	
	private Simulation sim;
	private String     name;
	
	
	
	public BlackBox( String name, Simulation sim ) {
		super();
		this.sim  = sim;
		this.name = name;
	}
	
	
	
	public void update() {
		sim.simulate();
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.blackBox;
	}
	
	
	
	public String getName() {
		return name;
	}
}
