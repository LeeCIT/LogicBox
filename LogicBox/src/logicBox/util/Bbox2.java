


package logicBox.util;

import java.util.List;



/**
 * 2D axis-aligned bounding box.
 * @author Lee Coakley
 */
public class Bbox2 extends Region
{
	public Bbox2() {}
	
	
	
	public Bbox2( Vec2 tl, Vec2 br ) {
		super( tl.copy(), br.copy() );
	}
	
	
	
	public Bbox2( double tlx, double tly, double brx, double bry ) {
		this.tl = new Vec2( tlx, tly );
		this.br = new Vec2( brx, bry );
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
}
































