


package logicBox.gui;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.List;
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
		fillBackground( g );
		
		Gfx.setAntialiasingState( g, true );
		
		updateTransform( g );
		drawGrid( g );
		
		Gfx.pushColorAndSet( g, Color.yellow );
		Gfx.drawCircle( g, new Vec2(0),       16, false );
		Gfx.drawCircle( g, getMousePosWorld(), 3, true  );
		Gfx.drawArc( g, new Vec2(0), 12, 45, 180 );
		Gfx.popColor( g );
		
		Region drawIn = new Region( new Vec2(256), new Vec2(320,320) );
		drawAndGate( g, drawIn );
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
		Gfx.pushColorAndSet( g, EditorColours.background );
		g.fillRect( getX(), getY(), getWidth(), getHeight() );
		Gfx.popColor( g );
	}
	
	
	
	private void drawGrid( Graphics2D g ) {
		Region worldRegion = getWorldRegion();
		Vec2   cellSize    = new Vec2( 64 );
		Vec2   offset      = worldRegion.tl.modulo( cellSize ).negate();
		
		worldRegion.tl = worldRegion.tl.subtract( cellSize );
		worldRegion.br = worldRegion.br.add     ( cellSize );
		
		double  thickThresh = 1.0 / 3.0;
		boolean fakeThin    = zoom <= thickThresh;
		boolean disableAA   = fakeThin || (zoom > 2); 
		double  thickness   = fakeThin ? 1.0 : 3.0;
		double  thinness    = 1.0 - Geo.boxStep( zoom, zoomMin, thickThresh );
		double  thinSoften  = 0.8;
		double  colFactor   = thinness * thinSoften;
		
		Color col = Geo.lerp( EditorColours.grid, EditorColours.background, colFactor );
		
		if (disableAA)
			Gfx.pushAntialiasingStateAndSet( g, false );
		
		Gfx.pushColorAndSet( g, col );
		Gfx.drawGrid( g, worldRegion, offset, cellSize, thickness );
		Gfx.popColor( g );
		
		if (disableAA)
			Gfx.popAntialiasingState( g );
	}
	
	
	
	// TODO separate into stored coords offset from [0,0] for easy transforming
	// TODO split into compute-once path storage + draw method
	private void drawAndGate( Graphics2D g, Region r ) {
		double flatFrac   = 0.5;
		double pinLenFrac = 0.25;
		float  thickness  = 5.0f;
		int    pinCount   = 2;
		double pinLength  = r.getSize().x * pinLenFrac;
		
		if (pinCount > 2)
			r.br.y += r.getSize().y * 0.125 * (pinCount-2);
		
		Vec2 bezRefTr = r.getTopRight();
		Vec2 bezRefBr = r.getBottomRight();
		
		Vec2 pinOutPos = r.getRightMiddle();
		Vec2 pinOutEnd = new Vec2( pinOutPos.x + pinLength, pinOutPos.y );
		
		Vec2 topLeft    = r.getTopLeft();
		Vec2 topFlatEnd = Geo.lerp( topLeft,    bezRefTr,  flatFrac );
		Vec2 topBezC1   = Geo.lerp( topFlatEnd, bezRefTr,  0.5      );
		Vec2 topBezC2   = Geo.lerp( bezRefTr,   pinOutPos, 0.5      );
		
		Vec2 botLeft    = r.getBottomLeft();
		Vec2 botFlatEnd = Geo.lerp( botLeft,    bezRefBr,  flatFrac );
		Vec2 botBezC1   = Geo.lerp( botFlatEnd, bezRefBr,  0.5      );
		Vec2 botBezC2   = Geo.lerp( bezRefBr,   pinOutPos, 0.5      );
		
		List<Vec2> pinPos = new ArrayList<>();
		pinPos.add( pinOutPos );
		pinPos.add( pinOutEnd );
		
		double height   = r.getSize().y;
		double pinSpace = height / (1 + pinCount);
		double yPos     = topLeft.y + pinSpace;
		for (int i=0; i<pinCount; i++) {
			pinPos.add( new Vec2( topLeft.x,             yPos ) );
			pinPos.add( new Vec2( topLeft.x - pinLength, yPos ) );
			yPos += pinSpace;
		}
		
		VecPath polyBody = new VecPath();
		polyBody.moveTo( topLeft );
		polyBody.lineTo( topFlatEnd );
		polyBody.curveTo( topBezC1, topBezC2, pinOutPos );
		polyBody.curveTo( botBezC2, botBezC1, botFlatEnd );
		polyBody.lineTo( botFlatEnd );
		polyBody.lineTo( botLeft );
		polyBody.closePath();
		
		VecPath polyPins = new VecPath();
		for (int i=0; i<pinPos.size(); i+=2) {
			polyPins.moveTo( pinPos.get(i)   );
			polyPins.lineTo( pinPos.get(i+1) );
		}
		
		Stroke strokeBody = new BasicStroke( thickness );
		Stroke strokePin  = new BasicStroke( thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND );
		
		Gfx.pushColorAndSet( g, EditorColours.componentStroke );
			Gfx.pushStrokeAndSet( g, strokePin );
				g.draw( polyPins );
			Gfx.popStroke( g );
				
			Gfx.pushColorAndSet( g, EditorColours.componentFill );
			Gfx.pushAntialiasingStateAndSet( g, false );
				g.fill( polyBody );
			Gfx.popAntialiasingState( g );
			Gfx.popColor( g );
			
			Gfx.pushStrokeAndSet( g, strokeBody );
				g.draw( polyBody );
			Gfx.popStroke( g );
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
























