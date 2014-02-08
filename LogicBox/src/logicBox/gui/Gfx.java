


package logicBox.gui;
import logicBox.util.Geo;
import logicBox.util.Region;
import logicBox.util.Vec2;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Stack;



/**
 * Provides convenient drawing functions.
 * @author Lee Coakley
 */
public class Gfx
{
	private static Stack<Color>           colorStack  = new Stack<Color>();
	private static Stack<Object>          aaStack     = new Stack<Object>();
	private static Stack<AffineTransform> matrixStack = new Stack<AffineTransform>();
	private static Stack<Stroke>          strokeStack = new Stack<Stroke>();
	
	
	
	public static void drawCircle( Graphics2D g, Vec2 pos, double radius, boolean filled ) {		
		int  tl   = (int) -radius;
		int  size = (int) (radius * 2.0);
		
		Gfx.pushMatrix( g );
			Gfx.translate( g, pos );
			
			if (filled)
				 g.fillOval( tl, tl, size, size );
			else g.drawOval( tl, tl, size, size );
			
		Gfx.popMatrix( g );
	}
	
	
	
	public static void drawCircle( Graphics2D g, Vec2 pos, double radius, Color col, boolean filled ) {
		pushColorAndSet( g, col );
		drawCircle( g, pos, radius, filled );
		popColor( g );
	}
	
	
	
	public static void drawArc( Graphics2D g, Vec2 pos, double radius, double angleA, double angleB ) {		
		Vec2 tl        = pos.substract( radius );
		int  tlx       = (int) tl.x;
		int  tly       = (int) tl.y;
		int  size      = (int) (radius * 2.0);
		int  angleDiff = (int) Geo.angleDiff( angleB, angleA );
		
		g.drawArc( tlx, tly, size, size, (int) angleA, angleDiff );
	}
	
	
	
	public static void drawRegion( Graphics2D g, Region r, boolean filled ) {
		drawOrientedRect( g, r.getCentre(), r.getSize(), 0, filled );
	}
	
	
	
	private static void drawThickLineImpl( Graphics2D g, Vec2 a, Vec2 b, double thickness, boolean rounded ) {
		if (thickness < 2.0) {
			g.drawLine( (int) a.x, (int) a.y, (int) b.x, (int) b.y );
			return;
		}
		
		Stroke stroke;
		
		if ( ! rounded)
			 stroke = new BasicStroke( (float) thickness );
		else stroke = new BasicStroke( (float) thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND );
		
		pushStrokeAndSet( g, stroke );
			VecPath poly = new VecPath();
			poly.moveTo( a );
			poly.lineTo( b );
			g.draw( poly );
		popStroke( g );
	}
	
	
	
	public static void drawThickLine( Graphics2D g, Vec2 a, Vec2 b, double thickness ) {
		drawThickLineImpl( g, a, b, thickness, false );
	}
	
	
	
	public static void drawThickRoundedLine( Graphics2D g, Vec2 a, Vec2 b, double thickness ) {
		drawThickLineImpl( g, a, b, thickness, true );
	}
	
	
	
	public static void drawOrientedRect( Graphics2D g, Vec2 centre, Vec2 size, double angle, boolean filled ) {
		VecPath poly = new VecPath();
		Vec2    offH = Geo.lenDir( size.x*0.5, angle    );
		Vec2    offV = Geo.lenDir( size.y*0.5, angle+90 );
		Vec2    a    = centre.add( offH         .add(offV         ) );
		Vec2    b    = centre.add( offH         .add(offV.negate()) );
		Vec2    c    = centre.add( offH.negate().add(offV.negate()) );
		Vec2    d    = centre.add( offH.negate().add(offV         ) );
		
		poly.moveTo( a );
		poly.lineTo( b );
		poly.lineTo( c );
		poly.lineTo( d );
		poly.closePath();
		
		if (filled)
			 g.fill( poly );
		else g.draw( poly );
	}
	
	
	
	/**
	 * Draw a bezier curve with two control points.
	 * TODO update to newer drawing method
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
			drawThickRoundedLine( g, last, cur, thickness );
			
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
		
		Vec2 xt = new Vec2( 0,     top    );
		Vec2 xb = new Vec2( 0,     bottom );
		Vec2 ly = new Vec2( left,  0      );
		Vec2 ry = new Vec2( right, 0      );
		
		for (double x=left; x<right; x+=cellSize.x) {
			xt.x = x;
			xb.x = x;
			Gfx.drawThickLine( g, xt, xb, thickness );
		}
		
		for (double y=top; y<bottom; y+=cellSize.y) {
			ly.y = y;
			ry.y = y;
			Gfx.drawThickLine( g, ly, ry, thickness );
		}
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
	
	
	
	public static void pushMatrix( Graphics2D g ) {
		matrixStack.push( g.getTransform() );
	}
	
	
	
	public static void popMatrix( Graphics2D g ) {
		g.setTransform( matrixStack.pop() );
	}
	
	
	
	public static void translate( Graphics2D g, Vec2 v ) {
		g.transform( AffineTransform.getTranslateInstance(v.x, v.y) );
	}
	
	
	
	public static void rotate( Graphics2D g, float a ) {
		g.transform( AffineTransform.getRotateInstance( Math.toRadians(a) ) );
	}



	public static void scale( Graphics2D g, Vec2 s ) {
		g.transform( AffineTransform.getScaleInstance(s.x, s.y) );
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
	
	
	
	public static void pushStrokeAndSet( Graphics2D g, Stroke s ) {
		strokeStack.push( g.getStroke() );
		g.setStroke( s );
	}
	
	
	
	public static void popStroke( Graphics2D g ) {
		g.setStroke( strokeStack.pop() );
	}
}



































