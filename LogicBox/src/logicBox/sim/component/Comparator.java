


package logicBox.sim.component;

import java.util.ArrayList;
import java.util.List;
import logicBox.gui.editor.GraphicComActive;
import logicBox.gui.editor.GraphicGen;
import logicBox.sim.SimUtil;



/**
 * Comparator.
 * Compares two unsigned binary numbers and sets three output flags: <, ==, >
 * @author Lee Coakley
 */
public class Comparator extends ComponentActive
{
	private static final long serialVersionUID = 1L;
	
	private List<Pin> pinsRef;
	private List<Pin> pinsCom;
	
	
	
	public Comparator( int bits ) {
		super();
		
		pinsRef = new ArrayList<>();
		pinsCom = new ArrayList<>();
		
		SimUtil.addPins( pinsRef,    this, PinIoMode.input,  bits );
		SimUtil.addPins( pinsRef,    this, PinIoMode.input,  bits );
		SimUtil.addPins( pinOutputs, this, PinIoMode.output, 3    );
		
		pinInputs.addAll( pinsRef );
		pinInputs.addAll( pinsCom );
	}
	
	
	
	public void update() {
		int ref = SimUtil.decodePinsToInt( pinsRef );
		int com = SimUtil.decodePinsToInt( pinsCom );
		
		boolean lt = ref <  com;
		boolean eq = ref == com;
		boolean gt = ref >  com;
		
		setPinOutputState( 0, lt );
		setPinOutputState( 1, eq );
		setPinOutputState( 2, gt );
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.comparator;
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateComparator( getBitCount() );
	}
	
	
	
	public String getName() {
		return "" + getBitCount() + "-bit Comparator";
	}
	
	
	
	private int getBitCount() {
		return pinsRef.size();
	}
}









