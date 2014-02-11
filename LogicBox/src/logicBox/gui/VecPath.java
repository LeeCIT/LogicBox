


package logicBox.gui;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import logicBox.util.Vec2;



/**
 * Path2D which accepts Vec2 parameters.
 * @author Lee Coakley
 */
public class VecPath extends Path2D.Float
{
	public VecPath() {
		super();
	}
	
	
	
	public VecPath( int rule, int initialCapacity ) {
		super( rule, initialCapacity );
	}
	
	
	
	public VecPath( int rule ) {
		super( rule );
	}
	
	
	
	public VecPath( Shape s, AffineTransform at ) {
		super( s, at );
	}
	
	
	
	public VecPath( Shape s ) {
		super( s );
	}		
	
	
	
	public void moveTo( Vec2 v ) {
		super.moveTo( v.x, v.y );
	}
	
	
	
	public void lineTo( Vec2 v ) {
		super.lineTo( v.x, v.y );
	}
	
	
	
	public void curveTo( Vec2 c1, Vec2 c2, Vec2 end ) {
		super.curveTo( c1.x, c1.y, c2.x, c2.y, end.x, end.y );
	}
}





























