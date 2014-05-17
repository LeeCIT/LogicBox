


package logicBox.sim;

import java.util.List;
import logicBox.sim.component.ComponentActive;
import logicBox.sim.component.connective.Pin;
import logicBox.util.Util;



/**
 * Generates truth tables for components.
 * @author Lee Coakley
 */
public abstract class TruthTable
{
	/**
	 * Generate a truth table for the given component.
	 * This only works for combinational components.  The result will be wrong otherwise.
	 */
	public static byte[][] generateFor( ComponentActive com ) {
		com = Util.deepCopy( com );
		
		int inCount      = com.getPinInputCount();
		int outCount     = com.getPinOutputCount();
		int combinations = getCombinations( inCount );
		
		byte[][]  table   = new byte[combinations][outCount];
		List<Pin> inputs  = com.getPinInputs();
		List<Pin> outputs = com.getPinOutputs();
		
		for (int i=0; i<combinations; i++) {
			SimUtil.encodeIntToPins( i, inputs );
			com.update();
			int output = SimUtil.decodePinsToInt( outputs );
			
			for (int z=0; z<table[i].length; z++)
				table[i][z] = (byte) ((output >> z) & 1);
		}
		
		return table;
	}
	
	
	
	private static int getCombinations( int inputs ) {
		return (int) Math.pow( 2, inputs );
	}
}
