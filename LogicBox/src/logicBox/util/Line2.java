


package logicBox.util;



/**
 * Represents a 2D line segment.
 * @author Lee Coakley
 */
public class Line2
{
	public Vec2 a,b;
	
	
	
	public Line2( Vec2 a, Vec2 b ) {
		this.a = a;
		this.b = b;
	}
	
	
	
	/**
	 * Find the point on the line which lies closest to the parameter.
	 */
	public Vec2 closestPoint( Vec2 point ) {
	    Vec2   delta  = delta();
	    double lenSqr = Geo.distanceSqr( a, b );

	    if (lenSqr == 0)
	        return a;

	    Vec2   offset = point.subtract( a );
	    double t      = Geo.dot( offset, delta ) / lenSqr;
	    double tClamp = Geo.clamp( t, 0, 1 ); // Clamp to line segment

	    return Geo.lerp( a, b, tClamp );
	}
	
	
	
	class IntersectResult {
		public boolean intersects;
		public Vec2    pos;
	}
	
	
	
	/**
	 * Find the intersection point on another line, if any.
	 */
	IntersectResult intersect( Line2 other ) {
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
	
	
	
	private Vec2 delta() {
		return b.subtract( a );
	}
}























