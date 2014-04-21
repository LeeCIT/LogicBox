


package logicBox.util;

import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



/**
 * Represents a 2D line segment.
 * @author Lee Coakley
 */
public class Line2 implements Transformable, Serializable
{
	private static final long serialVersionUID = 1L;
	public Vec2 a,b;
	
	
	
	public Line2( Vec2 a, Vec2 b ) {
		this.a = a.copy();
		this.b = b.copy();
	}
	
	
	
	public Line2( double x1, double y1, double x2, double y2) {
		this.a = new Vec2( x1, y1 );
		this.b = new Vec2( x2, y2 );
	}
	
	
	
	public Line2 translate( Vec2 v ) {
		return new Line2( a.add(v), b.add(v) );
	}
	
	
	
	public Line2 translate( double x, double y ) {
		return new Line2( a.add(x,y), b.add(x,y) );
	}
	
	
	
	public double distanceToPoint( Vec2 point ) {
		return Math.sqrt( distanceSqrToPoint(point) );
	}
	
	
	
	public double distanceSqrToPoint( Vec2 point ) {
		return Geo.distanceSqr( point, closestPoint(point) );
	}
	
	
	
	/**
	 * Find the point on the line which lies closest to the parameter.
	 */
	public Vec2 closestPoint( Vec2 point ) {
	    Vec2   delta  = delta();
	    double lenSqr = Geo.distanceSqr( a, b );

	    if (lenSqr == 0)
	        return a.copy();

	    Vec2   offset = point.subtract( a );
	    double t      = Geo.dot( offset, delta ) / lenSqr;
	    double tClamp = Geo.clamp( t, 0, 1 ); // Clamp to line segment

	    return Geo.lerp( a, b, tClamp );
	}
	
	
	
	public class IntersectResult {
		public boolean intersects;
		public Vec2    pos;
	}
	
	
	
	/**
	 * Find the intersection point on another line, if any.
	 */
	public IntersectResult intersect( Line2 other ) {
		IntersectResult result = new IntersectResult();
		
	    Vec2   aDelta = this .delta();
	    Vec2   bDelta = other.delta();
	    double denom  = Geo.cross( aDelta, bDelta );
	    if (denom == 0) // Parallel
	        return result;
	    
	    Vec2   abDelta  = this.a.subtract( other.a );
	    double crossDiv = Geo.cross(aDelta, abDelta) / denom;
	    if (crossDiv < 0 || crossDiv > 1)  // Would have to be infinite to intersect
	        return result;
	    
	    // Now the other way
	    denom    = -denom;
	    abDelta  = abDelta.negate();
	    crossDiv = Geo.cross(bDelta, abDelta) / denom;
	    if (crossDiv < 0 || crossDiv > 1) 
	        return result;
	    
	    result.intersects = true;
	    result.pos        = Geo.lerp( a, b, crossDiv );
	    return result;
	}
	
	
	
	public boolean overlaps( Bbox2 bbox ) {
		if (bbox.contains( a )
		||  bbox.contains( b ))
			return true;
		
		for (Line2 line: bbox.getLines())
			if (line.intersect( this ).intersects)
				return true;
		
		return false;
	}
	
	
	
	public Bbox2 getBbox() {
		List<Vec2> points = new ArrayList<>();
		points.add( a  );
		points.add( b  );
		return Bbox2.createFromPoints( points );
	}
	
	
	
	public Line2 swapEndpoints() {
		return new Line2( b, a );
	}
	
	
	
	public void transform( AffineTransform matrix ) {
		matrix.transform( a, a );
		matrix.transform( b, b );
	}
	
	
	
	private Vec2 delta() {
		return b.subtract( a );
	}
	
	
	
	public String toString() {
		return "" + a + " -> " + b;
	}
	
	
	
	public static List<Line2> toLines( List<Vec2> points ) {
		List<Line2> lines = new ArrayList<>();
		
		for (int i=0; i<points.size()-1; i++)
			lines.add( new Line2( points.get(i), points.get(i+1) ) );
		
		return lines;
	}
}























