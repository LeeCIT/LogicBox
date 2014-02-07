


package logicBox.gui;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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



public class EditorPanel extends JPanel
{
	private double zoomRate  = 1.25;
	private double zoomRange = 8.0;
	private double zoomMin   = Math.pow( 1.0/zoomRate, zoomRange );
	private double zoomMax   = Math.pow(     zoomRate, zoomRange );
	private double zoom;
	
	
	private boolean panningState;
	private Vec2    panningOrigin;
	private Vec2    pan;
	
	
	
	public EditorPanel() {
		zoom = 1;
		pan  = new Vec2( 0 );
		
		setupActions();
	}
	
	
	
	private void setupActions() {
		addMouseWheelListener( new MouseWheelListener() {
			public void mouseWheelMoved( MouseWheelEvent ev ) {
				doLogarithmicZoom( ev.getPreciseWheelRotation() );				
				repaint();
			}
		});
		
		
		addMouseListener( new MouseAdapter() {
			public void mousePressed( MouseEvent ev ) {
				if (SwingUtilities.isMiddleMouseButton( ev )) {
					panningState  = true;
					panningOrigin = new Vec2( ev.getX(), ev.getY() ).subtract( pan );
					setCursor( new Cursor(Cursor.HAND_CURSOR) );
					repaint();
				}
			}
			
			public void mouseReleased( MouseEvent ev ) {
				if (SwingUtilities.isMiddleMouseButton( ev )) {
					panningState = false;
					setCursor( new Cursor(Cursor.DEFAULT_CURSOR) );
					repaint();
				}
			}
		});
		
		
		addMouseMotionListener( new MouseMotionAdapter() {
			public void mouseDragged( MouseEvent ev ) {
				if (SwingUtilities.isMiddleMouseButton( ev )) {
					if (panningState) {
						Vec2 pos   = new Vec2( ev.getX(), ev.getY() );
						Vec2 delta = panningOrigin.subtract( pos );
						pan = delta.negate();
						repaint();
					}
				}
			}
		});
	}
	
	
	
	protected void doLogarithmicZoom( double wheelInput ) {
		double  delta = -wheelInput;
		boolean in    = delta > 0.0;
		double  rate  = 1.25;
		double  mod   = rate * Math.abs( delta );
		
		if ( ! in)
			 mod = 1.0 / mod;
		
		zoom = Geo.clamp( zoom * mod, zoomMin, zoomMax );
		
		double roundingSnapThresh = 1.0 / 32.0;
		if (Geo.absDiff( zoom, 1.0 ) < roundingSnapThresh)
			zoom = 1.0;
	}



	protected void paintComponent( Graphics g ) {
		super.paintComponent( g );
		
		Gfx.setAntialiasingState( g, true );
		
		g.setColor( EditorColours.background );
		g.fillRect( getX(), getY(), getWidth(), getHeight() );
		
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform mat = new AffineTransform();
		mat.scale( zoom, zoom );
		mat.translate( pan.x, pan.y );
		g2d.setTransform( mat );
		
		Gfx.pushColorAndSet( g, EditorColours.grid );
		Gfx.drawGrid( g, new Region(this), new Vec2(64), 3 );
		Gfx.popColor( g );
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
























