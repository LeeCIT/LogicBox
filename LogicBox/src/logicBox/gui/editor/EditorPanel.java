


package logicBox.gui.editor;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.Paint;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import logicBox.gui.Gfx;
import logicBox.gui.VecPath;
import logicBox.gui.editor.tools.ToolManager;
import logicBox.sim.component.Demux;
import logicBox.sim.component.GateAnd;
import logicBox.sim.component.GateBuffer;
import logicBox.sim.component.GateNand;
import logicBox.sim.component.GateNor;
import logicBox.sim.component.GateNot;
import logicBox.sim.component.GateOr;
import logicBox.sim.component.GateXnor;
import logicBox.sim.component.GateXor;
import logicBox.sim.component.Mux;
import logicBox.util.Bbox2;
import logicBox.util.Callback;
import logicBox.util.Geo;
import logicBox.util.Vec2;



/**
 * The main simulation editor interface.
 * @author Lee Coakley
 */
public class EditorPanel extends JPanel implements HistoryListener<EditorWorld>
{
	private Camera                      cam;
	private EditorWorld                 world;
	private ToolManager                 toolManager;
	private HistoryManager<EditorWorld> historyManager;
	
	private List<RepaintListener> repaintListeners;
	private boolean               enableAntialiasing;
	
	
	
	public EditorPanel() {
		super( true );
		
		enableAntialiasing = true;
		repaintListeners   = new ArrayList<>();
		
		world = new EditorWorld();
		
		cam = new Camera( this );
		cam.addTransformCallback( createOnTransformCallback() );
		
		toolManager = new ToolManager( this );
		
		historyManager = new HistoryManager<>( this );
		historyManager.markChange();
	}
	
	
	
	public void addDebugAndDemoStuff() {
		world.add( new EditorComponent( new GateBuffer(), GraphicGen.generateGateBuffer(), new Vec2(  0, -128) ) );
		world.add( new EditorComponent( new GateNot(),    GraphicGen.generateGateNot(),    new Vec2(  0, -256) ) );
		
		historyManager.markChange();
		
		for (int i=2; i<=4; i++) {
			double xo = 192 * (i-2);
			
			world.add( new EditorComponent( new GateAnd(i),  GraphicGen.generateGateAnd(i),  new Vec2(xo, 0)   ) );
			world.add( new EditorComponent( new GateNand(i), GraphicGen.generateGateNand(i), new Vec2(xo, 128) ) );
			world.add( new EditorComponent( new GateOr(i),   GraphicGen.generateGateOr(i),   new Vec2(xo, 256) ) );
			world.add( new EditorComponent( new GateNor(i),  GraphicGen.generateGateNor(i),  new Vec2(xo, 384) ) );
			world.add( new EditorComponent( new GateXor(i),  GraphicGen.generateGateXor(i),  new Vec2(xo, 512) ) );
			world.add( new EditorComponent( new GateXnor(i), GraphicGen.generateGateXnor(i), new Vec2(xo, 640) ) );
		}
		
		historyManager.markChange();
		
		for (int i=2; i<=8; i++) {
			double xo = 192 * (i-2);
			
			world.add( new EditorComponent( new Mux(i),   new Mux(i)  .getGraphic(), new Vec2(xo,-512) ) );
			world.add( new EditorComponent( new Demux(i), new Demux(i).getGraphic(), new Vec2(xo,-768) ) );
		}
		
		historyManager.markChange();
		
		addMouseOverTest();
	}
	
	
	
	public EditorWorld getHistoryState() {
		return world;
	}
	
	
	
	public void setStateFromHistory( EditorWorld world ) {
		this.world = world;
		repaint();
	}
	
	
	
	public void recentreCamera() {
		cam.interpolateToBbox( world.getWorldExtent(), 64, 3 );
	}
	
	
	
	public Camera getCamera() {
		return cam;
	}
	
	
	
	public EditorWorld getWorld() {
		return world;
	}
	
	
	
	public ToolManager getToolManager() {
		return toolManager;
	}
	
	
	
