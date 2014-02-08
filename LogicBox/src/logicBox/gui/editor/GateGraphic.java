


package logicBox.gui.editor;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.List;
import logicBox.gui.Gfx;
import logicBox.gui.VecPath;
import logicBox.util.Vec2;



public class GateGraphic implements Drawable
{
	private VecPath polyBody;
	private VecPath polyPins;
	private boolean hasBubble;
	private Vec2    bubblePos;
	private double  bubbleRadius;
	
	private List<Vec2> pinConnectors;
	
	
	
	public GateGraphic( VecPath polyBody, VecPath polyPins, List<Vec2> pinConnectors ) {
		this.polyBody      = polyBody;
		this.polyPins      = polyPins;
		this.pinConnectors = pinConnectors;
	}
	
	
	
	public void enableBubble( Vec2 pos, double radius ) {
		bubblePos    = pos;
		bubbleRadius = radius;
		hasBubble    = true;
	}
	
	
	
	public void draw( Graphics2D g, Vec2 pos, double angle ) {
		Gfx.pushMatrix( g );
			
			g.transform( AffineTransform.getTranslateInstance( pos.x, pos.y ) );
			g.transform( AffineTransform.getRotateInstance   ( Math.toRadians( angle ) ) );
		
			Gfx.pushColorAndSet( g, EditorStyle.componentStroke );
				Gfx.pushStrokeAndSet( g, EditorStyle.strokePin );
					g.draw( polyPins );
				Gfx.popStroke( g );
					
				Gfx.pushColorAndSet( g, EditorStyle.componentFill );
				Gfx.pushAntialiasingStateAndSet( g, false );
					g.fill( polyBody );
					if (hasBubble) Gfx.drawCircle( g, bubblePos, bubbleRadius, true );
				Gfx.popAntialiasingState( g );
				Gfx.popColor( g );
				
				Gfx.pushStrokeAndSet( g, EditorStyle.strokeBody );
					g.draw( polyBody );
				Gfx.popStroke( g );
				
				if (hasBubble) {
					Gfx.pushStrokeAndSet( g, EditorStyle.strokeBubble );
					Gfx.drawCircle( g, bubblePos, bubbleRadius, false );
					Gfx.popStroke( g );
				}
				
			Gfx.popColor( g );
		
		Gfx.popMatrix( g );
	}
	
	
	
	/**
	 * Get the component's untransformed pin positions relative to [0,0]
	 * These are where traces connect.
	 */
	public List<Vec2> getPinConnectors() {
		return pinConnectors;
	}
}


















