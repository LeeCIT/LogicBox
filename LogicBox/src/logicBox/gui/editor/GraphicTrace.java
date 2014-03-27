


package logicBox.gui.editor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.MultipleGradientPaint.CycleMethod;
import logicBox.gui.Gfx;
import logicBox.gui.VecPath;
import logicBox.util.Geo;
import logicBox.util.Vec2;



/**
 * Draws trace graphics.
 * TODO
 * @author Lee Coakley
 */
public class GraphicTrace extends Graphic
{
	public void draw( Graphics2D g ) {
		
	}
	
	
	
	private void drawTrace( Graphics2D g ) {
		Vec2 a = new Vec2( 256+64,256 );
		Vec2 j = a.add( 64 );
		Vec2 c = j.add( new Vec2(64,0) );
		Vec2 d = c.add( new Vec2(0,64) );
		Vec2 e = j.subtract( new Vec2(64,0) );
		
		VecPath poly = new VecPath();
		poly.moveTo( a );
		poly.lineTo( j );
		poly.lineTo( c );
		poly.lineTo( d );
		poly.moveTo( j );
		poly.lineTo( e );
		
		Gfx.pushColorAndSet ( g, EditorStyle.colTraceOff );
			Gfx.pushStrokeAndSet( g, EditorStyle.strokeTrace );
				g.draw( poly );
				drawJunction( g, j );
				drawConnection( g, a );
				drawConnection( g, d );
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
		
		Gfx.pushColorAndSet ( g, EditorStyle.colTraceOff );
			Gfx.pushStrokeAndSet( g, EditorStyle.strokeBody );
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
			
			Gfx.drawCircle( g, pos, radius, EditorStyle.colTraceOff, false );
		Gfx.popStroke( g );
	}
}
