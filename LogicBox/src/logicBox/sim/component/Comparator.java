


package logicBox.sim.component;

import java.util.ArrayList;
import java.util.List;
import logicBox.gui.editor.GraphicComActive;
import logicBox.gui.editor.GraphicGen;
import logicBox.sim.SimUtil;



/**
 * Comparator.
 * Compares two unsigned binary numbers and sets three output flags: <, ==, >.
 * The pin order is reversed compared to usual, since they're on the top and bottom.
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
		SimUtil.addPins( pinsCom,    this, PinIoMode.input,  bits );
		SimUtil.addPins( pinOutputs, this, PinIoMode.output, 3    );
		
		pinInputs.addAll( pinsRef );
		pinInputs.addAll( pinsCom );
	}
	
	
	
	public String getPinName( PinIoMode mode, int i ) {	
		String str = "Pin ";
		
		if (mode == PinIoMode.input) {
			str += "input: ";
			
			int aMSB = 0;
			int aLSB = pinsRef.size() - 1;
			int bMSB = pinsRef.size();
			int bLSB = getPinInputCount() - 1;
			
			if      (i <= aLSB) str += " A" + (aLSB-i) + getBitSigInfo( i, aLSB, aMSB );
			else if (i <= bLSB) str += " B" + (bLSB-i) + getBitSigInfo( i, bLSB, bMSB );
		}
		else {
			str += "output: ";
			switch (i) {
				case 0: str += "A < B"; break;
				case 1: str += "A = B"; break;
				case 2: str += "A > B"; break;
			}
		}
		
		return str;
	}
	
	
	
	private String getBitSigInfo( int i, int lsb, int msb ) {
		if      (i == lsb) return " (LSB)";
		else if (i == msb) return " (MSB)";
		else               return "";
	}
	
	
	
	public void update() {
		int ref = SimUtil.decodePinsToIntReverse( pinsRef );
		int com = SimUtil.decodePinsToIntReverse( pinsCom );
		
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









