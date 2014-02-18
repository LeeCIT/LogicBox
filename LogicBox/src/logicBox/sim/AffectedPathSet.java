


package logicBox.sim;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import logicBox.sim.component.Component;
import logicBox.sim.component.Junction;
import logicBox.sim.component.Stateful;
import logicBox.sim.component.Trace;



public class AffectedPathSet implements Iterable<Component>
{
	public Set<Junction> junctions      = new HashSet<>();
	public Set<Trace>    traces         = new HashSet<>();
	public Set<Pin>      pins           = new HashSet<>();
	public Set<Pin>      pinTerminators = new HashSet<>(); // Subset of pins
	
	
	
	public void setStates( boolean state ) {
		for (Stateful s: junctions)	s.setState( state );
		for (Stateful s: traces)	s.setState( state );
		for (Stateful s: pins)		s.setState( state );
	}
	
	
	
	public Iterator<Component> iterator() {
		List<Component> list = new ArrayList<>();
		
		list.addAll( junctions );
		list.addAll( traces    );
		list.addAll( pins      );
		
		return list.iterator();
	}
}
