


package logicBox.sim.component.memory;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import logicBox.gui.editor.graphics.GraphicComActive;
import logicBox.gui.editor.graphics.GraphicGen;
import logicBox.sim.SimUtil;
import logicBox.sim.component.ComponentType;
import logicBox.sim.component.PinIoMode;
import logicBox.sim.component.connective.Pin;
import logicBox.util.Geo;



/**
 * A chunk of random access memory.
 * Characterised by a word size and capacity. 
 * 
 * @author Lee Coakley
 */
public class RAM extends EdgeTriggered
{
	private List<Pin> pinAddress;
	private List<Pin> pinWordIn;
	private List<Pin> pinWordOut;
	
	private int    addressBits;
	private int    wordBits;
	private int    wordCount;
	private BitSet data;
	
	private int nextState;
	
	
	
	public RAM ( int wordBits, int wordCount ) {
		this.addressBits = Geo.log2i( wordCount );
		this.wordBits    = wordBits;
		this.wordCount   = wordCount;
		this.data        = new BitSet( wordBits * wordCount );
		
		pinAddress = new ArrayList<>();
		pinWordIn  = new ArrayList<>();
		pinWordOut = new ArrayList<>();
		
		SimUtil.addPins( pinInputs,  this, PinIoMode.input,  3           ); // R/C/W
		SimUtil.addPins( pinAddress, this, PinIoMode.input,  addressBits );
		SimUtil.addPins( pinWordIn,  this, PinIoMode.input,  wordBits    );
		SimUtil.addPins( pinWordOut, this, PinIoMode.output, wordBits    );
		pinInputs .addAll( pinWordIn  );
		pinInputs .addAll( pinAddress );
		pinOutputs.addAll( pinWordOut );
	}
	
	
	
	public void reset() {
		super.reset();
		dataClear();
	}
	
	
	
	public Pin getPinWrite() {
		return getPinInput( 0 );
	}
	
	
	
	public Pin getPinClock() {
		return getPinInput( 1 );
	}
	
	
	
	public Pin getPinReset() {
		return getPinInput( 2 );
	}
	
	
	
	protected void onPositiveEdge() {
		if (getPinReset().getState()) {
			dataClear();
			nextState = 0;
		}
		else {
			int address = SimUtil.decodePinsToInt( pinAddress );
			
			if (getPinWrite().getState()) {
				int word = SimUtil.decodePinsToInt( pinWordIn );
				dataWrite( address, word );
			}
			
			nextState = dataRead( address );
		}
	}
	
	
	
	protected void onNegativeEdge() {
		SimUtil.encodeIntToPins( nextState, pinWordOut );
	}
	
	
	
	private int dataRead( int address ) {
		int offset = address * wordBits;
		int word   = 0;
		
		for (int i=0; i<wordBits; i++) {
			int bit = data.get(i+offset) ? 1 : 0;
			word |= bit << i;
		}
		
		return word;
	}
	
	
	
	private void dataWrite( int address, int word ) {
		int offset = address * wordBits;
		
		for (int i=0; i<wordBits; i++) {
			int bit = (word >> i) & 1;
			data.set( i+offset, bit==1 );
		}
	}
	
	
	
	private void dataClear() {
		data.clear();
	}
	
	
	
	public ComponentType getType() {
		return ComponentType.ram;
	}
	
	
	
	public GraphicComActive getGraphic() {
		return GraphicGen.generateRam( wordBits, addressBits ); 
	}
	
	
	
	public String getName() {
		return "" + wordBits + "-bit RAM  (" + wordBits + " x " + wordCount + " words = " + describeCapacity() + ")";
	}
	
	
	
	private String describeCapacity() {
		int    bits   = wordBits * wordCount;
		String suffix = "bits";
		
		int m = 1_000_000;
		int k =     1_000;
		
		if      (bits >= m) { bits /= m;  suffix = "M bits"; }
		else if (bits >= k) { bits /= k;  suffix = "K bits"; }
		
		return "" + bits + " " + suffix;
	}
	
	
	
	
	
	public static void main( String[] args ) {
		RAM ram = new RAM( 4, 16 );
		System.out.println( ram + "\n" );
		
		ram.dataClear();
		ram.dataWrite( 3, 14 );
		
		for (int i=0; i<16; i++)
			System.out.println( "" + i + ":\t" + ram.dataRead(i)  );
	}
}



















