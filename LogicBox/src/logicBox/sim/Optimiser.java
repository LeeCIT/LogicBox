


package logicBox.sim;

import java.util.List;
import logicBox.sim.component.*;
import logicBox.sim.component.Pin;
import logicBox.util.Util;



/**
 * Produces lookup tables from combinational circuits.
 * @author Lee Coakley
 */
public abstract class Optimiser
{
	private static final int maxInputs  = 12;    // 4096 combinations
	private static final int maxOutputs = 32;    // Constrained by int type
	private static final int maxBytes   = 1<<14; // 16KB
	
	
	
	public static int[] generateLookupTable( ComponentActive com ) {
		com = Util.deepCopy( com );
		
		checkOptimisability( com );
		
		int inCount      = com.getPinInputCount();
		int combinations = getCombinations( inCount );
		
		int[]     table   = new int[combinations];
		List<Pin> inputs  = com.getPinInputs();
		List<Pin> outputs = com.getPinOutputs();
		
		for (int i=0; i<combinations; i++) {
			SimUtil.encodeIntToPins( i, inputs );
			com.update();
			table[i] = SimUtil.decodePinsToInt( outputs );
		}
		
		return table;
	}
	
	
	
	private static int getCombinations( int ins ) {
		return (int) Math.pow( 2, ins );
	}
	
	
	
	private static String toBitString( int i ) {
		String str = Integer.toBinaryString( i );
		
		while (str.length() < 32)
			str = "0" + str;
		
		return str;
	}
	
	
	
	private static void checkOptimisability( ComponentActive com ) {
		if ( ! com.isCombinational())
			throw new NonOptimisableComponentException( "Not a combinational circuit." );
		
		int in  = com.getPinInputCount();
		int out = com.getPinOutputCount();
		
		if (in > maxInputs)
			throw new NonOptimisableComponentException( "Too many inputs." );
		
		if (out > maxOutputs)
			throw new NonOptimisableComponentException( "Too many outputs." );
		
		if (out * getCombinations(in) > maxBytes)
			throw new NonOptimisableComponentException( "Lookup table would exceed max size." );
	}
	
	
	
	
	
	public static class NonOptimisableComponentException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		
		public NonOptimisableComponentException() {
			super();
		}
		
		public NonOptimisableComponentException( String message ) {
			super( message );
		}
	}
	
	
	
	
	
	public static void main( String[] args ) {
		ComponentActive com = new Decoder( 2 );
		
		generateLookupTable( com );
	}
}




























