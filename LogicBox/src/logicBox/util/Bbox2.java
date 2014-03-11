


package logicBox.util;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



/**
 * 2D axis-aligned bounding box.
 * @author Lee Coakley
 */
public class Bbox2 implements Transformable, Serializable
{
	private static final long serialVersionUID = 1L;
	
	public Vec2 tl; // Top left
	public Vec2 br; // Bottom right
	
	
	
	public Bbox2() {}
	
	
	
	public Bbox2( Vec2 tl, Vec2 br ) {
		this.tl = tl.copy();
		this.br = br.copy();
	}
	
	
	
	public Bbox2( double tlx, double tly, double brx, double bry ) {
		this.tl = new Vec2( tlx, tly );
		this.br = new Vec2( brx, bry );
	}
	
	
	
	/**
	 * Create a region based on a component.
	 * This is intended for drawing: its coordinates are [0,0] -> [w,h]-[1,1]
	 */
	public Bbox2( Component com ) {
		this.tl = new Vec2( 0, 0 );
		this.br = new Vec2( com.getWidth()  - 1,
						    com.getHeight() - 1 );
	}
	
	
	
	public Bbox2( Rectangle rect ) {
		this.tl = new Vec2( rect.getMinX(), rect.getMinY() );
		this.br = new Vec2( rect.getMaxX(), rect.getMaxY() );
	}
	
	
	
	public Bbox2 translate( Vec2 offs ) {
		return new Bbox2( tl.add(offs), br.add(offs) );
	}
	
	
	
	public Vec2 getCentre() {
		return Geo.centre( tl, br );
	}
	
	
	
	/**
	 * Access by normalised coordinates (0:1 inclusive on each axis)
	 */
	public Vec2 getNorm( double xn, double yn ) {
		return Geo.lerp( tl, br, new Vec2(xn,yn) );
	}
	
	
	
	public double getLeft() {
		return tl.x;
	}
	
	
	
	public double getRight() {
		return br.x;
	}
	
	
	
	public double getTop() {
		return tl.y;
	}
	
	
	
	public double getBottom() {
		return br.y;
	}
	
	
	
	public Vec2 getSize() {
		return new Vec2(
			br.x - tl.x,
			br.y - tl.y
		);						 
	}



	public double getAspect() {
		return getSize().x / getSize().y;
	}
	
	
	
	public double getSmallest() {
		return getSize().getSmallest();
	}
	
	
	
	public double getBiggest() {
		return getSize().getBiggest();
	}	
	
	
	
	public Vec2 getTopLeft() {
		return tl.copy();
	}
	
	
	
	public Vec2 getTopMiddle() {
		return new Vec2( getCentre().x, tl.y );
	}
	
	
	
	public Vec2 getTopRight() {
		return new Vec2( br.x, tl.y );
	}
	
	
	
	public Vec2 getLeftMiddle() {
		return new Vec2( tl.x, getCentre().y );
	}
	
	
	
	public Vec2 getRightMiddle() {
		return new Vec2( br.x, getCentre().y );
	}
	
	
	
	public Vec2 getBottomLeft() {
		return new Vec2( tl.x, br.y );
	}
	
	
	
	public Vec2 getBottomMiddle() {
		return new Vec2( getCentre().x, br.y );
	}
	
	
	
	public Vec2 getBottomRight() {
		return br.copy();
	}
	
	
	
	public Vec2 getNormalisedCoord( Vec2 pos ) {
		return new Vec2(
			Geo.unlerp( pos.x, getLeft(), getRight()  ),
			Geo.unlerp( pos.y, getTop(),  getBottom() )
		);
	}
	
	
	
	public String toString() {
		return "" + tl + " -> " + br;
	}
	
	
	
	/**
	 * WARNING: This is an axis-aligned region.
	 *  		A matrix with rotations other than multiples of 90 will produce incorrect results!
	 */
	public void transform( AffineTransform matrix ) {
		matrix.transform( tl, tl );
		matrix.transform( br, br );
	}
	
	
	
	public boolean contains( Bbox2 other ) {
		return other.tl.x >= tl.x
			&& other.tl.y >= tl.y
			&& other.br.x <= br.x
			&& other.br.y <= br.y;
	}
	
	
	
	public boolean overlaps( Bbox2 other ) {
		return other.tl.x <= br.x
			&& other.tl.y <= br.y
		    && other.br.x >= tl.x
		    && other.br.y >= tl.y;
	}
	
	
	
	/**
	 * Expand on each axis by this amount in total.
	 * In other words: half this amount gets added to the extents of the box.
	 */
	public Bbox2 expand( Vec2 amount ) {
		Vec2 ex = amount.multiply( 0.5 );
		
		return new Bbox2(
			tl.subtract( ex ),
			br.add     ( ex )
		);
	}
	
	
	
	public Bbox2 expand( double amount ) {
		return expand( new Vec2(amount) );
	}
	
	
	
	/**
	 * Create a bounding box from the extremes of a set of points.
	 * Returns null if the list is empty.
	 */
	public static Bbox2 createFromPoints( List<Vec2> points ) {
		if (points.isEmpty())
			return null;
		
		Vec2 first = points.get( 0 );
		Vec2 min   = first.copy();
		Vec2 max   = first.copy();
		
		for (int i=1; i<points.size(); i++) {
			Vec2 p = points.get( i );
			min.x = Math.min( min.x, p.x );
			min.y = Math.min( min.y, p.y );
			max.x = Math.max( max.x, p.x );
			max.y = Math.max( max.y, p.y );
		}
		
		return new Bbox2( min, max );
	}
	
	
	
	/**
	 * Create a bounding box from the extremes of a set of points.
	 * Returns null if the list is empty.
	 */
	public static Bbox2 createFromPoints( Vec2...points ) {
		List<Vec2> list = new ArrayList<>();
		
		for (Vec2 v: points)
			list.add( v );
		
		return createFromPoints( list );
	}
	
	
	
	/**
	 * Get the union of two bounding boxes. (The smallest that contains both of them)
	 */
	public static Bbox2 union( Bbox2 a, Bbox2 b ) {
		return Bbox2.createFromPoints( a.tl, a.br, b.tl, b.br );
	}
}
































