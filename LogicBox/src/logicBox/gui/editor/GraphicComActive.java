


package logicBox.gui.editor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import logicBox.gui.Gfx;
import logicBox.gui.VecPath;
import logicBox.util.Bbox2;
import logicBox.util.Geo;
import logicBox.util.Transformable;
import logicBox.util.Vec2;



/**
 * Performs drawing for active components.
 * By default the graphic is centred on [0,0] at angle 0.
 * @author Lee Coakley
 */
public class GraphicComActive extends Graphic implements GraphicIntersector
{
	private static final long serialVersionUID = 1L;
	
	private VecPath polyBody;
	private VecPath polyPins;
	private VecPath	polyAux;
	
	private Vec2	pos;
	private double	angle;
	
	private boolean hasBubble;
	private Vec2    bubblePos;
	private double  bubbleRadius;
	
	private boolean isFillAntialised;
	
	private AffineTransform matrix;
	private Bbox2           bbox;
	
	private List<GraphicPinMapping> pinMap;
	private HashMap<GraphicPinMapping,String> pinLabels;
	
	private boolean hasFillOverride;
	private Color   colFillOverride;
	
	
	
	public GraphicComActive( VecPath polyBody, VecPath polyPins, VecPath polyAux, List<GraphicPinMapping> pinMap ) {
		super();
		this.polyBody  = polyBody;
		this.polyPins  = polyPins;
		this.polyAux   = polyAux;
		this.pinMap    = pinMap;
		this.colStroke = EditorStyle.colComponentStroke;
		this.colFill   = EditorStyle.colComponentFill;
		this.bubblePos = new Vec2();
		this.matrix    = new AffineTransform();
		this.bbox      = computeBbox();
		this.pos       = new Vec2( 0, 0 );
		this.angle     = 0;
		this.pinLabels = new HashMap<>();
	}
	
	
	
	public Vec2 getPos() {
		return pos;
	}
	
	
	
	public double getAngle() {
		return angle;
	}
	
	
	
	/**
	 * Get the pin map.
	 * Does not return a copy: changes will affect the graphic.
	 */
	public List<GraphicPinMapping> getGraphicPinMappings() {
		return pinMap;
	}
	
	
	
	/**
	 * Enable the negation bubble for inverter type gates.
	 */
	public void setBubble( boolean state, Vec2 pos, double radius ) {
		hasBubble    = state;
		bubblePos    = pos;
		bubbleRadius = radius;
	}
	
	
	
	/**
	 * Overrides the internal fill colour.  Inversion-aware
	 */
	public void setFillOverride( boolean state, Color col ) {
		hasFillOverride = state;
		colFillOverride = col;
	}
	
	
	
	public void setPinLabels( HashMap<GraphicPinMapping,String> pinLabels ) {
		this.pinLabels = pinLabels;
	}
	
	
	
	public void setFillAntialias( boolean state ) {
		this.isFillAntialised = state;
	}
	
	
	
	public boolean contains( Vec2 pos ) {
		if (hasBubble) {
			double bubbleComp = Geo.sqr( bubbleRadius );
			if (Geo.distanceSqr(bubblePos, pos) <= bubbleComp)
				return true;
		}
		
		double pinDistComp = Geo.sqr(EditorStyle.compThickness * 0.5);
		for (GraphicPinMapping gpm: pinMap)
			if (Geo.distanceSqr( gpm.line.closestPoint(pos), pos) <= pinDistComp)
				return true;
		
		return polyBody.contains( pos );
	}
	
	
	
	public boolean overlaps( Bbox2 bbox ) {
		Vec2 size = bbox.getSize();
		
		return                      polyBody.intersects( bbox.tl.x, bbox.tl.y, size.x, size.y )
		    || (polyAux  != null && polyAux .intersects( bbox.tl.x, bbox.tl.y, size.x, size.y ))
			|| (polyPins != null && polyPins.intersects( bbox.tl.x, bbox.tl.y, size.x, size.y ));
	}
	
	
	
