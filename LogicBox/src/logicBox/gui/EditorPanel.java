


package logicBox.gui;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import logicBox.util.Geo;
import logicBox.util.Region;
import logicBox.util.Vec2;



/**
 * The main simulation editor interface.
 * @author Lee Coakley
 */
public class EditorPanel extends JPanel
{
	private double zoomRate  = 1.3333;
	private double zoomRange = 8.0;
	private double zoomMin   = Math.pow( 1.0/zoomRate, zoomRange );
	private double zoomMax   = Math.pow(     zoomRate, zoomRange );
	private double zoom;
	
	private boolean panningActive;
	private Vec2    panningOrigin;
	private Vec2    pan;
	
	private Vec2 focus; // TODO zoom onto cursor instead of inward
	
	
	
	public EditorPanel() {
		zoom  = 1;
		pan   = new Vec2( 0 );
		focus = new Vec2( 0 );
		
		setupActions();
	}
	
	
	
	private void setupActions() {
		addMouseWheelListener( new MouseWheelListener() {
			public void mouseWheelMoved( MouseWheelEvent ev ) {
				focus = absMousePos();
				doLogarithmicZoom( ev.getPreciseWheelRotation() );
				repaint();
				System.out.println( "Focused on " + focus );
			}
		});
		
		
		addMouseListener( new MouseAdapter() {
			public void mousePressed( MouseEvent ev ) {
				if (SwingUtilities.isMiddleMouseButton( ev )) {
					panningActive = true;
					Vec2 scale = new Vec2( zoom );
					panningOrigin = absMousePos().subtract( pan.multiply( scale ) );
					setCursor( new Cursor(Cursor.HAND_CURSOR) );
					repaint();
					System.out.println( "Panning from " + panningOrigin );
				}
			}
			
			public void mouseReleased( MouseEvent ev ) {
				if (SwingUtilities.isMiddleMouseButton( ev )) {
					panningActive = false;
					setCursor( new Cursor(Cursor.DEFAULT_CURSOR) );
					repaint();
					System.out.println( "Panning stopped at " + absMousePos() );
				}
			}
		});
		
		
		addMouseMotionListener( new MouseMotionAdapter() {
			public void mouseDragged( MouseEvent ev ) {
				if (SwingUtilities.isMiddleMouseButton( ev )) {
					if (panningActive) {
						Vec2 pos   = absMousePos();
						Vec2 delta = panningOrigin.subtract( pos );
						Vec2 scale = new Vec2( 1.0 / zoom );
						pan = delta.multiply( scale ).negate();
						repaint();
						//System.out.println(  );
					}
				}
			}
		});
	}
	
	
	
	private Vec2 absMousePos() {
		Point pos = MouseInfo.getPointerInfo().getLocation();
		return new Vec2( pos.x, pos.y );
	}



	protected void doLogarithmicZoom( double wheelInput ) {
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



	protected void paintComponent( Graphics g ) {
		super.paintComponent( g );
		
		Region region = new Region( this );
		Vec2   half   = region.getSize().multiply( new Vec2(0.5) );
		
		Gfx.setAntialiasingState( g, true );
		
		g.setColor( EditorColours.background );
		g.fillRect( getX(), getY(), getWidth(), getHeight() );
		
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform mat = new AffineTransform();
		mat.translate( half.x, half.y );
		mat.scale( zoom, zoom );
		mat.translate( -half.x, -half.y );
		mat.translate( pan.x, pan.y );	
		g2d.setTransform( mat );
		
		Gfx.pushColorAndSet( g, EditorColours.grid );
		Gfx.drawGrid( g, region, new Vec2(64), 3 );
		Gfx.popColor( g );
		
		Gfx.drawCircle( g2d, new Vec2(0), 16, Color.yellow, false );
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
























