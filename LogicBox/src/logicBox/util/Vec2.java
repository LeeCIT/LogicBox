


package logicBox.util;
import java.awt.geom.Point2D;
import java.io.Serializable;



/**
 * 2D vector.
 * @author Lee Coakley
 */
public class Vec2 extends Point2D implements Serializable
{
	private static final long serialVersionUID = 1L;
	public double x,y;
	
	
	
	public Vec2() {
		this( 0.0 );
	}
	
	
	
	public Vec2( double d ) {
		this.x = d;
		this.y = d;
	}
	
	
	
	public Vec2( double x, double y ) {
		this.x = x;
		this.y = y;
	}
	
	
	
	public Vec2( Vec2 v ) {
		this.x = v.x;
		this.y = v.y;
	}
	
	
	
	public Vec2( Point2D p ) {
		this.x = p.getX();
		this.y = p.getY();
	}
	
	
	
	public Vec2 add( Vec2 v ) {
		return new Vec2( x + v.x,
						 y + v.y );
	}
	
	
	
	public Vec2 subtract( Vec2 v ) {
		return new Vec2( x - v.x,
						 y - v.y );
	}
	
	
	
	public Vec2 multiply( Vec2 v ) {
		return new Vec2( x * v.x,
						 y * v.y );
	}
	
	
	
	public Vec2 modulo( Vec2 v ) {
		return new Vec2( x % v.x,
						 y % v.y );
	}
	
	
	
	public Vec2 add( double v ) {
		return new Vec2( x + v,
						 y + v );
	}
	
	
	
	public Vec2 subtract( double v ) {
		return new Vec2( x - v,
						 y - v );
	}
	
	
	
	public Vec2 multiply( double v ) {
		return new Vec2( x * v,
						 y * v );
	}
	
	
	
	public Vec2 modulo( double v ) {
		return new Vec2( x % v,
						 y % v );
	}
	
	
	
	public Vec2 negate() {
		return new Vec2( -x, -y );
	}
	
	
	
	public Vec2 rotate( double angle ) {
	    double r = Math.toRadians( angle );
	    double c = Math.cos( r );
	    double s = Math.sin( r );

	    return new Vec2( x* c  +  y*s,
	                     x*-s  +  y*c );
	}
	
	
	
	public Vec2 rotate( Vec2 uv ) {
	    double c = uv.x;
	    double s = uv.y;

	    return new Vec2( x*c  +  y*-s,
	                     x*s  +  y* c );
	}
	
	
	
	public double getSmallest() {
		return Math.min( x, y );
	}
	
	
	
	public double getBiggest() {
		return Math.max( x, y );
	}
	
	
	
	public Vec2 copy() {
		return new Vec2( x, y );
	}
	
	
	
	public String toString() {
		return "[" + x + ", " + y + "]";
	}
	
	
	
	public double getX() {
		return x;
	}
	
	
	
	public double getY() {
		return y;
	}
	
	
	
	public void setLocation( double x, double y ) {
		this.x = x;
		this.y = y;
	}
}



























































