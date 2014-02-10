


package logicBox.gui.editor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import logicBox.gui.Gfx;
import logicBox.gui.VecPath;
import logicBox.util.Bbox2;
import logicBox.util.Geo;
import logicBox.util.Vec2;



/**
 * Performs component drawing.
 * Essentially it just caches the computations from GraphicsGen.
 * @author Lee Coakley
 */
public class Graphic implements Drawable
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
	
	
	
	public Graphic( VecPath polyBody, VecPath polyPins, List<Vec2> pinConnectors ) {
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
	
	
	
	/**
	 * Test whether pos (in local space) is contained inside the graphic.
	 * For this to make sense you have to transform pos first.
	 */
	public boolean contains( Vec2 pos ) {
		return Geo.distance( bubblePos, pos ) <= bubbleRadius
			|| polyBody.contains( pos )
			|| polyPins.contains( pos );
	}
	
	
	
	/**
	 * Test whether bounding box intersects the graphic.
	 * The bbox must be transformed to the graphic's local space.
	 */
	public boolean overlaps( Bbox2 bbox ) {
		//return polyBody.i
		return false;
	}
}


















