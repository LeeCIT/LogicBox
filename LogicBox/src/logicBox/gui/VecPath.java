


package logicBox.gui;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.List;
import logicBox.util.Line2;
import logicBox.util.Transformable;
import logicBox.util.Vec2;



/**
 * Path2D which accepts Vec2 parameters.
 * @author Lee Coakley
 */
public class VecPath implements Serializable, Transformable, Shape
{
	private static final long serialVersionUID = 1L;
	
	private Path2D.Double path;
	
	
	
	public VecPath() {
		path = new Path2D.Double();
	}
	
	
	
	public VecPath( List<Vec2> points, boolean close ) {
		this();
		
		if (points.isEmpty())
			return;
		
		moveTo( points.get(0) );
		
		for (int i=1; i<points.size(); i++)
			lineTo( points.get(i) );
		
		if (close)
			closePath();
	}
	
	
	
	public void moveTo( Vec2 v ) {
		path.moveTo( v.x, v.y );
	}
	
	
	
	public void lineTo( Vec2 v ) {
		path.lineTo( v.x, v.y );
	}
	
	
	
	public void curveTo( Vec2 c1, Vec2 c2, Vec2 end ) {
		path.curveTo( c1.x, c1.y, c2.x, c2.y, end.x, end.y );
	}
	
	
	
	public void closePath() {
		path.closePath();
	}
	
	
	
	public void transform( AffineTransform matrix ) {
		path.transform( matrix );
	}
	
	
	
	public Rectangle getBounds() {
		return path.getBounds();
	}
	
	
	
	public Rectangle2D getBounds2D() {
		return path.getBounds2D();
	}
	
	
	
	public boolean contains( double x, double y ) {
		return path.contains( x, y );
	}
	
	
	
	public boolean contains( Point2D p ) {
		return path.contains( p );
	}
	
	
	
	public boolean intersects( double x, double y, double w, double h ) {
		return path.intersects( x, y, w, h );
	}
	
	
	
	public boolean intersects( Rectangle2D r ) {
		return path.intersects( r );
	}
	
	
	
	public boolean contains( double x, double y, double w, double h ) {
		return path.contains( x, y, w, h );
	}
	
	
	
	public boolean contains( Rectangle2D r ) {
		return path.contains( r );
	}
	
	
	
	public PathIterator getPathIterator( AffineTransform at ) {
		return path.getPathIterator( at );
	}
	
	
	
	public PathIterator getPathIterator( AffineTransform at, double flatness ) {
		return path.getPathIterator( at, flatness );
	}}





























