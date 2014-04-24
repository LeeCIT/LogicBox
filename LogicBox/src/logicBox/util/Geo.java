


package logicBox.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.awt.geom.AffineTransform;
import java.util.List;



/**
 * Provides advanced geometric and mathematical utility functions.
 * Coordinate system used works as follows:
 * Top left at [0,0], increases east and south
 * East is 0 degrees, increases anticlockwise
 * @author Lee Coakley
 */
public abstract class Geo
{
	/**
	 * Get the base-2 logarithm of x.
	 * In other words, get which power of 2 the number is.
	 */
	public static double log2( double x ) {
		return Math.log(x) / Math.log(2);
	}
	
	
	
	/**
	 * Get the integer base-2 logarithm of x.
	 * Rounds up.
	 */
	public static int log2i( double x ) {
		return (int) Math.ceil( log2(x) );
	}
	
	
	
	/**
	 * Linear interpolate from A to B by fraction F.
	 */
	public static double lerp( double a, double b, double f ) {
		return a + (b-a) * f;
	}
	
	
	
	/**
	 * Linear interpolate from A to B by fraction F.
	 */
	public static Vec2 lerp( Vec2 a, Vec2 b, double f ) {
		return lerp( a, b, new Vec2(f) );
	}
	
	
	
	/**
	 * Linear interpolate from A to B by fraction F.
	 */
	public static Vec2 lerp( Vec2 a, Vec2 b, Vec2 f ) {
		return new Vec2( 
			lerp( a.x, b.x, f.x ),
			lerp( a.y, b.y, f.y )
		);
	}
	
	
	
	/**
	 * Linear interpolate colour from A to B by fraction F.
	 */
	public static Color lerp( Color a, Color b, double f ) {
		return new Color(
			(int) lerp( a.getRed(),   b.getRed(),   f ),
			(int) lerp( a.getGreen(), b.getGreen(), f ),
			(int) lerp( a.getBlue(),  b.getBlue(),  f )
		);
	}
	
	
	
	/**
	 * Transform a linear [0:1] interpolant into a hermite curve.
	 */
	public static double hermite( double f ) {
		return f * f * (3.0 - (2.0 * f));
	}
	
	
	
	/**
	 * Hermite interpolate from A to B by fraction F.
	 */
	public static double herp( double a, double b, double f ) {
		return lerp( a, b, hermite(f) );
	}
	
	
	
	public static Vec2 herp( Vec2 a, Vec2 b, double f ) {
		return new Vec2( 
			herp( a.x, b.x, f ),
			herp( a.y, b.y, f )
		);
	}
	
	
	
	/**
	 * Normalise given current value and min/max.
	 */
	public static double unlerp( double v, double minv, double maxv ) {
		double base  = v    - minv;
		double delta = maxv - minv;
		return base / delta;
	}
	
	
	
	/**
	 * Clamp to inclusive range. 
	 */
	public static double clamp( double v, double minv, double maxv  ) {
		return Math.min( Math.max(v,minv), maxv );
	}
	
	
	
	/**
	 * Same as unlerp() but clamps result to [0:1] range.
	 * Linear version of the well-known smoothstep function.
	 */
	public static double boxStep( double v, double minv, double maxv ) {
		double ul = unlerp( v, minv, maxv );
		return clamp( ul, 0, 1 );
	}
	
	
	
	/**
	 * Get relative coordinate offset for given length and direction. 
	 */
	public static Vec2 lenDir( double len, double dir ) {
		double radians = Math.toRadians( dir );
		return new Vec2( Math.cos(radians) * len,
					    -Math.sin(radians) * len );
	}
	
	
	
	/**
	 * Get the average of two points, IE the middle of them.
	 */
	public static Vec2 centre( Vec2 a, Vec2 b ) {
		return lerp( a, b, 0.5 );
	}
	
	
	
	/**
	 * Get the vector needed to go from A to B.
	 */
	public static Vec2 delta( Vec2 a, Vec2 b ) {
		return b.subtract( a );
	}
	
	
	
	/**
	 * Snap to nearest multiple of S.
	 */
	public static Vec2 snapNear( Vec2 v, double s ) {
		return new Vec2( roundToMultiple( v.x, s ),
						 roundToMultiple( v.y, s ) );
	}
	
	
	
	/**
	 * Move a point to the nearest point on the edge of a circle.
	 */
	public static Vec2 moveToCircleEdge( Vec2 centre, double radius, Vec2 pos ) {
		Vec2 norm = normalise( delta(centre, pos) );
		return centre.add( norm.multiply(radius) );
	}
	
	
	
	/**
	 * Get the length of a vector squared.
	 */
	public static double lengthSqr( Vec2 v ) {
		return dot( v, v );
	}
	
	
	
	/**
	 * Get the length of a vector.
	 */
	public static double length( Vec2 v ) {
		return Math.sqrt( lengthSqr(v) );
	}
	
	
	
