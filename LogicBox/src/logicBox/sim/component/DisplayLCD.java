


package logicBox.sim.component;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import logicBox.sim.SimUtil;
import logicBox.util.Geo;



/**
 * A pixel display that allows simple graphics to be displayed.
 * It has X/Y pixel addressing pins, and clear, set and enable pins.
 * @author Lee Coakley
 */
public class DisplayLCD extends Display
{
	private static final long serialVersionUID = 1L;
	
	private int    resX;
	private int    resY;
	private BitSet matrix;
	
	// Refs only
	private List<Pin> pinX;
	private List<Pin> pinY;
	private Pin       pinSet;
	private Pin       pinClear;
	private Pin       pinEnable;
	
	
	
	public DisplayLCD( int resX, int resY ) {
		super();
		
		this.resX   = resX;
		this.resY   = resY;
		this.matrix = new BitSet( resX * resY );
		
		this.pinX      = addPinGroup( Geo.log2i(resX) );
		this.pinY      = addPinGroup( Geo.log2i(resY) );
		this.pinSet    = addPin();
		this.pinClear  = addPin();
		this.pinEnable = addPin();
	}
	
	
	
	private Pin addPin() {
		List<Pin> list = new ArrayList<>();
		SimUtil.addPins( list, this, PinIoMode.input, 1 );
		pinInputs.addAll( list );
		return list.get( 0 );
	}
	
	
	
	private List<Pin> addPinGroup( int count ) {
		List<Pin> list = new ArrayList<>();
		SimUtil.addPins( list, this, PinIoMode.input, count );
		pinInputs.addAll( list );
		return list;
	}
	
	
	
	public int getResX() {
		return resX;
	}
	
	
	
	public int getResY() {
		return resY;
	}
	
	
	
	public Pin getPinSet() {
		return pinSet;
	}
	
	
	
	public Pin getPinClear() {
		return pinClear;
	}
	
	
	
	public Pin getPinEnable() {
		return pinEnable;
	}
	
	
	
	public List<Pin> getPinX() {
		return pinX;
	}
	
	
	
	public List<Pin> getPinY() {
		return pinY;
	}
	
	
	
	public boolean isPixelOn( int x, int y ) {
		return matrix.get( y*resX + x );
	}
	
	
	
	public void update() {
		if ( ! pinEnable.getState())
			return;
		
		if (pinClear.getState()) {
			matrix.clear();
			return;
		}
		
		boolean set   = pinSet.getState();
		int     x     = SimUtil.decodePinsToInt( pinX );
		int     y     = SimUtil.decodePinsToInt( pinY );
		int     index = y*resX + x;
		
		if (x < resX && y < resY)
			matrix.set( index, set );
	}
	
	
	
	public String getName() {
		return "LCD Display [" + resX + "x" + resY + "]";
	}
	
	
	
	public void print() {
		System.out.print( this );
		
		for (int y=0; y<resY; y++) {
			System.out.println();
		
			for (int x=0; x<resX; x++)
				System.out.print( isPixelOn(x,y) ? "#" : "_" );
		}
		
		System.out.print( "\n\n" );
	}
	
	
	
	
	
	public static void main( String[] args ) {
		DisplayLCD lcd = new DisplayLCD( 16, 8 );
		
		lcd.getPinEnable().setState( true );
		
		for (int y=0; y<lcd.getResY(); y++)
		for (int x=0; x<lcd.getResX(); x++) {
			SimUtil.encodeIntToPins( x, lcd.getPinX() );
			SimUtil.encodeIntToPins( y, lcd.getPinY() );
			
			lcd.getPinSet().setState( ((x^y)&1) != 0 );
			lcd.update();
			lcd.print();
		}
	}
}






















