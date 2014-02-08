


package logicBox.gui.editor;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import logicBox.gui.Gfx;
import logicBox.gui.VecPath;
import logicBox.util.Geo;
import logicBox.util.Region;
import logicBox.util.Vec2;



/**
 * Generates graphical representation for components.
 * @author Lee Coakley
 */
public class ComGraphics
{
	private static final double flatFrac   = 0.5;
	private static final double pinLenFrac = 0.5;
	private static final double bubbleFrac = 0.1;
	private static final float  thickness  = 5.0f;
	
	private static final Stroke strokeBody   = new BasicStroke( thickness );
	private static final Stroke strokePin    = new BasicStroke( thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND );
	private static final Stroke strokeBubble = new BasicStroke( thickness * 0.5f );
	
	
	
	
	private static Region getBaseRegion() {
		return new Region( new Vec2(-32), new Vec2(32) );
	}
	
	
	public static Drawable generateNandGate( int pinCount ) {
		final Region r            = getBaseRegion();
		final double pinLength    = r.getSize().x * pinLenFrac;
		final double bubbleRadius = r.getSize().x * bubbleFrac;
		
		if (pinCount > 4)
			r.br.y += r.getSize().y * 0.125 * (pinCount-4);
		
		final Vec2 bezRefTr = r.getTopRight();
		final Vec2 bezRefBr = r.getBottomRight();
		
		final Vec2 pinOutPos = r.getRightMiddle();
		final Vec2 pinOutEnd = new Vec2( pinOutPos.x + pinLength, pinOutPos.y );
		final Vec2 bubblePos = new Vec2( pinOutPos.x + bubbleRadius + thickness*0.55, pinOutPos.y );
		
		final Vec2 topLeft    = r.getTopLeft();
		final Vec2 topFlatEnd = Geo.lerp( topLeft,    bezRefTr,  flatFrac );
		final Vec2 topBezC1   = Geo.lerp( topFlatEnd, bezRefTr,  0.5      );
		final Vec2 topBezC2   = Geo.lerp( bezRefTr,   pinOutPos, 0.5      );
		
		final Vec2 botLeft    = r.getBottomLeft();
		final Vec2 botFlatEnd = Geo.lerp( botLeft,    bezRefBr,  flatFrac );
		final Vec2 botBezC1   = Geo.lerp( botFlatEnd, bezRefBr,  0.5      );
		final Vec2 botBezC2   = Geo.lerp( bezRefBr,   pinOutPos, 0.5      );
		
		List<Vec2> pinPos = new ArrayList<>();
		pinPos.add( pinOutPos );
		pinPos.add( pinOutEnd );
		
		List<Vec2> pinDistrib = distributePins( topLeft, botLeft, pinCount );
		for (Vec2 pos: pinDistrib) {
			pinPos.add( pos                              );
			pinPos.add( new Vec2(pos.x-pinLength, pos.y) );
		}
		
		final VecPath polyBody = new VecPath();
		polyBody.moveTo ( topLeft );
		polyBody.lineTo ( topFlatEnd );
		polyBody.curveTo( topBezC1, topBezC2, pinOutPos );
		polyBody.curveTo( botBezC2, botBezC1, botFlatEnd );
		polyBody.lineTo ( botFlatEnd );
		polyBody.lineTo ( botLeft );
		polyBody.closePath();
		
		final VecPath polyPins = new VecPath();
		for (int i=0; i<pinPos.size(); i+=2) {
			polyPins.moveTo( pinPos.get(i)   );
			polyPins.lineTo( pinPos.get(i+1) );
		}
		
		Drawable drawable = new Drawable() {
			public void draw( Graphics2D g, Vec2 pos, double angle ) {
				Gfx.pushMatrix( g );
				
					g.transform( AffineTransform.getTranslateInstance( pos.x, pos.y ) );
					g.transform( AffineTransform.getRotateInstance   ( Math.toRadians( angle ) ) );
				
					Gfx.pushColorAndSet( g, EditorColours.componentStroke );
						Gfx.pushStrokeAndSet( g, strokePin );
							g.draw( polyPins );
						Gfx.popStroke( g );
							
						Gfx.pushColorAndSet( g, EditorColours.componentFill );
						Gfx.pushAntialiasingStateAndSet( g, false );
							g.fill( polyBody );
							Gfx.drawCircle( g, bubblePos, bubbleRadius, true );
						Gfx.popAntialiasingState( g );
						Gfx.popColor( g );
						
						Gfx.pushStrokeAndSet( g, strokeBody );
							g.draw( polyBody );
						Gfx.popStroke( g );
						
						Gfx.pushStrokeAndSet( g, strokeBubble );
						Gfx.drawCircle( g, bubblePos, bubbleRadius, false );
						Gfx.popStroke( g );
						
					Gfx.popColor( g );
				
				Gfx.popMatrix( g );
			}
		};
		
		return drawable;
	}
	
	
	
	private static List<Vec2> distributePins( Vec2 a, Vec2 b, int count ) {
		List<Vec2> list = new ArrayList<>();
		
		double dist   = Geo.distance( a, b );
		double step   = dist / count;
		double offset = step * 0.5;
		Vec2   cursor = new Vec2( a.x, a.y + offset );
		
		for (int i=0; i<count; i++) {
			list.add( cursor.copy() );
			cursor.y += step;
		}
		
		return list;
	}
}














































