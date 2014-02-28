


package logicBox.sim.component;

import logicBox.sim.SimUtil;



/**
 * Displays a 4-bit number in hex.
 * Can be placed side-by-side to display bigger numbers.
 * 
 * The mapping for each segment:
 * 0 -> right bottom
 * 1 -> right top
 * 2 -> top
 * 3 -> left top
 * 4 -> centre
 * 5 -> left bottom
 * 6 -> bottom
 * 
 * @author Lee Coakley
 */
public class DisplaySevenSeg extends Display
{
	private boolean[] segmentStates;
	
	
	
	public DisplaySevenSeg() {
		super( 4 );
	}
	
	
	
	public boolean[] getSegmentStates() {
		return segmentStates;
	}
	
	
	
	public void update() {
		int i = SimUtil.decodePinsToInt( pinInputs );
		
		boolean[] states = {
		    ! (i==2 | i==12 | i==14 | i==15                 ),
		    ! (i==5 | i== 6 | i==11 | i==12 | i==14 | i==15 ),
		    ! (i==1 | i== 4 | i==11 | i==13                 ),
		    ! (i==1 | i== 2 | i== 3 | i== 7 | i==13         ),
		    ! (i==0 | i== 1 | i== 7 | i==12                 ),
		    ! (i==1 | i== 3 | i== 4 | i== 5 | i== 7 | i== 9 ),
		    ! (i==1 | i== 4 | i== 7 | i== 9 | i==10 | i==15 )
		};
		
		segmentStates = states;
	}
	
	
	
	public String getName() {
		return "7-segment display";
	}
	
	
	
	
	
	public static void main( String[] args ) {
		DisplaySevenSeg seg = new DisplaySevenSeg();
		
		for (int i=0; i<=0xF; i++) {
			SimUtil.encodeIntToPins( i, seg.getPinInputs() );
			seg.update();
			printSegs( seg.getSegmentStates() );
		}
	}
	
	
	
	private static void printSegs( boolean[] b ) {
		for (int i=0; i<b.length; i++)
			System.out.print( b[i] ? i : "-" );
		
		System.out.println();
	}
}



























