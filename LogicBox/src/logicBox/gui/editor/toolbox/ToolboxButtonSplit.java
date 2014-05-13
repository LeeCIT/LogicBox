


package logicBox.gui.editor.toolbox;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JButton;
import org.gpl.JSplitButton.JSplitButton;
import logicBox.gui.Gfx;
import logicBox.gui.editor.graphics.Graphic;
import logicBox.gui.editor.graphics.GraphicComActive;
import logicBox.gui.editor.tools.ToolManager;
import logicBox.util.Bbox2;
import logicBox.util.Evaluator;
import logicBox.util.Geo;
import logicBox.util.Vec2;



/**
 * A button specialised for use in the Toolbox class.
 * @author Lee Coakley
 */
public class ToolboxButtonSplit extends JSplitButton implements ButtTargetable
{
	private Evaluator<ToolManager> targetEvaluator;
	private Graphic graphic;
	private Vec2    scaleAux;
	
	
	
	public ToolboxButtonSplit( GraphicComActive gca, String tooltip ) {
		super();
		
		this.graphic  = ToolboxButtonCom.makeGraphic( gca );
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
		Vec2    sizeComp   = new Bbox2(this).getSize().subtract( getSplitWidth(), 0 );
		double  scaleMul   = Geo.getAspectScaleFactor( sizeBbox, sizeComp, true );
		Vec2    scale      = new Vec2( scaleMul * (1.0 - borderFrac) );
		Vec2    trans      = sizeComp.multiply( 0.5 ).add( armed ? 1 : 0 );
		
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
	
	
	
	public void setToolManagerEvaluator( Evaluator<ToolManager> targetEvaluator ) {
		this.targetEvaluator = targetEvaluator;
	}
	
	
	
	public ToolManager getTargetToolManager() {
		return targetEvaluator.evaluate();
	}
	
	
	
	public JButton getButton() {
		return this;
	}
}























