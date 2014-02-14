


package logicBox.util;



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
}
