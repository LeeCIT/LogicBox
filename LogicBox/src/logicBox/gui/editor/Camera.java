


package logicBox.gui.editor;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import javax.swing.SwingUtilities;
import logicBox.util.Bbox2;
import logicBox.util.Callback;
import logicBox.util.Geo;
import logicBox.util.Vec2;



/**
 * Editor camera.  Freely pans and zooms using the mouse.
 * @author Lee Coakley
 */
public class Camera
{
	private Component component;
	
	private double zoomRate;
	private double zoomRange;
	private double zoomMin;
	private double zoomMax;
	private double zoom;
	
	private boolean panningActive;
	private Vec2    panningOrigin;
	private Vec2    pan;
	
	private AffineTransform matrix;
	private Callback        onTransform;
	
	
	
	
	
	public Camera( Component attachTo, Callback onTransformChange ) {
		component = attachTo;
		
		zoomRate  = 1.0 + (1.0 / 3.0);
		zoomRange = 8.0;
		zoomMin   = 1.0 / zoomRange;
		zoomMax   =       zoomRange;
		zoom      = 1.0;
		
		pan       = new Bbox2( attachTo ).getCentre();
		matrix    = new AffineTransform();
		
		onTransform = onTransformChange;
		
		setupActions();
	}
	
	
	
	public Vec2 getMousePosScreen() {
		return new Vec2( MouseInfo.getPointerInfo().getLocation() );
	}
	
	
	
	public Vec2 getMousePosWorld() {
		Vec2 comPos   = new Vec2( component.getLocationOnScreen() );
		Vec2 mousePos = getMousePosScreen();
		Vec2 pos      = mousePos.subtract( comPos );
		return mapScreenToWorld( pos );
	}
	
	
	
	public Vec2 mapScreenToWorld( Vec2 pos ) {
		Vec2 out = new Vec2();
		
		try {
			AffineTransform inv = matrix.createInverse(); // TODO consider caching this
			inv.transform( pos, out );
		}
		catch (NoninvertibleTransformException ex) {
			ex.printStackTrace(); // Can't happen in this trivial zoom/pan situation
		}
		
		return out;
	}
	
	
	
	public Bbox2 getWorldViewableArea() {
		Bbox2 b = new Bbox2( component );
		b.tl = mapScreenToWorld( b.tl );
		b.br = mapScreenToWorld( b.br );
		return b;
	}
	
	
	
	public AffineTransform getTransform() {
		return matrix;
	}
	
	
	
	public void zoomIn() {
		zoomLogarithmic( -1, false );
	}
	
	
	
	public void zoomOut() {
		zoomLogarithmic( 1, false );
	}
	
	
	
	public double getZoom() {
		return zoom;
	}
	
	
	
	public double getZoomMin() {
		return zoomMin;
	}
	
	
	
	public double getZoomMax() {
		return zoomMax;
	}
	
	
	
	public Vec2 getCentre() {
		return getWorldViewableArea().getCentre();
	}
	
	
	
	private void zoomLogarithmic( double wheelInput, boolean isMouseInput ) {
		double  delta = -wheelInput;
		boolean in    = delta > 0.0;
		double  mod   = zoomRate * Math.abs( delta );
		
		if ( ! in)
			 mod = 1.0 / mod;
		
		zoom = Geo.clamp( zoom * mod, zoomMin, zoomMax );
		
		double roundingSnapThresh = 1.0 / 32.0;
		if (Geo.absDiff( zoom, 1.0 ) < roundingSnapThresh)
			zoom = 1.0;
		
		// TODO account for mouse position
		updateTransform();
	}
	
	
	
	private void updateTransform() {
		updateTransform( new Vec2(0.5) );
	}
	
	
	
	private void updateTransform( Vec2 normalisedRelativePointer ) {
		Bbox2 region = new Bbox2( component );
		Vec2  half   = region.getSize().multiply( 0.5 );
		
		matrix = new AffineTransform();
		matrix.translate(  half.x,  half.y );
		matrix.scale    (  zoom,    zoom   );
		matrix.translate( -half.x, -half.y );
		matrix.translate(  pan.x,   pan.y  );
		matrix.translate(  0.5,     0.5    );
		
		onTransform.execute();
	}
	
	
	
	private void setupActions() {
		component.addMouseWheelListener( new MouseWheelListener() {
			public void mouseWheelMoved( MouseWheelEvent ev ) {
				zoomLogarithmic( ev.getPreciseWheelRotation(), true );
			}
		});
		
		
		component.addMouseListener( new MouseAdapter() {
			public void mousePressed( MouseEvent ev ) {
				if (SwingUtilities.isMiddleMouseButton( ev ))
					panBegin();
			}
			
			public void mouseReleased( MouseEvent ev ) {
				if (SwingUtilities.isMiddleMouseButton( ev ))
					panEnd();
			}
		});
		
		
		component.addMouseMotionListener( new MouseMotionAdapter() {
			public void mouseDragged( MouseEvent ev ) {
				if (SwingUtilities.isMiddleMouseButton( ev ))
					panMove();
			}
		});
	}
	
	
	
	private void panBegin() {
		panningActive = true;
		panningOrigin = getMousePosScreen().subtract( pan.multiply( zoom ) );
		component.setCursor( new Cursor(Cursor.HAND_CURSOR) );
		updateTransform();
	}
	
	
	
	private void panEnd() {
		panningActive = false;
		component.setCursor( new Cursor(Cursor.DEFAULT_CURSOR) );
		updateTransform();
	}
	
	
	
	private void panMove() {
		if ( ! panningActive)
			return;
					
		Vec2   pos     = getMousePosScreen();
		Vec2   delta   = panningOrigin.subtract( pos );
		double rcpZoom = 1.0 / zoom;
		
		pan = delta.multiply( rcpZoom ).negate();
		updateTransform();
	}
}
























