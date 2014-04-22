


package logicBox.gui.editor.toolbox;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JButton;
import logicBox.gui.Gfx;
import logicBox.gui.contextMenu.ContextMenu;
import logicBox.gui.editor.Graphic;
import logicBox.gui.editor.GraphicComActive;
import logicBox.gui.editor.tools.ToolManager;
import logicBox.util.Bbox2;
import logicBox.util.Evaluator;
import logicBox.util.Geo;
import logicBox.util.Util;
import logicBox.util.Vec2;



/**
 * A button specialised for use in the Toolbox class.
 * @author Lee Coakley
 */
public class ToolboxButtonGraphic extends ToolboxButton
{
	private Graphic graphic;
	private Vec2    scaleAux;
	
	
	
	public ToolboxButtonGraphic( Graphic gca, String tooltip ) {
		super();
		
		this.graphic  = Util.deepCopy( gca );
		this.scaleAux = new Vec2( 1.0 );
		
		setToolTipText( tooltip );
	    setRolloverEnabled( true );
	}
	
	
	
	public void setScaleAux( double scale ) {
		scaleAux = new Vec2( scale );
	}
	
	
	
	protected void paintComponent( Graphics gx ) {
		super.paintComponent( gx );
		
		boolean armed      = getModel().isArmed();
		boolean rollover   = getModel().isRollover();
		double  borderFrac = 0.15;
		Bbox2   bbox       = graphic.getBbox();
		Vec2    sizeBbox   = bbox.getSize();
		Vec2    sizeComp   = new Bbox2(this).getSize();
		double  scaleMul   = Geo.getAspectScaleFactor( sizeBbox, sizeComp, true );
		Vec2    scale      = new Vec2( scaleMul * (1.0 - borderFrac) );
		Vec2    trans      = sizeComp.multiply( 0.5 ).add( armed ? 1 : 0 );
		
		// Centre graphic
		trans = trans.add( bbox.getCentre().multiply(0.5).negate() );
		
		Graphics2D g = (Graphics2D) gx;
		
		Gfx.pushMatrix( g );
			Gfx.translate( g, trans    );
			Gfx.scale    ( g, scale    );
			Gfx.scale    ( g, scaleAux );
			
			Gfx.pushAntialiasingStateAndSet( g, true );
				graphic.setHighlighted( rollover );
				graphic.draw( g );
			Gfx.popAntialiasingState( g );
		Gfx.popMatrix( g );
	}
}























