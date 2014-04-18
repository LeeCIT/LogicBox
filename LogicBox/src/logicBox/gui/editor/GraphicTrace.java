


package logicBox.gui.editor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.util.ArrayList;
import java.util.List;
import logicBox.gui.Gfx;
import logicBox.gui.VecPath;
import logicBox.util.Bbox2;
import logicBox.util.Geo;
import logicBox.util.Line2;
import logicBox.util.Vec2;



/**
 * Draws trace graphics.
 * @author Lee Coakley
 */
public class GraphicTrace extends Graphic implements GraphicIntersector
{
	private static final long serialVersionUID = 1L;
	
	private List<Vec2>  points;
	private List<Line2> lines;
	private VecPath     polyLine;
	private boolean     isPowered;
	private boolean 	connectedSource;
	private boolean 	connectedDest;
	
	
	
	public GraphicTrace( List<Vec2> points, GraphicPinMapping gpmSrc, GraphicPinMapping gpmDest ) {
		super();
		setFromPoints( points );
		setColors( false );
	}
	
	
	
	public void setFromPoints( List<Vec2> points ) {
		this.points   = new ArrayList<>( points );
		this.lines    = Line2.toLines( points );
		this.polyLine = new VecPath( points, false );
	}
	
	
	
	public void setPowered( boolean powered ) {
		isPowered = powered;
		setColors( isPowered );
	}
	
	
	
	public void setConnectedSource( boolean b ) {
		connectedSource = b;
	}
	
	
	
	public void setConnectedDest( boolean b ) {
		connectedDest = b;
	}
	
	
	
	public void draw( Graphics2D g ) {
		drawTrace( g );
		
		if (connectedSource) drawConnection( g, points.get(0)               );
		if (connectedDest)   drawConnection( g, points.get(points.size()-1) );
	}
	
	
	
	public List<Vec2> getPoints() {
		return points;
	}
	
	
	
	public List<Line2> getLines() {
		return lines;
	}
	
	
	
	public boolean contains( Vec2 pos ) {		
		double distComp = Geo.sqr(EditorStyle.traceThickness * 0.5);
		
		for (Line2 line: lines)
			if (line.distanceSqrToPoint(pos) <= distComp)
				return true;
		
		return false;
	}
	
	
	
	public boolean overlaps( Bbox2 bbox ) {
		Vec2 size = bbox.getSize();
		return polyLine.intersects( bbox.tl.x, bbox.tl.y, size.x, size.y );
	}
	
	
	
	public Bbox2 getBbox() {
		return Bbox2.createFromPoints( points );
	}
	
	
	
	private void setColors( boolean powered ) {
		colStrokeNormal = (powered) ? EditorStyle.colTraceOn : EditorStyle.colTraceOff;
		colStroke       = colStrokeNormal;
	}
	
	
	
	public void setInverted( boolean state ) {
		// Do nothing
	}
	
	
	
	private void drawTrace( Graphics2D g ) {
		Gfx.pushColorAndSet ( g, colStroke );
			Gfx.pushStrokeAndSet( g, EditorStyle.strokeTrace );
				g.draw( polyLine );
			Gfx.popStroke( g );
		Gfx.popColor( g );
	}
	
	
	
	private void drawOverlappedTrace( Graphics2D g, Vec2 a, Vec2 intersect, Vec2 b ) {
		double radius = EditorStyle.compThickness * 2;
		double angleB = Geo.angleBetween( a, b );
		double angleA = angleB + 180;
		Vec2   a2i    = Geo.lenDir(radius,angleA).add( intersect );
		Vec2   b2i    = Geo.lenDir(radius,angleB).add( intersect );
		
		Color   shade      = Geo.lerp( g.getColor(), new Color(0,255,0), 0.5 );
		float[] fracs      = { 0.0f, 0.5f, 1.0f };
		Color[] cols       = { EditorStyle.colTraceOff, shade, EditorStyle.colTraceOff };
		Paint   shadePaint = new LinearGradientPaint( a2i, b2i, fracs, cols, CycleMethod.NO_CYCLE );
		
		VecPath poly = new VecPath();
		poly.moveTo( a   );
		poly.lineTo( a2i );
		poly.moveTo( b2i );
		poly.lineTo( b   );
		
		Gfx.pushColorAndSet ( g, colStroke );
			Gfx.pushStrokeAndSet( g, EditorStyle.strokeTrace );
				g.draw( poly );
				
				Gfx.pushPaintAndSet( g, shadePaint );
					Gfx.drawArc( g, intersect, radius, angleA, angleB );
				Gfx.popPaint( g );
				
				drawConnection( g, a );
				drawConnection( g, b );
			Gfx.popStroke( g );
		Gfx.popColor( g );
	}
	
	
	
	private void drawJunction( Graphics2D g, Vec2 pos ) {
		double radius = 4;
		
		Gfx.pushStrokeAndSet( g, EditorStyle.strokeBubble );
			Gfx.pushAntialiasingStateAndSet( g, false );
				Gfx.drawCircle( g, pos, radius, EditorStyle.colJunctionOff, true );
			Gfx.popAntialiasingState( g );
			
			Gfx.drawCircle( g, pos, radius, EditorStyle.colJunctionOn, false );
		Gfx.popStroke( g );
	}
	
	
	
	private void drawConnection( Graphics2D g, Vec2 pos ) {
		double radius = 3;
		
		Gfx.pushStrokeAndSet( g, EditorStyle.strokeBubble );
			Gfx.pushAntialiasingStateAndSet( g, false );
				Gfx.drawCircle( g, pos, radius, EditorStyle.colBackground, true );
			Gfx.popAntialiasingState( g );
			
			Gfx.drawCircle( g, pos, radius, colStroke, false );
		Gfx.popStroke( g );
	}
}
