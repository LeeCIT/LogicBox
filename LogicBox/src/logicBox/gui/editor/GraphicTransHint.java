


package logicBox.gui.editor;

import java.awt.Color;
import java.awt.Graphics2D;
import logicBox.gui.Gfx;
import logicBox.util.Bbox2;
import logicBox.util.Vec2;



/**
 * Transparent box with text for explaining things in the editor.
 * Operates in screen-space.
 * @author Lee Coakley
 */
public class GraphicTransHint implements Drawable
{
	private Vec2   pos;
	private String text;
	
	
	
	public GraphicTransHint( String text ) {
		this( new Vec2(24), text );
	}
	
	
	
	public GraphicTransHint( Vec2 pos, String text ) {
		this.pos  = pos;
		this.text = text;
	}
	
	
	
	public void draw( Graphics2D g ) {
		Bbox2  bbox    = new Bbox2( g.getFontMetrics(g.getFont()).getStringBounds(text, g) );
		double yOffset = bbox.getSize().y;
		double expand  = 16;
		
		Vec2 pos = this.pos.add( 0, yOffset );
		
		bbox = bbox.translate( pos );
		bbox = bbox.expand( expand );
		
		Gfx.pushCompositeAndSet( g, 0.5 );
		Gfx.pushColorAndSet( g, Color.black );
			Gfx.drawBboxRounded( g, bbox, 12, true );
		Gfx.popColor( g );
		Gfx.popComposite( g );
		
		Gfx.pushColorAndSet( g, Color.white );
			g.drawString( text, (int) pos.x, (int) pos.y );
		Gfx.popColor( g );
	}
}
