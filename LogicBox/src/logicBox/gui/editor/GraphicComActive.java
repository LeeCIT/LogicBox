


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
 * Performs drawing for active components.
 * Essentially it just caches the computations from GraphicsGen.
 * @author Lee Coakley
 */
public class GraphicComActive implements Drawable
{
	private Bbox2 bbox;
	
	private VecPath polyBody;
	private VecPath polyPins;
	
	private boolean hasBubble;
	private Vec2    bubblePos;
	private double  bubbleRadius;
	
	private boolean isSelected;
	private Color   colStroke;
	private Color   colFill;  
	
	private List<GraphicPinMapping> pinMap;
	
	
	
	public GraphicComActive( VecPath polyBody, VecPath polyPins, List<GraphicPinMapping> pinMap, Bbox2 bbox ) {
		this.colStroke = EditorStyle.colComponentStroke;
		this.colFill   = EditorStyle.colComponentFill;
		this.bbox      = bbox;
		this.polyBody  = polyBody;
		this.polyPins  = polyPins;
		this.pinMap    = pinMap;
	}
	
	
	
	public Bbox2 getBbox() {
		return bbox;
	}
	
	
	
	public void enableBubble( Vec2 pos, double radius ) {
		bubblePos    = pos;
		bubbleRadius = radius;
		hasBubble    = true;
	}
	
	
	
	public void setSelected( boolean state ) {
		isSelected = state;
		
		if (isSelected) {
			colStroke = EditorStyle.makeSelected( EditorStyle.colComponentStroke );
			colFill   = EditorStyle.makeSelected( EditorStyle.colComponentFill   );
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
	 * Test whether pos (in local space) is contained inside the graphic.
	 * For this to make sense you have to transform pos first.
	 * Intended for mouse-over usage, so it's fairly precise.
	 */
	public boolean contains( Vec2 pos ) {
		if (hasBubble
		&&  Geo.distance(bubblePos, pos) <= bubbleRadius)
			return true;
		
		double pinDistComp = Geo.sqr(EditorStyle.compThickness) * 0.5;
		
		for (GraphicPinMapping gpm: pinMap)
			if (Geo.distanceSqr( gpm.line.closestPoint(pos), pos) <= pinDistComp)
				return true;
		
		return polyBody.contains( pos );
	}
	
	
	
	/**
	 * Test whether the bounding box intersects the graphic.
	 * The bbox must be transformed to the graphic's local space.
	 * Intended for selections.  Not very precise (excludes pins, bubbles etc).
	 */
	public boolean overlaps( Bbox2 bbox ) {
		Vec2 size = bbox.getSize();
		return polyBody.intersects( bbox.tl.x, bbox.tl.y, size.x, size.y );
	}
	
	
	
	/**
	 * Find the nearest pin within the given distance threshold.
	 * Uses local coordinates.
	 * Returns null if there are no pins within the threshold.
	 */
	public GraphicPinMapping findClosestPin( Vec2 pos, double threshold ) {
		GraphicPinMapping best     = null;
		double            bestDist = Double.MAX_VALUE;
		
		threshold *= threshold; // Squared because distance is squared
		
		for (GraphicPinMapping gpm: pinMap) {
			Vec2   closest = gpm.line.closestPoint( pos );
			double dist    = Geo.distanceSqr( closest, pos );
			
			if (dist <= threshold
			&&  dist <= bestDist) {
				bestDist = dist;
				best     = gpm;
			}
		}
		
		return best;
	}
}


