	/**
	 * Get the euclidean distance squared between two points.
	 */
	public static double distanceSqr( Vec2 a, Vec2 b ) {
		return lengthSqr( delta(a,b) );
	}
	
	
	
	/**
	 * Get the euclidean distance between two points.
	 */
	public static double distance( Vec2 a, Vec2 b ) {
		return length( delta(a,b) );
	}
	
	
	
	/**
	 * Get the square of X.
	 */
	public static double sqr( double x ) {
		return x * x;
	}
	
	
	
	/**
	 * Get the dot product of A and B.
	 */
	public static double dot( Vec2 a, Vec2 b ) {
		return (a.x * b.x) + (a.y * b.y);
	}
	
	
	
	/**
	 * Find the 2D cross product of A,B.
	 */
	public static double cross( Vec2 a, Vec2 b ) {
		return (a.x * b.y) - (a.y * b.x);
	}
	
	
	
	/**
	 * Find the angle from A->B.  0 = East.  Increases anticlockwise. 
	 */
	public static double angleBetween( Vec2 a, Vec2 b ) {
		Vec2   d    = b.subtract( a );
		double rads = Math.atan2( d.y, -d.x );
		double degs = Math.toDegrees( rads );
		return (degs + 180.0) % 360.0;
	}
	
	
	
	/**
	 * Express an angle as a unit vector.
	 */
	public static Vec2 angleToVector( double angle ) {
		double r = Math.toRadians( angle );
	    double c = Math.cos( r );
	    double s = Math.sin( r );
	    return new Vec2( c, -s );
	}
	
	
	
	/**
	 * Normalised angular difference in range (-180,+180).
	 * Result is negative if B is anticlockwise with respect to A.
	 * Order of comparison affects the sign, but the absolute value is the same either way.
	 */
	public static double angleDiff( double a, double b ) {
	    double diff   = a - b;
	    double mod360 = diff % 360;
	    return ((mod360 + 540.0) % 360.0) - 180.0;
	}
	
	
	
	public static Vec2 normalise( Vec2 v ) {
		double rcpSqrt = 1.0 / Math.sqrt( lengthSqr(v) );
		return v.multiply( rcpSqrt );
	}
	
	
	
	public static AffineTransform createTransform( Vec2 trans, double rotate ) {
		return createTransform( trans, new Vec2(1,1), rotate );
	}
	
	
	
	public static AffineTransform createTransform( Vec2 trans, Vec2 scale, double rotate ) {
		AffineTransform t = new AffineTransform();
		t.scale    ( scale.x, scale.y );
		t.translate( trans.x, trans.y );
		t.rotate( Math.toRadians(-rotate) );
		return t;
	}
	
	
	
	/**
	 * Transform a monotonically increasing linear input into a sinewave.
	 * Waveform: One dip and rise per period, zero at edges and centre.
	 * Cycle:    Dip -> zero -> rise -> zero  [v^]
	 * Range:    [-1:+1] inclusive.
	 */
	public static double sineSync( double input, double wavelength ) {
	    double half = wavelength * 0.5;
	    double mod  = (input % wavelength) - half;
	    double pm1  = mod / half;
	    return Math.sin( pm1 * Math.PI );
	}
	
	
	
	/**
	 * Same as sineSync but with user-defined output range.
	 */
	public static double sineSync( double input, double wavelength, double low, double high ) {
	    double sine = sineSync( input, wavelength );
	    double f    = (sine * 0.5) + 0.5;
	    return lerp( low, high, f );
	}
	
	
	
	/**
	 * Transform a monotonically increasing linear input into a triangle wave.
	 * Waveform: One dip and rise per period, zero at edges and centre.
	 * Cycle:    Dip -> zero -> rise -> zero  [v^]
	 * Range:    [-1:+1] inclusive.
	 */
	public static double triSync( double input, double wavelength ) {
		double half = wavelength * 0.50;
	    double off  = wavelength * 0.25;
		double m    = ((input+off) % wavelength) - half;
		double a    = Math.abs( m / half );
	    return (a - 0.5) * 2.0;
	}
	
	
	
	/**
	 * Same as triSync but with user-defined output range.
	 */
	public static double triSync( double input, double wavelength, double low, double high ) {
		double tri       = triSync( input, wavelength );
		double rangeConv = (tri + 1.0) * 0.5;
	    return lerp( low, high, rangeConv );
	}
	
	
	
	/**
	 * Transform a monotonically increasing linear input into a square wave.
	 * Waveform: One dip and rise per period, zero at edges and centre.
	 * Cycle:    Dip -> zero -> rise -> zero  [v^]
	 * Range:    [-1:+1] inclusive.
	 */
	public static double sqrSync( double input, double wavelength ) {
		double tri = triSync( input, wavelength );
		
		if      (tri >= +0.5) return  1.0;
		else if (tri <= -0.5) return -1.0;
		else 				  return  0.0;			
	}
	
	
	
