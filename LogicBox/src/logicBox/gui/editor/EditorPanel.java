


package logicBox.gui.editor;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import logicBox.gui.Gfx;
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
	
	private boolean enableGrid;
	private boolean enableAntialiasing;
	private boolean isPrinting;
	
	
	
	public EditorPanel() {
		super( true );
		
		enableGrid         = true;
		enableAntialiasing = true;
		repaintListeners   = new ArrayList<>();
		
		world = new EditorWorld();
		
		cam = new Camera( this );
		cam.addTransformCallback( createOnTransformCallback() );
		
		historyManager = new HistoryManager<>( this );
		historyManager.markChange();
		
		toolManager = new ToolManager( this );
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
		this.world.clearGraphicSelectionAndHighlightStates();
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
		repaint();
	}
	
	
	
	public boolean getAntialiasingEnabled() {
		return enableAntialiasing;
	}
	
	
	
	public void setGridEnabled( boolean state ) {
		enableGrid = state;
		repaint();
	}
	
	
	
	public boolean getGridEnabled() {
		return enableGrid;
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
	
	
	
	public void print( Graphics g ) {
		// TODO change background colour, zoom out etc
		System.out.println( "Printing " + this );
		isPrinting = true;
		super.print( g );
		isPrinting = false;
	}
	
	
	
	protected void paintComponent( Graphics g ) {
		draw( (Graphics2D) g );
	}
	
	
	
	private void draw( Graphics2D g ) {
		Gfx.pushMatrix( g );
			Gfx.pushAntialiasingStateAndSet( g, enableAntialiasing );
				fillBackground( g );
				cam.applyTransform( g );
				drawGrid( g );
				drawEditorComponents( g );
				drawRepaintListeners( g );
			Gfx.popAntialiasingState( g );
		Gfx.popMatrix( g );
	}
	
	
	
	private void drawEditorComponents( Graphics2D g ) {
		for (EditorComponent ecom: world.getViewableComponents( cam, 64 )) {
			Graphic graphic     = ecom.getGraphic();
			boolean wasInverted = graphic.isInverted();
			
			graphic.setInverted( isPrinting );
			graphic.draw( g );
			graphic.setInverted( wasInverted );
		}
	}
	
	
	
	private void drawRepaintListeners( Graphics2D g ) {
		for (RepaintListener rpl: repaintListeners)
			rpl.draw( g );
	}
	
	
	
	private void fillBackground( Graphics2D g ) {
		Gfx.pushColorAndSet( g, (isPrinting) ? Color.white : EditorStyle.colBackground );
			Gfx.pushAntialiasingStateAndSet( g, false );
				g.fillRect( 0, 0, getWidth(), getHeight() );
			Gfx.popAntialiasingState( g );
		Gfx.popColor( g );
	}
	
	
	
	private void drawGrid( Graphics2D g ) {
		if ( ! enableGrid || isPrinting)
			return;
		
		Bbox2 worldRegion  = cam.getWorldViewArea();
		Vec2  cellSize     = new Vec2( 64 );
		Vec2  cellSizeHalf = cellSize.multiply( 0.5 );
		Vec2  offset       = worldRegion.tl.modulo( cellSize ).negate().subtract( cellSizeHalf );
		
		worldRegion = worldRegion.expand( cellSize.x * 3.0 );
		
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

























