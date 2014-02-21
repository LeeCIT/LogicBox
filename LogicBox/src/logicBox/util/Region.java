


package logicBox.util;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.Serializable;



/**
 * Defines a region.  Used by drawing functions.
 * @author Lee Coakley
 */
public class Region implements Transformable, Serializable
{
	public Vec2 tl; // Top left
	public Vec2 br; // Bottom right
	
	
	
	public Region() {
		// Do nothing
	}
	
	
	
	public Region( Vec2 tl, Vec2 br ) {
		this.tl = tl;
		this.br = br;
	}
	
	
	
	/**
	 * Create a region based on a component.
	 * This is intended for drawing: its coordinates are [0,0] -> [w,h]-[1,1]
	 */
	public Region( Component com ) {
		this.tl = new Vec2( 0, 0 );
		this.br = new Vec2( com.getWidth()  - 1,
						    com.getHeight() - 1 );
	}
	
	
	
	public Region translate( Vec2 offs ) {
		return new Region( tl.add(offs), br.add(offs) );
	}
	
	
	
	public Vec2 getCentre() {
		return Geo.centre( tl, br );
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
	
	
	
	public double getDrawRadius() {
		return getSize().getSmallest() * 0.5 * 0.85;
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
	 *  		A matrix with rotation will produce incorrect results!
	 */
	public void transform( AffineTransform matrix ) {
		matrix.transform( tl, tl );
		matrix.transform( br, br );
	}
}






































