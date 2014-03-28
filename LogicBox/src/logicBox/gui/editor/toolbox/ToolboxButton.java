


package logicBox.gui.editor.toolbox;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JButton;
import logicBox.gui.Gfx;
import logicBox.gui.contextMenu.ContextMenu;
import logicBox.gui.editor.GraphicComActive;
import logicBox.gui.editor.tools.ToolManager;
import logicBox.util.Bbox2;
import logicBox.util.Evaluator;
import logicBox.util.Geo;
import logicBox.util.Vec2;



/**
 * A button specialised for use in the Toolbox class.
 * @author Lee Coakley
 */
public class ToolboxButton extends JButton
{
	private Evaluator<ToolManager> targetEvaluator;
	private ContextMenu            contextMenu;
	private GraphicComActive       gca;
	
	
	
	/**
	 * Create a toolbox button.
	 * The Graphic is modified by the button.  Make a fresh copy.
	 */
	public ToolboxButton( GraphicComActive gca, String tooltip, ContextMenu contextMenu ) {
		super();
		
		this.gca         = gca;
		this.contextMenu = contextMenu;
		
		setToolTipText( tooltip );
	    
	    setRolloverEnabled( true );
	    gca.setInverted( true );
	    gca.setFillAntialias( true );
	}
	
	
	
	protected void setToolManagerEvaluator( Evaluator<ToolManager> targetEvaluator ) {
		this.targetEvaluator = targetEvaluator;
	}
	
	
	
	protected ToolManager getTargetToolManager() {
		return targetEvaluator.evaluate();
	}
	
	
	
	protected void paintComponent( Graphics gx ) {
		super.paintComponent( gx );
		
		boolean armed      = getModel().isArmed();
		boolean rollover   = getModel().isRollover();
		double  borderFrac = 0.125;
		Bbox2   bbox       = gca.getBbox();
		Vec2    sizeBbox   = bbox.getSize();
		Vec2    sizeComp   = new Bbox2(this).getSize();
		double  scaleMul   = Geo.getAspectScaleFactor( sizeBbox, sizeComp, true );
		Vec2    scale      = new Vec2( scaleMul * (1.0 - borderFrac) );
		Vec2    trans      = sizeComp.multiply( 0.5 ).add( armed ? 1 : 0 );
		
		Graphics2D g = (Graphics2D) gx;
		
		Gfx.pushMatrix( g );
			Gfx.translate( g, trans );
			Gfx.scale    ( g, scale );
			
			Gfx.pushAntialiasingStateAndSet( g, true );
				gca.setHighlighted( rollover );
				gca.draw( g );
			Gfx.popAntialiasingState( g );
		Gfx.popMatrix( g );
	}
}























