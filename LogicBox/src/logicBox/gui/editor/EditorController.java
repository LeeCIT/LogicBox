


package logicBox.gui.editor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.List;
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
import logicBox.util.Evaluator;
import logicBox.util.Geo;
import logicBox.util.Vec2;



/**
 * Interfaces the editor GUI with its core functions.
 * @author Lee Coakley
 */
public class EditorController implements HistoryListener<EditorWorld>
{
	private EditorFrame                 frame;
	private Camera                      cam;
	private EditorWorld                 world;
	private ToolManager                 toolManager;
	private HistoryManager<EditorWorld> historyManager;
	
	
	
	public EditorController( EditorFrame edframe ) {
		frame = edframe;
		
		world = new EditorWorld();
		cam   = new Camera();
		
		historyManager = new HistoryManager<>( this );
		historyManager.markChange();
		
		toolManager = new ToolManager( this );
		
		addDebugAndDemoStuff();
		//getEditorPanel().addWorldRepaintListener( world.getSpatialGridDebugRepainter() );
	}
	
	
	
	/**
	 * Get the frame associated with the controller.
	 */
	public EditorFrame getEditorFrame() {
		return frame;
	}
	
	
	
	public EditorPanel getEditorPanel() {
		return frame.getEditorPanel();
	}
	
	
	
	public EditorWorld getHistoryState() {
		return world;
	}
	
	
	
	public void setStateFromHistory( EditorWorld world ) {
		this.world = world;
		this.world.clearGraphicSelectionAndHighlightStates();
		getEditorPanel().repaint();
	}
	
	
	
	public void recentreCamera() {
		Bbox2  extent = world.getWorldExtent();
		double border = 64;
		double dist   = Geo.distance( extent.getCentre(), cam.getPan() );
		double frac   = Geo.unlerp( dist, 512, 4096 );
		double secs   = Geo.lerp( 2, 5, frac );
		cam.interpolateToBbox( extent, border, secs );
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
	
	
	
	public Evaluator<Bbox2> getWorldExtentEvaluator() {
		return new Evaluator<Bbox2>() {
			public Bbox2 evaluate() {
				return world.getWorldExtent();
			}
		};
	}
	
	
	
	public Evaluator<Bbox2> getViewExtentEvaluator() {
		return new Evaluator<Bbox2>() {
			public Bbox2 evaluate() {
				return cam.getWorldViewArea();
			}
		};
	}
	
	
	
	public Evaluator<List<Graphic>> getViewableGraphicsEvaluator() {
		return new Evaluator<List<Graphic>>() {
			public List<Graphic> evaluate() {
				return world.getViewableComponentGraphics( cam, 64 );
			}
		};
	}
	
	
	
	public void addDebugAndDemoStuff() {
		world.add( new EditorComponentActive( new GateBuffer(), GraphicGen.generateGateBuffer(), new Vec2(  0, -128) ) );
		world.add( new EditorComponentActive( new GateNot(),    GraphicGen.generateGateNot(),    new Vec2(  0, -256) ) );
		
		historyManager.markChange();
		
		for (int i=2; i<=4; i++) {
			double xo = 192 * (i-2);
			
			world.add( new EditorComponentActive( new GateAnd(i),  GraphicGen.generateGateAnd(i),  new Vec2(xo, 0)   ) );
			world.add( new EditorComponentActive( new GateNand(i), GraphicGen.generateGateNand(i), new Vec2(xo, 128) ) );
			world.add( new EditorComponentActive( new GateOr(i),   GraphicGen.generateGateOr(i),   new Vec2(xo, 256) ) );
			world.add( new EditorComponentActive( new GateNor(i),  GraphicGen.generateGateNor(i),  new Vec2(xo, 384) ) );
			world.add( new EditorComponentActive( new GateXor(i),  GraphicGen.generateGateXor(i),  new Vec2(xo, 512) ) );
			world.add( new EditorComponentActive( new GateXnor(i), GraphicGen.generateGateXnor(i), new Vec2(xo, 640) ) );
		}
		
		historyManager.markChange();
		
		for (int i=2; i<=8; i++) {
			double xo = 192 * (i-2);
			
			world.add( new EditorComponentActive( new Mux(i),   new Mux(i)  .getGraphic(), new Vec2(xo,-512) ) );
			world.add( new EditorComponentActive( new Demux(i), new Demux(i).getGraphic(), new Vec2(xo,-768) ) );
		}
		
		historyManager.markChange();
		
		addMouseOverTest();
	}
	
	
	
	protected Callback getPanelOnTransformCallback() {
		return new Callback() {
			public void execute() {
				onTransform();
			}
		};
	}
	
	
	
	/**
	 * When the transform is changed by the camera, repaint and generate mouse events.
	 * The events ensure that tools update their positions when simultaneously zooming/panning and dragging/placing/etc.
	 */
	private void onTransform() {
		generateMouseEvent();
		getEditorPanel().repaint();
	}
	
	
	
	private void generateMouseEvent() {
		MouseEvent me = new MouseEvent(
			getEditorPanel(),
			MouseEvent.MOUSE_MOVED,
			System.nanoTime(),
			0,
			(int) cam.getMousePosScreen().x,
			(int) cam.getMousePosScreen().y,
			0,
			false,
			MouseEvent.NOBUTTON
		);
		
		for (MouseMotionListener ml: getEditorPanel().getMouseMotionListeners()) {
			ml.mouseMoved  ( me );
			ml.mouseDragged( me );
		}
	}
	
	
	
	private void addMouseOverTest() {
		getEditorPanel().addMouseMotionListener( new MouseMotionAdapter() {
			public void mouseMoved( MouseEvent ev ) {
				for (EditorComponent ecom: world.find( cam.getMousePosWorld() )) {
					GraphicPinMapping gpm = ecom.findPinNear( cam.getMousePosWorld(), 5 );
					System.out.println( "Ed: " + ecom.com.getName() );
					System.out.println( "Ed: " + gpm );					
				}
			}
		});
	}
}
