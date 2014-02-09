


package logicBox.gui.editor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.List;
import logicBox.gui.Gfx;
import logicBox.gui.VecPath;
import logicBox.util.Vec2;



/**
 * Performs component drawing for gates.
 * Essentially it just caches the computations from ComGraphics.
 * @author Lee Coakley
 */
public class GateGraphic implements Drawable
{
	private VecPath polyBody;
	private VecPath polyPins;
	private boolean hasBubble;
	private Vec2    bubblePos;
	private double  bubbleRadius;
	
	private boolean isSelected;
	private Color   colStroke;
	private Color   colFill;  
	
	private List<Vec2> pinConnectors;
	
	
	
	public GateGraphic( VecPath polyBody, VecPath polyPins, List<Vec2> pinConnectors ) {
		this.colStroke     = EditorStyle.colComponentStroke;
		this.colFill       = EditorStyle.colComponentFill;
		this.polyBody      = polyBody;
		this.polyPins      = polyPins;
		this.pinConnectors = pinConnectors;
		
		setSelected( false );
	}
	
	
	
	public void enableBubble( Vec2 pos, double radius ) {
		bubblePos    = pos;
		bubbleRadius = radius;
		hasBubble    = true;
	}
	
	
	
	public void setSelected( boolean state ) {
		isSelected = state;
		
		if (isSelected) {
			colStroke = EditorStyle.makeSelect( EditorStyle.colComponentStroke );
			colFill   = EditorStyle.makeSelect( EditorStyle.colComponentFill   );
		} else {
			colStroke = EditorStyle.colComponentStroke;
			colFill   = EditorStyle.colComponentFill;
		}
	}
	
	
	
	public boolean isSelected() {
		return isSelected;
	}
	
	
	
	public void draw( Graphics2D g, Vec2 pos, double angle ) {
		Gfx.pushMatrix( g );
			Gfx.translate( g, pos   );
			Gfx.rotate   ( g, angle );
		
			Gfx.pushColorAndSet( g, colStroke );
				Gfx.pushStrokeAndSet( g, EditorStyle.strokePin );
					g.draw( polyPins );
				Gfx.popStroke( g );
					
				Gfx.pushColorAndSet( g, colFill );
				Gfx.pushAntialiasingStateAndSet( g, false );
					g.fill( polyBody );
					if (hasBubble)
						Gfx.drawCircle( g, bubblePos, bubbleRadius, true );
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
	 * Get the component's untransformed pin endpoints relative to [0,0]
	 * These are where traces connect.
	 */
	public List<Vec2> getPinConnectors() {
		return pinConnectors;
	}
}


