	public HistoryManager<EditorWorld> getHistoryManager() {
		return historyManager;
	}
	
	
	
	public Bbox2 getWorldViewArea() {
		return cam.getWorldViewArea();
	}
	
	
	
	public Bbox2 getWorldExtent() {
		return world.getWorldExtent();
	}
	
	
	
	public void setAntialiasingEnabled( boolean state ) {
		enableAntialiasing = state;
	}
	
	
	
	public void addRepaintListener( RepaintListener rl ) {
		repaintListeners.add( rl );
	}
	
	
	
	public void removeRepaintListener( RepaintListener rl ) {
		repaintListeners.remove( rl );
	}
	
	
	
	private void addMouseOverTest() {
		addMouseMotionListener( new MouseMotionAdapter() {
			public void mouseMoved( MouseEvent ev ) {
				for (EditorComponent ecom: world.find( cam.getMousePosWorld() )) {
					GraphicPinMapping gpm = ecom.graphic.findClosestPin( cam.getMousePosWorld(), 5 );
					System.out.println( "Ed: " + ecom.com.getName() );
					System.out.println( "Ed: " + gpm );					
				}
			}
		});
	}
	
	
	
	protected void paintComponent( Graphics gx ) {
		Graphics2D g = (Graphics2D) gx;
		
		Gfx.pushMatrix( g );
			Gfx.pushAntialiasingStateAndSet( g, enableAntialiasing );
				Gfx.pushAntialiasingStateAndSet( g, false );
					fillBackground( g );
				Gfx.popAntialiasingState( g );
				
				cam.applyTransform( g );
				drawGrid( g );
				drawDebugCrap( g );
				
				for (EditorComponent ecom: world.getViewableComponents( cam, 64 ))
					ecom.draw( g );
				
				for (RepaintListener rpl: repaintListeners)
					rpl.draw( g );
				
			Gfx.popAntialiasingState( g );
		Gfx.popMatrix( g );
	}
	
	
	
	private void drawDebugCrap( Graphics2D g ) {
		drawTrace( g );
		Vec2 ota   = new Vec2( 448-96, 384-32 );
		Vec2 inter = new Vec2( 448,    384-32 );
		Vec2 otb   = new Vec2( 448+96, 384-32 );
		drawOverlappedTrace( g, ota, inter, otb );
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
		
		Paint lastPaint = g.getPaint(); // TODO abstract this away
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
			Gfx.pushColorAndSet( g, EditorStyle.colBackground );
				g.fillRect( 0, 0, getWidth(), getHeight() );
			Gfx.popColor( g );
		Gfx.popMatrix( g );
	}
	
	
	
	private void drawGrid( Graphics2D g ) {
		Bbox2 worldRegion  = cam.getWorldViewArea();
		Vec2  cellSize     = new Vec2( 64 );
		Vec2  cellSizeHalf = cellSize.multiply( 0.5 );
		Vec2  offset       = worldRegion.tl.modulo( cellSize ).negate().subtract( cellSizeHalf );
		
		worldRegion.tl = worldRegion.tl.subtract( cellSize             );
		worldRegion.br = worldRegion.br.add     ( cellSize.multiply(2) );
		
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
				onTransform();
			}
		};
	}
	
	
	
	/**
	 * When the transform is changed by the camera, repaint and generate mouse events.
	 * The events ensure that tools update their positions when zooming/panning while dragging/placing/etc.
	 */
	private void onTransform() {
		generateMouseEvent();
		repaint();
	}
	
	
	
	private void generateMouseEvent() {
		MouseEvent me = new MouseEvent(
			this,
			MouseEvent.MOUSE_MOVED,
			System.nanoTime(),
			0,
			(int) cam.getMousePosScreen().x,
			(int) cam.getMousePosScreen().y,
			0,
			false,
			MouseEvent.NOBUTTON
		);
		
		for (MouseMotionListener ml: getMouseMotionListeners()) {
			ml.mouseMoved  ( me );
			ml.mouseDragged( me );
		}
	}
}

























