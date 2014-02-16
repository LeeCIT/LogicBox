


package logicBox.gui.editor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JButton;
import javax.swing.JFrame;
import logicBox.gui.Gfx;
import logicBox.gui.contextMenu.ContextMenu;
import logicBox.util.Bbox2;
import logicBox.util.Geo;
import logicBox.util.Region;
import logicBox.util.Vec2;



/**
 * A button specialised for displaying component graphics.
 * For use in the ToolBox class.
 * @author Lee Coakley
 */
public class ButtonGraphicCom extends JButton
{
	private GraphicComActive gca;
	private String           tooltip;
	private ContextMenu      contextMenu;
	
	
	
	public ButtonGraphicCom( GraphicComActive gca, String tooltip, ContextMenu contextMenu ) {
		super();
		
		this.gca         = gca;
		this.tooltip     = tooltip;
		this.contextMenu = contextMenu;
		
		
		this.gca         = GraphicGen.generateGateXnor();
		this.tooltip     = "test";
	    this.contextMenu = null;
	}
	
	
	
	protected void paintComponent( Graphics gx ) {
		super.paintComponent( gx );
		
		Graphics2D g = (Graphics2D) gx;
		
		double borderFrac = 0.1;
		Bbox2  bbox       = gca.computeBbox();
		Vec2   sizeBbox   = bbox.getSize();
		Vec2   sizeComp   = new Region(this).getSize();
		double scaleMul   = Geo.getAspectScaleFactor( sizeBbox, sizeComp, true );
		Vec2   scale      = new Vec2( scaleMul * (1.0 - borderFrac) );
		Vec2   trans      = sizeComp.multiply( 0.5 );
		
		Gfx.pushMatrix( g );	
			Gfx.translate( g, trans );
			Gfx.scale    ( g, scale.multiply( 1.0 - borderFrac ) );
			
			Gfx.pushAntialiasingStateAndSet( g, true );
				gca.draw( g, new Vec2(0), 0 );
			Gfx.popAntialiasingState( g );
			
		Gfx.popMatrix( g );
	}
	
	
	
	
	
	public static void main( String[] args ) {
		JFrame frame = new EditorFrame();
		
		frame.setSize( new Dimension(600,600) );
		frame.add( new ButtonGraphicCom( null, null, null ) );
		frame.setVisible( true );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
}
