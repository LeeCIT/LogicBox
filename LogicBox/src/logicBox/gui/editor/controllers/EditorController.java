


package logicBox.gui.editor.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import logicBox.gui.DialogueAnswer;
import logicBox.gui.GUI;
import logicBox.gui.cloud.CloudController;
import logicBox.gui.cloud.FilePanel;
import logicBox.gui.cloud.LoginPanel;
import logicBox.gui.cloud.RegisterPanel;
import logicBox.gui.editor.*;
import logicBox.gui.editor.components.*;
import logicBox.gui.editor.graphics.*;
import logicBox.gui.editor.toolbox.Toolbox;
import logicBox.gui.editor.tools.ToolManager;
import logicBox.gui.edtior.printing.EditorPrinter;
import logicBox.gui.help.AboutDialogue;
import logicBox.gui.help.HelpFrame;
import logicBox.sim.component.ComponentType;
import logicBox.sim.component.connective.Pin;
import logicBox.sim.component.simple.SourceOscillator;
import logicBox.util.Bbox2;
import logicBox.util.Callback;
import logicBox.util.CallbackRepeater;
import logicBox.util.Evaluator;
import logicBox.util.Geo;
import logicBox.util.Util;



/**
 * Interfaces the editor GUI with its core functions.
 * @author Lee Coakley
 */
public class EditorController implements HistoryListener<EditorWorld>
{
	private EditorFrame                 frame;
	private Camera                      cam;
	private EditorWorld                 world;
	private CallbackRepeater            baseClockSignal;
	private ToolManager                 toolManager;
	private HistoryManager<EditorWorld> historyManager;
	
	private File    circuitFile; // File currently being affected by save command
	private boolean isUnsaved;   // File is new and unsaved
	private boolean needsToSave; // File exists on disk, but in different form
	
	
	
	public EditorController( EditorFrame frame ) {
		this.frame           = frame;
		this.world           = new EditorWorld();
		this.cam             = new Camera();
		this.historyManager  = new HistoryManager<>( this );
		this.toolManager     = new ToolManager( this );
		this.baseClockSignal = createBaseClockSignal();
		
		addNeedToSaveCallback();
		initialiseCircuit( false );
		
		baseClockSignal.unpause();
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
		boolean wasPowerOn = this.world.isPowerOn();
		
		this.world = world;
		this.world.clearGraphicSelectionAndHighlightStates();
		
		if (wasPowerOn)
			 this.world.simPowerOn();
		else this.world.simPowerOff();
		
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
	
	
	
	/**
	 * If enabled the sim runs as fast as the CPU can manage.
	 */
	public void setSimTurboEnabled( boolean turbo ) {
		long interval = getBaseClockPeriod();
		
		if (turbo)
			interval = 0;
		
		baseClockSignal.setInterval( interval );
	}
	
	
	
	public boolean isSimTurboEnabled() {
		return baseClockSignal.getInterval() == 0;
	}
	
	
	
	protected Evaluator<Bbox2> getWorldExtentEvaluator() {
		return new Evaluator<Bbox2>() {
			public Bbox2 evaluate() {
				return getWorld().getWorldExtent();
			}
		};
	}
	
	
	
	protected Evaluator<Bbox2> getViewExtentEvaluator() {
		return new Evaluator<Bbox2>() {
			public Bbox2 evaluate() {
				return getCamera().getWorldViewArea();
			}
		};
	}
	
	
	
	protected Evaluator<List<Graphic>> getViewableGraphicsEvaluator() {
		return new Evaluator<List<Graphic>>() {
			public List<Graphic> evaluate() {
				return getWorld().getViewableComponentGraphics( cam, 64 );
			}
		};
	}
	
	
	
	public void onCloseButtonPressed() {
		if (canDiscardCircuit()) {
			// TODO save prefs, cloud sync, etc
			baseClockSignal.join();
			System.exit( 0 );
		}
	}
	
	
	
	public void powerOn() {
		getWorld().simPowerOn();
		getEditorPanel().repaint();
	}
	
	
	
	public void powerReset() {
		getWorld().simPowerReset();
		getEditorPanel().repaint();
	}
	
	
	
	public void powerOff() {
		getWorld().simPowerOff();
		getEditorPanel().repaint();
	}
	
	
	
	public void showHelpFor( ComponentType type ) {
		HelpFrame helpFrame = showHelp();
		helpFrame.showInfoFor( type );
		helpFrame.setSelectedComponent( type );
	}
	
	
	
	private HelpFrame showHelp() {
		HelpFrame helpFrame = HelpFrame.getInstance();
		
		helpFrame.setVisible( true );
		helpFrame.setLocationRelativeTo( getEditorFrame() );
		
		return helpFrame;
	}
	
	
	
	private void toggleHelp() {
		HelpFrame helpFrame = HelpFrame.getInstance();
		
		if (helpFrame.isVisible())
			 helpFrame.dispose();
		else showHelp();
	}
	
	
	
	private CallbackRepeater createBaseClockSignal() {
		Callback cb = new Callback() {
			public void execute() {
				sendClockSignalAndRepaint();
			}
		};
		
		return new CallbackRepeater(
			getBaseClockPeriod(),
			true,
			cb
		);
	}
	
	
	
	private long getBaseClockPeriod() {
		return (int) Geo.hertzToMillisecs( SourceOscillator.baseFrequencyHz );
	}



	private void sendClockSignalAndRepaint() { // Never call this manually - must be done by BCS thread
		if (getWorld().sendClockSignal()) {
			SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					getEditorPanel().repaint();
				}
			});
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
		return DialogueAnswer.YES == GUI.askYesNoCancel(
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
	
	
	
	private void openCircuitFromFile( File file ) {
		EditorWorld world = null;
		
		try {
			world = Storage.load( file );
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
			isUnsaved   = false;
			needsToSave = false;
			
			this.world = world;
			historyManager.clear();
			historyManager.markChange( "<initial state>" );
			needsToSave = false; // Got reset by change marking
			
			circuitFile = file;
			frame.setCircuitName( circuitFile.getName() );
			frame.setCircuitModified( false );
			
			recentreCamera();
			powerOff();
		}
	}
	
	
	
	private void saveToCircuitFile() {
		try {
			EditorWorld saveMe = Util.deepCopy( world );
			saveMe.clearGraphicSelectionAndHighlightStates();
			saveMe.simPowerOff();
			
			Storage.save( circuitFile, saveMe );
			frame.setCircuitName( circuitFile.getName() );
			frame.setCircuitModified( false );
			isUnsaved   = false;
			needsToSave = false;
			
			if(CloudController.getUser() != null)
				CloudController.syncFile(circuitFile.getAbsoluteFile());
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
	
	
	
	protected ActionListener getNewAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				if ( ! canDiscardCircuit())
					return;
				
				initialiseCircuit( true );
			}
		};
	}
	
	
	
