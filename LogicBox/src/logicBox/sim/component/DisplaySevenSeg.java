


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
	private static final long serialVersionUID = 1L;
	
	private boolean[] segmentStates;
	
	
	
	public DisplaySevenSeg() {
		super( 4 );
	}
	
	
	
	public boolean[] getSegmentStates() {
		return segmentStates;
	}
	
	
	
	public int getNumber() {
		return SimUtil.decodePinsToInt( pinInputs );
	}
	
	
	
	public void update() {
		int i = getNumber();
		
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
	
	
	
	public void reset() { // Show zero
		super.reset();
		update();
	}
	
	
	
	public String getName() {
		return "Seven segment display";
	}
	
	
	
	
	
	public static void main( String[] args ) {
		DisplaySevenSeg ssd = new DisplaySevenSeg();
		
		for (int i=0; i<=0xF; i++) {
			SimUtil.encodeIntToPins( i, ssd.getPinInputs() );
			ssd.update();
			printSegs( ssd.getSegmentStates() );
		}
	}
	
	
	
	private static void printSegs( boolean[] b ) {
		for (int i=0; i<b.length; i++)
			System.out.print( b[i] ? i : "-" );
		
		System.out.println();
	}
}



























