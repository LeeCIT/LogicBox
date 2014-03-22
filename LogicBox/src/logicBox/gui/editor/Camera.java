


package logicBox.gui.editor;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import logicBox.util.Bbox2;
import logicBox.util.Callback;
import logicBox.util.CallbackRepeater;
import logicBox.util.Geo;
import logicBox.util.Vec2;



/**
 * Editor camera.  Freely pans and zooms using the mouse.
 * @author Lee Coakley
 */
public class Camera
{
	private JComponent component;
	
	private double zoomRate;
	private double zoomRange;
	private double zoomMin;
	private double zoomMax;
	private double zoom;
	
	private final double zoomDirIn  = -1;
	private final double zoomDirOut = +1;
	
	private boolean panningActive;
	private Vec2    panningOrigin;
	private Vec2    pan;
	
	private AffineTransform  matrix;
	private List<Callback>   onTransform;
	private CallbackRepeater mover;
	
	
	
	
	
	public Camera( JComponent attachTo ) {
		component = attachTo;
		
		zoomRate  = 1.0 + (1.0 / 4.0);
		zoomRange = 8.0;
		zoomMin   = 1.0 / zoomRange;
		zoomMax   =       zoomRange;
		zoom      = 1.0;
		
		pan         = new Vec2( 0 );
		matrix      = new AffineTransform();
		onTransform = new ArrayList<>();
		
		setupActions();
	}
	
	
	
	public void applyTransform( Graphics2D g ) {
		AffineTransform matCam = getTransform();
		AffineTransform matRef = g.getTransform();
		AffineTransform mat    = new AffineTransform();
		mat.concatenate( matRef );
		mat.concatenate( matCam );
		
		g.setTransform( mat );
	}
	
	
	
	public void addTransformCallback( Callback cb ) {
		onTransform.add( cb );
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
			AffineTransform inv = matrix.createInverse();
			inv.transform( pos, out );
		}
		catch (NoninvertibleTransformException ex) {
			ex.printStackTrace(); // Can't happen in this trivial zoom/pan situation
		}
		
		return out;
	}
	
	
	
	public Bbox2 getWorldViewArea() {
		Bbox2 b = new Bbox2( component );
		b.tl = mapScreenToWorld( b.tl );
		b.br = mapScreenToWorld( b.br );
		return b;
	}
	
	
	
	public Vec2 getCentre() {
		return getWorldViewArea().getCentre();
	}
	
	
	
	public AffineTransform getTransform() {
		return matrix;
	}
	
	
	
	/**
	 * Move the camera so it is centred on the given position.
	 */
	public void panTo( Vec2 pos ) {
		pan = pos.negate();
		updateTransform();
	}
	
	
	
	public Vec2 getPan() {
		return pan.negate();
	}
	
	
	
	/**
	 * Set the zoom level of the camera.
	 * The level is automatically clamped to min/max.
	 * > 1 magnifies
	 * < 1 minifies
	 */
	public void zoomTo( double zoomLevel ) {
		zoom = Geo.clamp( zoomLevel, zoomMin, zoomMax );
		
		double thresh = 0.10;
		if (zoom < 1  &&  zoom > 1-thresh
		||  zoom > 1  &&  zoom < 1+thresh)
			zoom = 1.0;
		
		updateTransform();
	}
	
	
	
	private void zoomLogarithmic( double wheelInput, boolean isMouseInput ) {
		double  delta = -wheelInput;
		boolean in    = delta > 0.0;
		double  mod   = zoomRate * Math.abs( delta );
		
		if ( ! in)
			 mod = 1.0 / mod;
		
		zoomTo( zoom * mod );
	}
	
	
	
	public void zoomIn() {
		zoomLogarithmic( zoomDirIn, false );
	}
	
	
	
	public void zoomOut() {
		zoomLogarithmic( zoomDirOut, false );
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
	
	
	
	public void interpolateToBbox( Bbox2 bbox, double border, double timeInSeconds ) {
		Vec2   sizeMe  = new Bbox2( component ).getSize();
		Vec2   sizeYou = bbox.expand( border ) .getSize();
		Vec2   pos     = bbox.getCentre();
		double zoom    = Geo.getAspectScaleFactor( sizeYou, sizeMe, true );
		
		interpolateTo( pos, zoom, timeInSeconds );
	}
	
	
	
	public void interpolateTo( final Vec2 pos, final double zoom, final double timeInSeconds ) {
		interpolateStop();
		
		mover = new CallbackRepeater( 1000 / 60,
			new Callback() {
				private Vec2   panStart   = Camera.this.getPan();
				private double zoomStart  = Camera.this.zoom;
				private Vec2   panTarget  = pos;
				private double zoomTarget = zoom;
				private double frac       = 0;
				private double step       = (1.0 / timeInSeconds) / (1000/60.0);
				
				public void execute() {
					Vec2   p = Geo.herp( panStart,  panTarget,  frac );
					double z = Geo.herp( zoomStart, zoomTarget, frac );
					
					if (frac < 1)
						frac += step;
					
					if (frac >= 1) {
						panTo ( panTarget );
						zoomTo( zoomTarget );
						
						mover.softStop();
					} else {
						directZoomAndPan( p, z );
					}
				}
			}
		);
	}
	
	
	
	public void interpolateStop() {
		if (mover != null)
			mover.join();
	}
	
	
	
	private void directZoomAndPan( Vec2 pan, double zoom ) {
		this.zoom = Geo.clamp( zoom, zoomMin, zoomMax );
		this.pan  = pan.negate();
		
		updateTransform();
	}
	
	
	
	private void updateTransform() {
		updateTransform( new Vec2(0.5) );
	}
	
	
	
	private void updateTransform( Vec2 relativeCentre ) {
		Bbox2 region = new Bbox2( component );
		Vec2  centre = region.getSize().multiply( relativeCentre );
		
		matrix = new AffineTransform();
		matrix.translate(  centre.x,  centre.y );
		matrix.scale    (  zoom,      zoom     );
		matrix.translate( -centre.x, -centre.y );
		matrix.translate(  pan.x,     pan.y    );
		matrix.translate(  centre.x,  centre.y );
		matrix.translate(  0.5,       0.5      );
		
		for (Callback cb: onTransform)
			cb.execute();
	}
	
	
	
	private void setupActions() {
		component.addMouseWheelListener( new MouseWheelListener() {
			public void mouseWheelMoved( MouseWheelEvent ev ) {
				interpolateStop(); // Never override user input; always listen
				zoomLogarithmic( ev.getPreciseWheelRotation(), true );
			}
		});
		
		
		component.addMouseListener( new MouseAdapter() {
			public void mousePressed( MouseEvent ev ) {
				if (SwingUtilities.isMiddleMouseButton( ev )) {
					interpolateStop();
					panBegin();
				}
			}
			
			public void mouseReleased( MouseEvent ev ) {
				if (SwingUtilities.isMiddleMouseButton( ev )) {
					interpolateStop();
					panEnd();
				}
			}
		});
		
		
		component.addMouseMotionListener( new MouseMotionAdapter() {
			public void mouseDragged( MouseEvent ev ) {
				if (SwingUtilities.isMiddleMouseButton( ev )) {
					interpolateStop();
					panMove();
				}
			}
		});
		
		
		component.addComponentListener( new ComponentListener() {
			public void componentShown  ( ComponentEvent ev ) { updateTransform(); }
			public void componentResized( ComponentEvent ev ) { updateTransform(); }
			public void componentMoved  ( ComponentEvent ev ) { updateTransform(); }
			public void componentHidden ( ComponentEvent ev ) { updateTransform(); }
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
























