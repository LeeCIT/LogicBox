


package logicBox.gui.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import logicBox.fileManager.FileManager;
import logicBox.gui.GUI;
import logicBox.gui.editor.toolbox.Toolbox;
import logicBox.gui.editor.tools.ToolManager;
import logicBox.gui.edtior.printing.EditorPrinter;
import logicBox.gui.help.HelpFrame;
import logicBox.sim.Simulation;
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
import logicBox.util.Storage;
import logicBox.util.Util;
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
	
	private File    circuitFile; // File currently being affected by save command
	private boolean isUnsaved;   // File is new and unsaved
	private boolean needsToSave; // File exists on disk, but in different form
	
	
	
	public EditorController( EditorFrame edframe ) {
		frame = edframe;
		
		world          = new EditorWorld();
		cam            = new Camera();
		historyManager = new HistoryManager<>( this );
		toolManager    = new ToolManager( this );
		
		addNeedToSaveCallback();
		initialiseCircuit( false );
		
		addDebugAndDemoStuff();
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
	
	
	
	public void onCloseButtonPressed() {
		if (canDiscardCircuit()) {
			// TODO save prefs, cloud sync, etc
			System.exit( 0 );
		}
	}
	
	
	
	public void powerOn() {
		doWithLoopErrorDetect( new Callback() {
			public void execute() {
				world.simPowerOn();
				getEditorPanel().repaint();
			}
		});
	}
	
	
	
	public void powerReset() {
		doWithLoopErrorDetect( new Callback() {
			public void execute() {
				world.simPowerReset();
				getEditorPanel().repaint();
			}
		});
	}
	
	
	
	public void powerOff() {
		doWithLoopErrorDetect( new Callback() {
			public void execute() {
				world.simPowerOff();
				getEditorPanel().repaint();
			}
		});
	}
	
	
	
	private void doWithLoopErrorDetect( Callback cb ) {
		try {
			cb.execute();
		}
		catch (Simulation.NonLevelisableCircuitException ex) {
			GUI.showError(
				frame,
				"Circuit Contains Loop",
				"The circuit contains a loop.  This version of LogicBox does not support loops."
			);
		}
	}
	
	
	
	private boolean canDiscardCircuit() {
		boolean unsavedAndNotEmpty = isUnsaved && !world.isEmpty();
		
		if (unsavedAndNotEmpty || needsToSave)
			 return askUserToDiscard();
		else return true;
	}
	
	
	
	private boolean askUserToOverwrite( File file ) {
		return GUI.askConfirm(
			getEditorFrame(),
			"Overwrite File?",
			"A file called " + file.getName() + " already exists.  Do you really want to overwrite it?"
		);
	}
	
	
	
	private boolean askUserToDiscard() {
		return GUI.askConfirm(
			getEditorFrame(),
			"Discard Unsaved Changes?",
			"You have unsaved changes.  Do you really want to discard them?"
		);
	}
	
	
	
	private void addNeedToSaveCallback() {
		Callback onChange = new Callback() {
			public void execute() {
				needsToSave = true;
				frame.setCircuitModified( true );
			}
		};
		
		historyManager.addOnChangeCallback  ( onChange );
		historyManager.addOnUndoRedoCallback( onChange );
	}
	
	
	
	private void initialiseCircuit( boolean recentre ) {
		world.clear();
		historyManager.clear();
		historyManager.markChange( "<initial state>" );
		
		frame.setCircuitName    ( "New Circuit" );
		frame.setCircuitModified( false );
		frame.repaint();
		
		circuitFile = null;
		isUnsaved   = true;
		needsToSave = false;
		
		if (recentre)
			recentreCamera();
	}
	
	
	
	private void saveToCircuitFile() {
		try {
			EditorWorld saveMe = Util.deepCopy( world );
			saveMe.clearGraphicSelectionAndHighlightStates();
			
			Storage.write( circuitFile.getPath(), saveMe ); // TODO compress + version
			frame.setCircuitName( circuitFile.getName() );
			frame.setCircuitModified( false );
			isUnsaved   = false;
			needsToSave = false;
			// TODO cloud stuff here - sync file in background thread
		}
		catch (Exception ex) {
			ex.printStackTrace();
			GUI.showError(
				getEditorFrame(),
				"Save Failed",
				"Couldn't save!  Try saving it somewhere else.\n\n" +
				"Technical details:\n" + ex
			);
		}
	}
	
	
	
	private void openCircuitFromFile( File file ) {
		EditorWorld world = null;
		
		try {
			world = Storage.read( file.getPath(), EditorWorld.class );
		}
		catch (Exception ex) {
			ex.printStackTrace();
			GUI.showError(
				getEditorFrame(),
				"Failed to Open Circuit",
				"Couldn't open the file.  This is bad!\n\n" +
				"Technical details:\n" + ex
			);
		}
		
		if (world != null) {
			initialiseCircuit( false );
			isUnsaved = false;
			
			circuitFile = file;
			frame.setCircuitName( circuitFile.getName() );
			
			this.world = world;
			
			recentreCamera();
			powerOff();
		}
	}
	
	
	
	public ActionListener getNewAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				if ( ! canDiscardCircuit())
					return;
				
				initialiseCircuit( true );
			}
		};
	}
	
	
	
	public ActionListener getOpenAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				if ( ! canDiscardCircuit())
					return;
				
				FileManager fileManager = new FileManager( frame );
				File        file        = fileManager.openFile();
				
				if (file != null)
					openCircuitFromFile( file );
			}
		};
	}
	
	
	
	public ActionListener getSaveAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				if (isUnsaved) { // Redirect to save-as if unsaved
					getSaveAsAction().actionPerformed( ev );
					return;
				}
				
				saveToCircuitFile();
			}
		};
	}
	
	
	
	public ActionListener getSaveAsAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				FileManager fileManager = new FileManager( frame );
				File        file        = fileManager.saveFile();
				
				if (file != null) {
					if ( ! file.getPath().endsWith( FileManager.fileExtension ))
						file = new File( file + FileManager.fileExtension );
					
					if (file.exists())
					if ( ! askUserToOverwrite( file ))
						return;
					
					circuitFile = file;
					saveToCircuitFile();
				}
			}
		};
	}



	public ActionListener getPrintAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				new EditorPrinter( getEditorFrame() );
			}
		};
	}
	
	
	
	public ActionListener getCutAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				
			}
		};
	}
	
	
	
	public ActionListener getCopyAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				
			}
		};
	}
	
	
	
	public ActionListener getPasteAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				
			}
		};
	}
	
	
	
	public ActionListener getDeleteAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				getToolManager().delete();
			}
		};
	}
	
	
	
	public ActionListener getSelectAllAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				getToolManager().selectAll();
			}
		};
	}
	
	
	
	public ActionListener getSelectNoneAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				getToolManager().selectNone();
			}
		};
	}
	
	
	
	public ActionListener getSelectInvertAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				getToolManager().selectInvert();
			}
		};
	}
	
	
	
	public ActionListener getGridToggleAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				EditorPanel panel = getEditorPanel();
				panel.setGridEnabled( ! panel.getGridEnabled() );
			}
		};
	}
	
	
	
	public ActionListener getRecentreCameraAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				recentreCamera();
			}
		};
	}
	
	
	
	public ActionListener getToolboxToggleAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				Toolbox toolbox = Toolbox.getInstance();
				
				if (toolbox == null) {
					toolbox = new Toolbox( GUI.getMainFrame() );
					toolbox.setActiveToolManager( getToolManager() );
				} else {
					toolbox.dispose();
				}
			}
		};
	}
	
	
	
	public ActionListener getHelpAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				new HelpFrame(); // TODO this is temporary, change it later
			}
		};
	}
	
	
	
	public ActionListener getUndoAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				historyAction( true );
			}
		};
	}
	
	
	
	public ActionListener getRedoAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				historyAction( false );
			}
		};
	}
	
	
	
	private void historyAction( boolean undoing ) {
		EditorFrame                 frame   = getEditorFrame();
		HistoryManager<EditorWorld> manager = getHistoryManager();
		
		JMenuItem menuUndo = frame.getEditorMenuBar().itemEditUndo;
		JButton   buttUndo = frame.getEditorToolbar().buttUndo;
		JMenuItem menuRedo = frame.getEditorMenuBar().itemEditRedo;
		JButton   buttRedo = frame.getEditorToolbar().buttRedo;
		
		if (undoing)
			 manager.undo();
		else manager.redo();
		
		boolean canUndo = manager.canUndo();
		boolean canRedo = manager.canRedo();
		
		menuUndo.setEnabled( canUndo );
		buttUndo.setEnabled( canUndo );
		menuRedo.setEnabled( canRedo );
		buttRedo.setEnabled( canRedo );
	}
	
	
	
	private void addDebugAndDemoStuff() {
		world.add( new EditorComponentActive( new GateBuffer(), GraphicGen.generateGateBuffer(), new Vec2(  0, -128) ) );
		world.add( new EditorComponentActive( new GateNot(),    GraphicGen.generateGateNot(),    new Vec2(  0, -256) ) );
		
		historyManager.markChange( "test" );
		
		for (int i=2; i<=4; i++) {
			double xo = 192 * (i-2);
			
			world.add( new EditorComponentActive( new GateAnd(i),  GraphicGen.generateGateAnd(i),  new Vec2(xo, 0)   ) );
			world.add( new EditorComponentActive( new GateNand(i), GraphicGen.generateGateNand(i), new Vec2(xo, 128) ) );
			world.add( new EditorComponentActive( new GateOr(i),   GraphicGen.generateGateOr(i),   new Vec2(xo, 256) ) );
			world.add( new EditorComponentActive( new GateNor(i),  GraphicGen.generateGateNor(i),  new Vec2(xo, 384) ) );
			world.add( new EditorComponentActive( new GateXor(i),  GraphicGen.generateGateXor(i),  new Vec2(xo, 512) ) );
			world.add( new EditorComponentActive( new GateXnor(i), GraphicGen.generateGateXnor(i), new Vec2(xo, 640) ) );
		}
		
		historyManager.markChange( "test" );
		
		for (int i=2; i<=8; i++) {
			double xo = 192 * (i-2);
			
			world.add( new EditorComponentActive( new Mux(i),   new Mux(i)  .getGraphic(), new Vec2(xo,-512) ) );
			world.add( new EditorComponentActive( new Demux(i), new Demux(i).getGraphic(), new Vec2(xo,-768) ) );
		}
		
		historyManager.markChange( "test" );
		
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
					System.out.println( "Ed: " + ecom.getComponent().getName() );
					System.out.println( "Ed: " + gpm );					
				}
			}
		});
	}
}
