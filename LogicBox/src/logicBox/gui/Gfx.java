


package logicBox.gui;
import logicBox.util.Geo;
import logicBox.util.Vec2;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Stack;



/**
 * Provides advanced drawing functions.
 * @author Lee Coakley
 */
public class Gfx
{
	private static Stack<Color> colorStack = new Stack<Color>();
	
	
	
	public static void drawCircle( Graphics g, Vec2 pos, double radius, boolean filled ) {
		Polygon poly     = new Polygon();
		double  vertices = 60.0;
		
		for (double degs=0; degs<360.0; degs+=360.0/vertices) {
			Vec2 offset = Geo.lenDir( radius, degs );
			Vec2 vertex = pos.add( offset );			
			poly.addPoint( (int) vertex.x, (int) vertex.y );
		}
		
		if (filled)
			 g.fillPolygon( poly );
		else g.drawPolygon( poly );
	}
	
	
	
	public static void drawCircle( Graphics g, Vec2 pos, double radius, Color col, boolean filled ) {
		pushColorAndSet( g, col );
		drawCircle( g, pos, radius, filled );
		popColor( g );
	}
	
	
	
	public static void drawThickLine( Graphics g, Vec2 a, Vec2 b, double thickness, boolean filled ) {
		if (thickness < 2.0) {
			g.drawLine( (int) a.x, (int) a.y, (int) b.x, (int) b.y );
			return;
		}
		
		Vec2   centre = Geo.centre      ( a, b );
		double dist   = Geo.distance    ( a, b );
		double angle  = Geo.angleBetween( a, b );
		Vec2   size   = new Vec2( dist, thickness );
		
		drawOrientedRect( g, centre, size, angle, filled );
	}
	
	
	
	public static void drawThickRoundedLine( Graphics g, Vec2 a, Vec2 b, double thickness, boolean filled ) {
		double radius = thickness * 0.5;
		
		drawThickLine( g, a, b, thickness, filled );
		drawCircle( g, a, radius, filled );
		drawCircle( g, b, radius, filled );
	}
	
	
	
	public static void drawOrientedRect( Graphics g, Vec2 centre, Vec2 size, double angle, boolean filled ) {
		Polygon poly = new Polygon();
		Vec2    offH = Geo.lenDir( size.x*0.5, angle    );
		Vec2    offV = Geo.lenDir( size.y*0.5, angle+90 );
		Vec2    a    = centre.add( offH         .add(offV         ) );
		Vec2    b    = centre.add( offH         .add(offV.negate()) );
		Vec2    c    = centre.add( offH.negate().add(offV.negate()) );
		Vec2    d    = centre.add( offH.negate().add(offV         ) );
		
		poly.addPoint( (int) a.x, (int) a.y );
		poly.addPoint( (int) b.x, (int) b.y );
		poly.addPoint( (int) c.x, (int) c.y );
		poly.addPoint( (int) d.x, (int) d.y );
		
		if (filled)
			 g.fillPolygon( poly );
		else g.drawPolygon( poly );
	}
	
	
	
	/**
	 * Draw a bezier curve with two control points.
	 */
	public static void drawBezierCubic( Graphics g, Vec2 a, Vec2 b, Vec2 c1, Vec2 c2, double thickness ) {
		int    precision = 64;
		Vec2   t         = new Vec2( 1.0 / (double)(precision-1) );
		double tAcc      = 0.0;
		
		Vec2 svDelta = t.multiply( c1.subtract(a)  );
		Vec2 cvDelta = t.multiply( c2.subtract(c1) );
		Vec2 evDelta = t.multiply( b .subtract(c2) );
		Vec2 cur     = a  .copy();
		Vec2 last    = cur.copy();
	    
		for (int i=0; i<precision; i++) {
			drawThickRoundedLine( g, last, cur, thickness, true );
			
			tAcc += t.x;
			
			a  = a .add( svDelta );
			c1 = c1.add( cvDelta );
			c2 = c2.add( evDelta );
			
			last = cur.copy();
			
			Vec2 lsc = Geo.lerp( a,   c1,  tAcc );
			Vec2 lce = Geo.lerp( c1,  b,   tAcc );
				 cur = Geo.lerp( lsc, lce, tAcc );
		}
	}
	
	
	
	/**
	 * Set the identity transform and return whatever it was before.
	 */
	public static AffineTransform setIdentity( Graphics g ) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform lastMatrix = g2d.getTransform();
		AffineTransform identity   = new AffineTransform();
		
		g2d.setTransform( identity );
		return lastMatrix;
	}
	
	
	
	/**
	 * Enable/disable sub-pixel precision when rendering.
	 */
	public static void setAntialiasingState( Graphics g, boolean state ) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			state ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF
		);
	}
	
	
	
	public static void pushColorAndSet( Graphics g, Color col ) {
		colorStack.push( g.getColor() );
		g.setColor( col );
	}
	
	
	
	public static void popColor( Graphics g ) {
		g.setColor( colorStack.pop() );
	}
}



































