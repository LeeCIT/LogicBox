


package logicBox.gui.editor;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import logicBox.gui.Gfx;
import logicBox.util.Callback;
import logicBox.util.Geo;
import logicBox.util.Region;
import logicBox.util.Util;
import logicBox.util.Vec2;



/**
 * The main simulation editor interface.
 * @author Lee Coakley
 */
public class EditorPanel extends JPanel
{
	private Camera cam;
	
	
	
	public EditorPanel() {
		cam = new Camera( this, createOnTransformCallback() );
	}
	
	
	
	protected void paintComponent( Graphics gx ) {
		Graphics2D g = (Graphics2D) gx;
		
		Gfx.pushMatrix( g );
			Gfx.setAntialiasingState( g, false );
			fillBackground( g );
			
			g.setTransform( cam.getTransform() );
			
			Gfx.setAntialiasingState( g, true );
			drawGrid( g );
			
			Gfx.pushColorAndSet( g, Color.yellow );
				Gfx.drawCircle( g, new Vec2(0),            16, false );
				Gfx.drawCircle( g, cam.getMousePosWorld(),  3, true  );
				Gfx.drawArc( g, new Vec2(0), 12, 45, 180 );
			Gfx.popColor( g );
			
			GateGraphic graphic = SchematicGenerator.generateAndGate( Util.randomIntRange( 1, 8 ), false );
			graphic.draw( g, new Vec2(256), 0 );
		Gfx.popMatrix( g );
	}
	
	
	
	private void fillBackground( Graphics2D g ) {
		Gfx.pushColorAndSet( g, EditorStyle.background );
		g.fillRect( getX(), getY(), getWidth(), getHeight() );
		Gfx.popColor( g );
	}
	
	
	
	private void drawGrid( Graphics2D g ) {
		Region worldRegion  = cam.getWorldViewableArea();
		Vec2   cellSize     = new Vec2( 64 );
		Vec2   cellSizeHalf = cellSize.multiply( 0.5 );
		Vec2   offset       = worldRegion.tl.modulo( cellSize ).negate().subtract( cellSizeHalf );
		
		worldRegion.tl = worldRegion.tl.subtract( cellSize 				 );
		worldRegion.br = worldRegion.br.add     ( cellSize.multiply( 2 ) );

		double  zoom        = cam.getZoom();
		double  zoomMin     = cam.getZoomMin();
		double  thickThresh = 1.0 / 3.0;
		boolean fakeThin    = zoom <= thickThresh;
		boolean disableAA   = fakeThin || (zoom > 2); 
		double  thickness   = fakeThin ? 1.0 : EditorStyle.gridThickness;
		double  thinness    = 1.0 - Geo.boxStep( zoom, zoomMin, thickThresh );
		double  thinSoften  = 0.8;
		double  colFactor   = thinness * thinSoften;
		
		Color col = Geo.lerp( EditorStyle.grid, EditorStyle.background, colFactor );
		
		if (disableAA)
			Gfx.pushAntialiasingStateAndSet( g, false );
		
		Gfx.pushColorAndSet( g, col );
		Gfx.drawGrid( g, worldRegion, offset, cellSize, thickness );
		Gfx.popColor( g );
		
		if (disableAA)
			Gfx.popAntialiasingState( g );
	}
	
	
	
	private Callback createOnTransformCallback() {
		return new Callback() {
			public void execute() {
				repaint();
			}
		};
	}
	
	
	
	public static void main( String[] args ) {
		EditorFrame frame = new EditorFrame();
		EditorPanel panel = new EditorPanel();
		
		frame.setSize( new Dimension(600,600) );
		frame.setContentPane( panel );
		frame.setVisible( true );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
}
























