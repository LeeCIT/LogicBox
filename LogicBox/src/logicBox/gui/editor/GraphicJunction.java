


package logicBox.gui.editor;

import java.awt.Color;
import java.awt.Graphics2D;
import logicBox.gui.Gfx;
import logicBox.util.Bbox2;
import logicBox.util.Geo;
import logicBox.util.Vec2;



/**
 * Junction for splitting out traces.
 * @author Lee Coakley
 */
public class GraphicJunction extends Graphic implements GraphicIntersector
{
	private static final long   serialVersionUID = 1L;
	private static final double radius = 6;
	
	private Vec2 pos;
	
	
	
	public GraphicJunction( Vec2 pos ) {
		super();
		this.pos             = pos;
		this.colStrokeNormal = EditorStyle.colJunctionOff;
		this.colFillNormal   = EditorStyle.colJunctionOn;
		updateColours();
	}
	
	
	
	public void setPos( Vec2 pos ) {
		this.pos = pos;
	}
	
	
	
	public Vec2 getPos() {
		return pos;
	}
	
	
	
	public double getRadius() {
		return radius + (0.5 * EditorStyle.bubbleThickness);
	}
	
	
	
	public boolean contains( Vec2 pos ) {
		double dist = Geo.distance(this.pos, pos);
		double ref  = getRadius(); 
		return dist <= ref;
	}
	
	
	
	public boolean overlaps( Bbox2 bbox ) {
		return getBbox().overlaps( bbox );
	}
	
	
	
	public Bbox2 getBbox() {
		return new Bbox2( pos.subtract(radius), pos.add(radius) );
	}
	
	
	
	public void draw( Graphics2D g ) {
		drawJunction( g, pos );
	}
	
	
	
	private void drawJunction( Graphics2D g, Vec2 pos ) {
		Color inside  = (isInverted()) ? colStroke : colFill;
		Color outside = (isInverted()) ? colFill   : colStroke;
		
		Gfx.pushStrokeAndSet( g, EditorStyle.strokeBubble );
			Gfx.pushAntialiasingStateAndSet( g, false );
				Gfx.drawCircle( g, pos, radius, outside, true );
			Gfx.popAntialiasingState( g );
			
			Gfx.drawCircle( g, pos, radius, inside, false );
		Gfx.popStroke( g );
	}
}
