	/**
	 * Find the nearest pin within the given distance threshold.
	 * Returns null if there are no pins which meet the criteria.
	 */
	public GraphicPinMapping findClosestPin( Vec2 pos, double threshold ) {
		threshold *= threshold; // Squared because distance is squared
		
		GraphicPinMapping best     = null;
		double            bestDist = Double.MAX_VALUE;
		
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
	
	
	
	public Bbox2 getBbox() {
		return bbox;
	}
	
	
	
	private Bbox2 computeBbox() {
		List<Vec2>    points = new ArrayList<>();
		List<VecPath> polys  = new ArrayList<>();
		
		polys.add( polyBody );
		
		if (polyPins != null)
			polys.add( polyPins );
		
		if (polyAux != null)
			polys.add( polyAux );
		
		for (VecPath v: polys) {			
			Rectangle2D rect = v.getBounds2D();
			points.add( new Vec2(rect.getMinX(), rect.getMinY()) );
			points.add( new Vec2(rect.getMaxX(), rect.getMaxY()) );
		}
		
		Bbox2 bbox = Bbox2.createFromPoints( points );
		return bbox.expand( new Vec2(EditorStyle.compThickness) );
	}
	
	
	
	/**
	 * Change the transformation to centre on POS, facing ANGLE.
	 * @param pos
	 * @param angle
	 */
	public void transformTo( Vec2 pos, double angle ) {
		this.pos   = pos;
		this.angle = angle;
		unTransform();		
		transform( Geo.createTransform(pos,angle), false );
	}
	
	
	
	private void unTransform() {
		try {
			transform( matrix.createInverse(), false );
		}
		catch (NoninvertibleTransformException ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	protected void transform( AffineTransform mat, boolean permanent ) {
		if (permanent)
			 matrix = new AffineTransform();
		else matrix = mat;
		
		for (Transformable trans: getTransformables())
			trans.transform( mat );
		
		bbox = computeBbox();
	}
	
	
	
	protected List<Transformable> getTransformables() {
		List<Transformable> trans = new ArrayList<>();
		
		trans.add( polyBody );
		
		if (polyPins != null) trans.add( polyPins  );
		if (polyAux  != null) trans.add( polyAux   );
		if (hasBubble)        trans.add( bubblePos );
		
		for (GraphicPinMapping gpm: pinMap)
			trans.add( gpm.line );
		
		return trans;
	}
	
	
	
	public void draw( Graphics2D g ) {
		Color colStroke = (isInverted()) ? this.colFill   : this.colStroke;
		Color colFill   = (isInverted()) ? this.colStroke : this.colFill;
		
		if (hasFillOverride && !isSelected()) 
			colFill = colFillOverride;
			
		Gfx.pushColorAndSet( g, colStroke );
			if (polyPins != null) {
				Gfx.pushStrokeAndSet( g, EditorStyle.strokePin );
					g.draw( polyPins );
				Gfx.popStroke( g );
			}
			
			Gfx.pushColorAndSet( g, colFill );
				Gfx.pushAntialiasingStateAndSet( g, isFillAntialised );
					g.fill( polyBody );
				Gfx.popAntialiasingState( g );
			Gfx.popColor( g );
			
			Gfx.pushStrokeAndSet( g, EditorStyle.strokeBody );
				g.draw( polyBody );
			Gfx.popStroke( g );
			
			if (polyAux != null) {
				Gfx.pushStrokeAndSet( g, EditorStyle.strokePin );
					g.draw( polyAux );
				Gfx.popStroke( g );
			}
			
			if (hasBubble) {
				Gfx.pushColorAndSet( g, colFill );
					Gfx.pushAntialiasingStateAndSet( g, isFillAntialised );
						Gfx.drawCircle( g, bubblePos, bubbleRadius, true );
					Gfx.popAntialiasingState( g );
				Gfx.popColor( g );
				
				Gfx.pushStrokeAndSet( g, EditorStyle.strokeBubble );
					Gfx.drawCircle( g, bubblePos, bubbleRadius, false );
				Gfx.popStroke( g );
			}
			
			if ( ! pinLabels.isEmpty()) {
				Gfx.pushFontAndSet( g, EditorStyle.componentFont );
					for (GraphicPinMapping gpm: pinLabels.keySet())
						drawPinText( g, pinLabels.get(gpm), gpm );
				Gfx.popFont( g );
			}
		Gfx.popColor( g );
	}
	
	
	
	private void drawPinText( Graphics2D g, String str, GraphicPinMapping gpm ) {
		Vec2   delta  = Geo.delta( gpm.getPinPosEnd(), gpm.getPinPosBody() );
		Vec2   pinVec = Geo.normalise( delta );
		Vec2   offset = new Vec2( -6, 6 );
		double len    = 12 + (4 * (str.length()-1));
		Vec2   pos    = gpm.getPinPosBody().add( pinVec.multiply(len) ).add( offset );
		
		g.drawString( str, (int) pos.x, (int) pos.y );
	}
}


















