


package logicBox.gui.editor;
import java.awt.*;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import logicBox.gui.Gfx;
import logicBox.gui.VecPath;
import logicBox.sim.GateAnd;
import logicBox.util.Bbox2;
import logicBox.util.Callback;
import logicBox.util.Geo;
import logicBox.util.Region;
import logicBox.util.Vec2;



/**
 * The main simulation editor interface.
 * @author Lee Coakley
 */
public class EditorPanel extends JPanel
{
	private Camera      cam;
	private EditorWorld world;
	
	
	
	public EditorPanel() {
		super( true );
		cam   = new Camera( this, createOnTransformCallback() );
		world = new EditorWorld();
		
		EditorComponent ecom = new EditorComponent( new GateAnd(2), GraphicGen.generateAndGate(2,false), new Vec2(768) );
		
		world.add( ecom );
		
		addMouseOverTest();
		setupActions();
	}
	
	
	
	private void setupActions() {
		addComponentListener( new ComponentAdapter() {
			public void componentResized( ComponentEvent e ) {
				repaint();
			}
		});
	}
	
	
	
	private void addMouseOverTest() {
		addMouseMotionListener( new MouseMotionAdapter() {
			public void mouseMoved( MouseEvent ev ) {
				System.out.println( "Moved" );
				for (EditorComponent ecom: world.find( cam.getMousePosWorld() ))
					System.out.println( ecom );
			}
		});
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
			
			drawTrace( g );
			Vec2 ota   = new Vec2( 448-96, 384-32 );
			Vec2 inter = new Vec2( 448,    384-32 );
			Vec2 otb   = new Vec2( 448+96, 384-32 );
			drawOverlappedTrace( g, ota, inter, otb );
			
			drawSelection( g, new Bbox2(512,512,1024,1024) );
			
			GraphicComActive graphicComActive = GraphicGen.generateAndGate( 2, true );
			graphicComActive.draw( g, new Vec2(256), 270 );
			
			for (EditorComponent ecom: world.getComponents())
				ecom.draw( g );
			
		Gfx.popMatrix( g );
	}
	
	
	
	private void drawSelection( Graphics2D g, Bbox2 bbox ) {
		double zoom      = cam.getZoom();
		float  thickness = (float) (EditorStyle.compThickness / zoom);
		int    arc       = (int)   (16.0 / zoom);
		
		Gfx.pushStrokeAndSet( g, EditorStyle.makeSelectionStroke(thickness) );
			Gfx.pushColorAndSet( g, EditorStyle.colSelectionStroke );
				g.drawRoundRect( (int) bbox.getLeft(), (int) bbox.getTop(), (int) bbox.getSize().x, (int) bbox.getSize().y, arc, arc );
			Gfx.popColor( g );
		Gfx.popStroke( g );
	}



	private void drawTrace( Graphics2D g ) {
		Vec2 a = new Vec2( 256+64,256 );
		Vec2 j = a.add( 64 );
		Vec2 c = j.add( new Vec2(64,0) );
		Vec2 d = c.add( new Vec2(0,64) );
		Vec2 e = j.subtract( new Vec2(64,0) );
		
		VecPath poly = new VecPath();
		poly.moveTo( a );
		poly.lineTo( j );
		poly.lineTo( c );
		poly.lineTo( d );
		poly.moveTo( j );
		poly.lineTo( e );
		
		Gfx.pushColorAndSet ( g, EditorStyle.colTraceOff );
		Gfx.pushStrokeAndSet( g, EditorStyle.strokeTrace );
		
			g.draw( poly );
			drawJunction( g, j );
			drawConnection( g, a );
			drawConnection( g, d );
			
		Gfx.popStroke( g );
		Gfx.popColor( g );
	}
	
	
	
	private void drawOverlappedTrace( Graphics2D g, Vec2 a, Vec2 intersect, Vec2 b ) {
		double radius = EditorStyle.compThickness * 2;
		double angleB = Geo.angleBetween( a, b );
		double angleA = angleB + 180;
		Vec2   a2i    = Geo.lenDir(radius,angleA).add( intersect );
		Vec2   b2i    = Geo.lenDir(radius,angleB).add( intersect );
		
		Paint lastPaint = g.getPaint();
		Color   shade = Geo.lerp( EditorStyle.colTraceOff, new Color(0,255,0), 0.5 );
		float[] fracs = { 0.0f, 0.5f, 1.0f };
		Color[] cols  = { EditorStyle.colTraceOff, shade, EditorStyle.colTraceOff };
		Paint shadePaint = new LinearGradientPaint( a2i, b2i, fracs, cols, CycleMethod.NO_CYCLE );
		
		VecPath poly = new VecPath();
		poly.moveTo( a   );
		poly.lineTo( a2i );
		poly.moveTo( b2i );
		poly.lineTo( b   );
		
		Gfx.pushColorAndSet ( g, EditorStyle.colTraceOff );
		Gfx.pushStrokeAndSet( g, EditorStyle.strokeBody );
			g.draw( poly );
			
			g.setPaint( shadePaint );
			Gfx.drawArc( g, intersect, radius, angleA, angleB );
			g.setPaint( lastPaint );
			
			drawConnection( g, a );
			drawConnection( g, b );
		Gfx.popStroke( g );
		Gfx.popColor( g );
	}
	
	
	
	private void drawJunction( Graphics2D g, Vec2 pos ) {
		double radius = 4;
		
		Gfx.pushStrokeAndSet( g, EditorStyle.strokeBubble );
			Gfx.pushAntialiasingStateAndSet( g, false );
				Gfx.drawCircle( g, pos, radius, EditorStyle.colJunctionOff, true );
			Gfx.popAntialiasingState( g );
			
			Gfx.drawCircle( g, pos, radius, EditorStyle.colJunctionOn, false );
		Gfx.popStroke( g );
	}
	
	
	
	private void drawConnection( Graphics2D g, Vec2 pos ) {
		double radius = 3;
		
		Gfx.pushStrokeAndSet( g, EditorStyle.strokeBubble );
			Gfx.pushAntialiasingStateAndSet( g, false );
				Gfx.drawCircle( g, pos, radius, EditorStyle.colBackground, true );
			Gfx.popAntialiasingState( g );
			
			Gfx.drawCircle( g, pos, radius, EditorStyle.colTraceOff, false );
		Gfx.popStroke( g );
	}
	
	
	
	private void fillBackground( Graphics2D g ) {
		Gfx.pushMatrix( g );
			Gfx.setIdentity( g );
			
			Gfx.pushColorAndSet( g, EditorStyle.colBackground );
				g.fillRect( 0, 0, getWidth(), getHeight() );
			Gfx.popColor( g );
		
		Gfx.popMatrix( g );
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
		
		Color col = Geo.lerp( EditorStyle.colGrid, EditorStyle.colBackground, colFactor );
		
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
		SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				test();
			}
		});
	}
	
	
	
	public static void test() {
		EditorFrame frame = new EditorFrame();
		EditorPanel panel = new EditorPanel();
		
		frame.setSize( new Dimension(600,600) );
		frame.add( new JLabel("What"), "wrap" );
		frame.add( new JLabel("What"), "split 2" );
		frame.add( panel );
		frame.setVisible( true );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
}

























