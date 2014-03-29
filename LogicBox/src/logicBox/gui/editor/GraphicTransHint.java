


package logicBox.gui.editor;

import java.awt.Color;
import java.awt.Graphics2D;
import logicBox.gui.Gfx;
import logicBox.util.Bbox2;
import logicBox.util.StringUtil;
import logicBox.util.Vec2;



/**
 * Transparent box with text for explaining things in the editor.
 * Operates in screen-space.
 * @author Lee Coakley
 */
public class GraphicTransHint implements Drawable, RepaintListener 
{
	private Vec2     pos;
	private String[] lines;
	private String   longestLine;
	
	
	
	public GraphicTransHint( String text ) {
		this( new Vec2(24), text );
	}
	
	
	
	public GraphicTransHint( Vec2 pos, String text ) {
		this.pos         = pos;
		this.lines       = text.split( "\n" );
		this.longestLine = StringUtil.findLongest( lines );
	}
	
	
	
	public void draw( Graphics2D g ) {
		Bbox2  bbox       = new Bbox2( g.getFontMetrics(g.getFont()).getStringBounds(longestLine, g) );
		double lineHeight = bbox.getSize().y;
		double heightAdd  = lineHeight * (lines.length - 1);
		double expand     = 16;
		double rounding   = 12;
		double opacity    = 0.5;
		
		Vec2 pos = this.pos.add( 0, lineHeight );
		
		bbox = bbox.translate( pos );
		bbox = bbox.expand( expand );
		bbox.br.y += heightAdd;
		
		Gfx.pushCompositeAndSet( g, opacity );
		Gfx.pushColorAndSet( g, Color.black );
			Gfx.drawBboxRounded( g, bbox, rounding, true );
		Gfx.popColor( g );
		Gfx.popComposite( g );
		
		Gfx.pushColorAndSet( g, Color.white );
			for (String line: lines) {
				g.drawString( line, (int) pos.x, (int) pos.y );
				pos.y += lineHeight;
			}
		Gfx.popColor( g );
	}
}
