


package logicBox.gui.editor.graphics;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import logicBox.gui.Gfx;
import logicBox.util.Bbox2;
import logicBox.util.Vec2;



/**
 * Graphic for comments (editable text objects placed into the editor)
 * @author Lee Coakley
 */
public class GraphicComment extends Graphic
{
	private static final long serialVersionUID = 1L;
	
	private String text;
	
	
	
	public GraphicComment( String text ) {
		super();
		this.text = text;
	}
	
	
	
	public String getText() {
		return text;
	}
	
	
	
	public void setText( String text ) {
		this.text = text;
		wrapText();
	}
	
	
	
	public void draw( Graphics2D g ) { // TODO
		Font        fontPrev = g.getFont();
		Font        font     = new Font( "Arial", 0, 48 );
		Rectangle2D rect     = g.getFontMetrics(font).getStringBounds( text, g );
		int         width    = (int) rect.getWidth ();
		int         height   = (int) rect.getHeight();
		
		Gfx.pushAntialiasingStateAndSet( g, true );
			g.setFont( font );
			g.drawString( text, 0, 0 );
			g.setFont( fontPrev );
		Gfx.popAntialiasingState( g );
	}
	
	
	
	private void wrapText() {
		// TODO
	}



	public boolean contains( Vec2 pos ) {
		return false;
	}



	public boolean overlaps( Bbox2 bbox ) {
		return false;
	}



	public Bbox2 getBbox() {
		return null;
	}
}
