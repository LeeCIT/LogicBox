


package logicBox.util;
import java.awt.*;



/**
 * Defines a region.  Used by drawing functions.
 * @author Lee Coakley
 */
public class Region
{
	public Vec2 tl; // Top left
	public Vec2 br; // Bottom right
	
	
	
	public Region( Vec2 tl, Vec2 br ) {
		this.tl = tl;
		this.br = br;
	}
	
	
	
	public Region( Component com ) {
		Rectangle rect = com.getBounds();
		this.tl = new Vec2( rect.getMinX(), rect.getMinY() );
		this.br = new Vec2( rect.getMaxX(), rect.getMaxY() );
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
	
	
	
	public Vec2 getNormalisedCoord( Vec2 pos ) {
		return new Vec2(
			Geo.unlerp( pos.x, getLeft(), getRight()  ),
			Geo.unlerp( pos.y, getTop(),  getBottom() )
		);
	}
}






































