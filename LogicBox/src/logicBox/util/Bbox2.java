


package logicBox.util;



/**
 * 2D Bounding box.
 * @author Lee Coakley
 */
public class Bbox2 extends Region
{
	public Bbox2( Vec2 tl, Vec2 br ) {
		super( tl, br );
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
