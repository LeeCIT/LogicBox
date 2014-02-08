


package logicBox.gui.editor;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import logicBox.gui.Gfx;
import logicBox.gui.VecPath;
import logicBox.util.Geo;
import logicBox.util.Region;
import logicBox.util.Vec2;



/**
 * The main simulation editor interface.
 * @author Lee Coakley
 */
public class EditorPanel extends JPanel
{
	private double zoomRate  = 1.0 + (1.0 / 3.0);
	private double zoomRange = 8.0;
	private double zoomMin   = Math.pow( 1.0/zoomRate, zoomRange );
	private double zoomMax   = Math.pow(     zoomRate, zoomRange );
	private double zoom;
	
	private boolean panningActive;
	private Vec2    panningOrigin;
	private Vec2    pan;
	
	private Vec2 focus; // TODO zoom onto cursor instead of inward
	
	private AffineTransform matrix;
	
	
	
	public EditorPanel() {
		zoom   = 1;
		pan    = new Vec2( 0 );
		focus  = new Vec2( 0 );
		matrix = new AffineTransform();
		
		setupActions();
	}
	
	
	
	private void setupActions() {
		addMouseWheelListener( new MouseWheelListener() {
			public void mouseWheelMoved( MouseWheelEvent ev ) {
				focus = getMousePosScreen();
				doLogarithmicZoom( ev.getPreciseWheelRotation() );
				repaint();
				System.out.println( "Focused on " + focus );
			}
		});
		
		
		addMouseListener( new MouseAdapter() {
			public void mousePressed( MouseEvent ev ) {
				if (SwingUtilities.isMiddleMouseButton( ev )) {
					panningActive = true;
					panningOrigin = getMousePosScreen().subtract( pan.multiply( zoom ) );
					setCursor( new Cursor(Cursor.HAND_CURSOR) );
					repaint();
				}
			}
			
			public void mouseReleased( MouseEvent ev ) {
				if (SwingUtilities.isMiddleMouseButton( ev )) {
					panningActive = false;
					setCursor( new Cursor(Cursor.DEFAULT_CURSOR) );
					repaint();
				}
			}
		});
		
		
		addMouseMotionListener( new MouseMotionAdapter() {
			public void mouseDragged( MouseEvent ev ) {
				if (SwingUtilities.isMiddleMouseButton( ev )) {
					if (panningActive) {
						Vec2   pos     = getMousePosScreen();
						Vec2   delta   = panningOrigin.subtract( pos );
						double rcpZoom = 1.0 / zoom;
						pan = delta.multiply( rcpZoom ).negate();
						repaint();
					}
				}
			}
		});
	}
	
	
	
	private Vec2 getMousePosScreen() {
		return new Vec2( MouseInfo.getPointerInfo().getLocation() );
	}
	
	
	
	private Vec2 getMousePosWorld() {
		Vec2 comPos   = new Vec2( getLocationOnScreen() );
		Vec2 mousePos = getMousePosScreen();
		Vec2 pos      = mousePos.subtract( comPos );
		
		return screenToWorldSpace( pos );
	}
	
	
	
	private Vec2 screenToWorldSpace( Vec2 pos ) {
		Vec2 out = new Vec2();
		
		try {
			AffineTransform inv = matrix.createInverse();
			inv.transform( pos, out );
		}
		catch (NoninvertibleTransformException ex) {
			ex.printStackTrace(); // Can't happen in this trivial zoom/pan situation
		}
		
		return out;
	}
	
	
	
	private void doLogarithmicZoom( double wheelInput ) {
		double  delta = -wheelInput;
		boolean in    = delta > 0.0;
		double  mod   = zoomRate * Math.abs( delta );
		
		if ( ! in)
			 mod = 1.0 / mod;
		
		zoom = Geo.clamp( zoom * mod, zoomMin, zoomMax );
		
		double roundingSnapThresh = 1.0 / 32.0;
		if (Geo.absDiff( zoom, 1.0 ) < roundingSnapThresh)
			zoom = 1.0;
	}
	
	
	
	private Region getWorldRegion() {
		Region r = new Region( this );
		r.tl = screenToWorldSpace( r.tl );
		r.br = screenToWorldSpace( r.br );
		return r;
	}
	
	
	
	protected void paintComponent( Graphics gx ) {	
		Graphics2D g = (Graphics2D) gx;
		
		Gfx.setAntialiasingState( g, false );
		fillBackground( g );
		
		updateTransform( g );
		
		Gfx.setAntialiasingState( g, true );
		drawGrid( g );
		
		Gfx.pushColorAndSet( g, Color.yellow );
			Gfx.drawCircle( g, new Vec2(0),       16, false );
			Gfx.drawCircle( g, getMousePosWorld(), 3, true  );
			Gfx.drawArc( g, new Vec2(0), 12, 45, 180 );
		Gfx.popColor( g );
		
		SchematicGenerator.generateNandGate( 3 ).draw( g, new Vec2(256), Math.random() * 360 );
	}
	
	
	
	private void updateTransform( Graphics2D g ) {
		Region region = new Region( this );
		Vec2   half   = region.getSize().multiply( 0.5 );
		
		matrix = new AffineTransform();
		matrix.translate( half.x, half.y );
		matrix.scale( zoom, zoom );
		matrix.translate( -half.x, -half.y );
		matrix.translate( pan.x, pan.y );
		matrix.translate( 0.5, 0.5 );
		g.setTransform( matrix );
	}
	
	
	
	private void fillBackground( Graphics2D g ) {
		Gfx.pushColorAndSet( g, EditorStyle.background );
		g.fillRect( getX(), getY(), getWidth(), getHeight() );
		Gfx.popColor( g );
	}
	
	
	
	private void drawGrid( Graphics2D g ) {
		Region worldRegion  = getWorldRegion();
		Vec2   cellSize     = new Vec2( 64 );
		Vec2   cellSizeHalf = cellSize.multiply( 0.5 );
		Vec2   offset       = worldRegion.tl.modulo( cellSize ).negate().subtract( cellSizeHalf );
		
		worldRegion.tl = worldRegion.tl.subtract( cellSize );
		worldRegion.br = worldRegion.br.add     ( cellSize );
		
		double  thickThresh = 1.0 / 3.0;
		boolean fakeThin    = zoom <= thickThresh;
		boolean disableAA   = fakeThin || (zoom > 2); 
		double  thickness   = fakeThin ? 1.0 : 3.0;
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
	
	
	
	
	
	public static void main( String[] args ) {
		EditorFrame frame = new EditorFrame();
		EditorPanel panel = new EditorPanel();
		
		frame.setSize( new Dimension(600,600) );
		frame.setContentPane( panel );
		frame.setVisible( true );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
}
