	protected ActionListener getOpenAction() {
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
	
	
	
	protected ActionListener getSaveAction() {
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
	
	
	
	protected ActionListener getSaveAsAction() {
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



	protected ActionListener getPrintAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				new EditorPrinter( getEditorFrame() );
			}
		};
	}
	
	
	
	protected ActionListener getCutAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				toolManager.cut();
			}
		};
	}
	
	
	
	protected ActionListener getCopyAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				toolManager.copy();
			}
		};
	}
	
	
	
	protected ActionListener getPasteAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				toolManager.paste();
			}
		};
	}
	
	
	
	protected ActionListener getDeleteAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				getToolManager().delete();
			}
		};
	}
	
	
	
	protected ActionListener getSelectAllAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				getToolManager().selectAll();
			}
		};
	}
	
	
	
	protected ActionListener getSelectNoneAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				getToolManager().selectNone();
			}
		};
	}
	
	
	
	protected ActionListener getSelectInvertAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				getToolManager().selectInvert();
			}
		};
	}
	
	
	
	protected ActionListener getSelectBlackBoxAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				try {
					getToolManager().selectCreateBlackBox();
				}
				catch (BlackBoxCreator.NoBlackBoxPinsException ex) {
					GUI.showError( getEditorFrame(), "Can't Create Black-Box From Selection", "There are no black-box pins in the selection." );
				}
			}
		};
	}
	
	
	
	protected ActionListener getGridToggleAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				EditorPanel panel = getEditorPanel();
				panel.setGridEnabled( ! panel.getGridEnabled() );
			}
		};
	}
	
	
	
	protected ActionListener getRecentreCameraAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				recentreCamera();
			}
		};
	}
	
	
	
	protected ActionListener getToolboxToggleAction() {
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
	
	
	
	protected ActionListener getHelpAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				toggleHelp();
			}
		};
	}
	
	
	
	protected ActionListener getAboutAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				new AboutDialogue( getEditorFrame() );
			}
		};
	}



	protected ActionListener getUndoAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				historyAction( true );
			}
		};
	}
	
	
	
	protected ActionListener getRedoAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				historyAction( false );
			}
		};
	}
	
	
	
	protected ActionListener getLoginAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				LoginPanel.getInstance();
			}
		};
	}
	
	
	
	protected ActionListener getLogoutAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				CloudController.handleLogoutRequest();
			}
		};
	}
	
	
	
	protected ActionListener getRegisterAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				RegisterPanel.getInstance();
			}
		};
	}
	
	
	
	protected ActionListener getFilesAction() {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				File f = FilePanel.openFile();
				
				if(f != null)
					openCircuitFromFile(f);
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
					System.out.println();
					System.out.println( "Ed: " + ecom.getComponent().getName() );
					System.out.println( "Ed: " + gpm );
					
					if (gpm != null)
					if (ecom instanceof EditorComponentActive) {
						Pin pin = SimMapper.getMappedPin((EditorComponentActive)ecom,gpm);
						System.out.println( "Ed: " + pin );
					}
				}
			}
		});
	}
}