	/**
	 * Same as sqrSync but with user-defined output range.
	 */
	public static double sqrSync( double input, double wavelength, double low, double high ) {
		double sqr       = sqrSync( input, wavelength );
		double rangeConv = (sqr + 1.0) * 0.5;
	    return lerp( low, high, rangeConv );
	}
	
	
	
	/**
	 * Convert hertz to milliseconds.
	 */
	public static double hertzToMillisecs( double hz ) {		
		return (1.0 / hz) * 1000;
	}
	
	
	
	/**
	 * Compute the scaling factor needed to fit a given rectangle into another while preserving aspect.
	 */
	public static double getAspectScaleFactor( Vec2 size, Vec2 fitIn, boolean inside ) {
		Vec2 ratios = fitIn.divide( size );
        
        if (inside)
        	 return ratios.getSmallest();
        else return ratios.getBiggest();
	}
	
	
	
	/**
	 * Get the absolute difference between two numbers.
	 */
	public static double absDiff( double x, double y ) {
		return Math.abs( x - y );
	}
	
	
	
	/**
	 * Round to nearest integer using arithmetic rounding.
	 * Math.round() uses banker's rounding which gives undesirable results sometimes (in graphics for example).
	 */
	public static double roundArith( double x ) {
		if (x >= 0.0)
		     return Math.floor(       x + 0.5 );
		else return Math.floor( 1.0 + x - 0.5 );
	}
	
	
	
	/**
	 * Round to nearest integer if the difference it would cause is <= the given threshold.
	 */
	public static double roundOnThresh( double x, double thresh ) {
		double rounded = roundArith( x );
		double diff    = absDiff( x, rounded );
		
		if (diff <= thresh)
			 return rounded;
		else return x;
	}
	
	
	
	/**
	 * Round to nearest multiple.
	 */
	public static double roundToMultiple( double x, double mult ) {
		return roundArith( x / mult ) * mult;
	}
	
	
	
	/**
	 * Round upwards to nearest multiple.
	 */
	public static double ceilToMultiple( double x, double mult ) {
		return Math.ceil( x / mult ) * mult;
	}
	
	
	
	/**
	 * Round to next highest power of two.
	 * Source: http://graphics.stanford.edu/~seander/bithacks.html#RoundUpPowerOf2
	 */
	public static int roundToNextPowerOfTwo( int x ) {
		x--;
	    x |= x >>  1;
	    x |= x >>  2;
	    x |= x >>  4;
	    x |= x >>  8;
	    x |= x >> 16;
	    x++;

	    return x;
	}
	
	
	
	/**
	 * Check if number is a power of two.
	 * Source: http://graphics.stanford.edu/~seander/bithacks.html#DetermineIfPowerOf2
	 */
	public static boolean isPowerOfTwo( int x ) {
		return (x & (x-1)) == 0;
	}



	/**
	 * Get a random integer in the given half-open range.
	 * @param low
	 * @param highex (exclusive)
	 * @return int
	 */
	public static int randomIntRange( int low, int highex ) {
		return low + ((int) Math.floor(Math.random() * (highex-low)));
	}
	
	
	
	/** 
	 * Apply a function to a list, accumulating the result.
	 * Example: reduce() a list of numbers [0,1,2,3] using addition.
	 * 			The result is 6: ((0+1)+2)+3).
	 * @param list
	 * @param functor
	 * @return T, or null if the list is empty.
	 */
	public static <T> T reduce( List<T> list, BinaryFunctor<T> functor ) {
		if (list.isEmpty())
			return null;
		
		T reduced = list.get( 0 );
		
		for (int i=1; i<list.size(); i++)
			reduced = functor.call( reduced, list.get(i) );
		
		return reduced;
	}
	
	
	
	/**
	 * Find the least member of a sequence according to a user-defined comparator.
	 * See main() below for an example usage. 
	 * @param iterable Iterable<T>
	 * @return T, or null if the sequence is empty.
	 */
	public static <T> T findLeast( Iterable<T> iterable, Comparator<T> comp ) {
		Iterator<T> iter = iterable.iterator();
		
		if ( ! iter.hasNext())
			return null;
		
		T best = iter.next();
		
		while (iter.hasNext()) {
			T cur = iter.next();
			
			if (comp.compare(cur,best) < 0)
				best = cur;
		}
		
		return best;
	}
	
	
	
	
	
	public static void main( String[] args ) {
		List<Vec2> otherPoints = new ArrayList<>();
		otherPoints.add( new Vec2(10,0) );
		otherPoints.add( new Vec2(20,0) );
		otherPoints.add( new Vec2(30,0) );
		otherPoints.add( new Vec2(40,0) );
		
		final Vec2 point = new Vec2( 21, 0 );
		
		Vec2 closest = findLeast( otherPoints, new Comparator<Vec2>() {
			public int compare( Vec2 a, Vec2 b ) {
				double aDist = Geo.distanceSqr( a, point );
				double bDist = Geo.distanceSqr( b, point );
				return (aDist<bDist) ? -1 : +1;
			}
		});
		
		System.out.println( "Closest to " + point + " is " + closest );
	}
}


























