


package logicBox.gui;
import logicBox.util.Geo;
import logicBox.util.Region;
import logicBox.util.Vec2;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.Stack;



/**
 * Provides advanced drawing functions.
 * @author Lee Coakley
 */
public class Gfx
{
	private static Stack<Color>           colorStack  = new Stack<Color>();
	private static Stack<Object>          aaStack     = new Stack<Object>();
	private static Stack<AffineTransform> matrixStack = new Stack<AffineTransform>();
	
	
	
	public static void drawCircle( Graphics2D g, Vec2 pos, double radius, boolean filled ) {
		GeneralPath poly     = new GeneralPath();
		double      vertices = 8 + 60 * Geo.boxStep( radius, 0, 64 );
		double      inc      = 360.0 / vertices;
		
		Vec2 start = Geo.lenDir(radius,0).add( pos );
		poly.moveTo( start.x, start.y );
		
		for (double degs=inc; degs<360.0; degs+=inc) {
			Vec2 offset = Geo.lenDir( radius, degs );
			Vec2 vertex = pos.add( offset );
			poly.lineTo( vertex.x, vertex.y );
		}
		
		poly.closePath();
		
		if (filled)
			 g.fill( poly );
		else g.draw( poly );
	}
	
	
	
	public static void drawCircle( Graphics2D g, Vec2 pos, double radius, Color col, boolean filled ) {
		pushColorAndSet( g, col );
		drawCircle( g, pos, radius, filled );
		popColor( g );
	}
	
	
	
	public static void drawThickLine( Graphics2D g, Vec2 a, Vec2 b, double thickness, boolean filled ) {
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
	
	
	
	public static void drawThickRoundedLine( Graphics2D g, Vec2 a, Vec2 b, double thickness, boolean filled ) {
		double radius = thickness * 0.5;
		
		drawThickLine( g, a, b, thickness, filled );
		drawCircle( g, a, radius, filled );
		drawCircle( g, b, radius, filled );
	}
	
	
	
	public static void drawOrientedRect( Graphics2D g, Vec2 centre, Vec2 size, double angle, boolean filled ) {
		GeneralPath poly = new GeneralPath();
		Vec2        offH = Geo.lenDir( size.x*0.5, angle    );
		Vec2        offV = Geo.lenDir( size.y*0.5, angle+90 );
		Vec2        a    = centre.add( offH         .add(offV         ) );
		Vec2        b    = centre.add( offH         .add(offV.negate()) );
		Vec2        c    = centre.add( offH.negate().add(offV.negate()) );
		Vec2        d    = centre.add( offH.negate().add(offV         ) );
		
		poly.moveTo( a.x, a.y );
		poly.lineTo( b.x, b.y );
		poly.lineTo( c.x, c.y );
		poly.lineTo( d.x, d.y );
		poly.closePath();
		
		if (filled)
			 g.fill( poly );
		else g.draw( poly );
	}
	
	
	
	/**
	 * Draw a bezier curve with two control points.
	 */
	public static void drawBezierCubic( Graphics2D g, Vec2 a, Vec2 b, Vec2 c1, Vec2 c2, double thickness ) {
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
	
	
	
	public static void drawGrid( Graphics2D g, Region region, Vec2 offset, Vec2 cellSize, double thickness ) {
		double left   = region.getLeft()   + offset.x;
		double right  = region.getRight()  + offset.x;
		double top    = region.getTop()    + offset.y;
		double bottom = region.getBottom() + offset.y;
		
		for (double x=left; x<right; x+=cellSize.x)
			Gfx.drawThickLine( g, new Vec2(x,top), new Vec2(x,bottom), thickness, true );
		
		for (double y=top; y<bottom; y+=cellSize.y)
			Gfx.drawThickLine( g, new Vec2(left,y), new Vec2(right,y), thickness, true );
	}
	
	
	
	/**
	 * Set the identity transform and return whatever it was before.
	 */
	public static AffineTransform setIdentity( Graphics2D g ) {
		AffineTransform lastMatrix = g.getTransform();
		AffineTransform identity   = new AffineTransform();
		
		g.setTransform( identity );
		return lastMatrix;
	}
	
	
	
	public static void pushMatrix( Graphics2D g, AffineTransform mat ) {
		matrixStack.push( g.getTransform() );
	}
	
	
	
	public static AffineTransform popMatrix( Graphics2D g ) {
		return matrixStack.pop();
	}
	
	
	
	/**
	 * Enable/disable sub-pixel precision when rendering.
	 */
	public static void setAntialiasingState( Graphics2D g, boolean state ) {
		g.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			state ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF
		);
	}
	
	
	
	public static void pushAntialiasingStateAndSet( Graphics2D g, boolean state ) {
		aaStack.push( g.getRenderingHint( RenderingHints.KEY_ANTIALIASING ) );
		setAntialiasingState( g, state );
	}
	
	
	
	public static void popAntialiasingState( Graphics2D g ) {
		g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, aaStack.pop() );
	}
	
	
	
	public static void pushColorAndSet( Graphics2D g, Color col ) {
		colorStack.push( g.getColor() );
		g.setColor( col );
	}
	
	
	
	public static void popColor( Graphics2D g ) {
		g.setColor( colorStack.pop() );
	}
}



































