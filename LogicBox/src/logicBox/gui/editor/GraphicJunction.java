


package logicBox.gui.editor;

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
	
	
	
	public boolean contains( Vec2 pos ) {
		double dist = Geo.distance(this.pos, pos);
		double ref  = radius + (0.5 * EditorStyle.compThickness); 
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
		Gfx.pushStrokeAndSet( g, EditorStyle.strokeBubble );
			Gfx.pushAntialiasingStateAndSet( g, false );
				Gfx.drawCircle( g, pos, radius, colStroke, true );
			Gfx.popAntialiasingState( g );
			
			Gfx.drawCircle( g, pos, radius, colFill, false );
		Gfx.popStroke( g );
	}
}
