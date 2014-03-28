


package logicBox.gui.editor;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JScrollBar;
import logicBox.gui.editor.menubar.EditorMenuBar;
import logicBox.gui.editor.toolbar.EditorToolbar;
import logicBox.gui.editor.toolbox.Toolbox;
import logicBox.util.Bbox2;
import logicBox.util.Callback;
import logicBox.util.Evaluator;



/**
 * Creates and links the GUI components and controllers comprising the main editor.
 * @author Lee Coakley
 */
public class EditorCreator
{
	public static EditorFrame createEditorFrame( boolean withToolbox ) {
		EditorPanel     panel   = new EditorPanel();
		EditorMenuBar   menubar = new EditorMenuBar();
		EditorToolbar   toolbar = new EditorToolbar();
		EditorScrollBar scrollX = new EditorScrollBar( JScrollBar.HORIZONTAL );
		EditorScrollBar scrollY = new EditorScrollBar( JScrollBar.VERTICAL   );
		EditorFrame     frame   = new EditorFrame( panel, menubar, toolbar, scrollX, scrollY );
		
		frame.pack();
		frame.setSize       ( new Dimension(720, 640) );
		frame.setMinimumSize( new Dimension(640, 480) );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setVisible( true );
		
		EditorController ctrl = new EditorController( frame );
		linkFrame    ( ctrl, frame   );
		linkPanel    ( ctrl, panel   );
		linkScrollbar( ctrl, scrollX );
		linkScrollbar( ctrl, scrollY );
		
		if (withToolbox) {
			Toolbox toolbox = new Toolbox( frame );
			toolbox.setActiveToolManager( ctrl.getToolManager() );
		}
		
		linkAction( Actions.getNewAction   (ctrl), toolbar.buttNew,   menubar.itemFileNew    );
		linkAction( Actions.getOpenAction  (ctrl), toolbar.buttOpen,  menubar.itemFileOpen   );
		linkAction( Actions.getSaveAction  (ctrl), toolbar.buttSave,  menubar.itemFileSave   );
		linkAction( Actions.getSaveAsAction(ctrl), null, 			  menubar.itemFileSaveAs );
		linkAction( Actions.getPrintAction (ctrl), toolbar.buttPrint, menubar.itemFilePrint  );
		
		linkActionUndoRedo( ctrl, menubar, toolbar );
		
		linkAction( Actions.getGridToggleAction    (ctrl), toolbar.buttToggleGrid,     menubar.itemViewGrid   );
		linkAction( Actions.getRecentreCameraAction(ctrl), toolbar.buttCameraRecentre, menubar.itemViewCamera );
		
		linkAction( Actions.getHelpAction(ctrl), toolbar.buttHelp, menubar.itemHelpHelp );
		
		return frame;
	}
	
	
	
	private static void linkFrame( final EditorController ctrl, EditorFrame frame ) {
		frame.addWindowListener( new WindowAdapter() {
			public void windowActivated( WindowEvent e ) {
				Toolbox.getInstance().setActiveToolManager( ctrl.getToolManager() );
			}
		});
	}
	
	
	
	
	private static void linkPanel( EditorController ctrl, EditorPanel panel ) {
		Camera                   cam  = ctrl.getCamera();
		Evaluator<List<Graphic>> eval = ctrl.getViewableGraphicsEvaluator();
		
		panel.setCamera( cam );
		panel.setViewablesEvaluator( eval );
		
		cam.attachTo( panel );
		cam.addTransformCallback( ctrl.getPanelOnTransformCallback() );
		cam.updateTransform();
	}
	
	
	
	private static void linkScrollbar( EditorController ctrl, EditorScrollBar scrollbar ) {
		Evaluator<Bbox2> evalWorld = ctrl.getWorldExtentEvaluator();
		Evaluator<Bbox2> evalView  = ctrl.getViewExtentEvaluator();
		Camera           cam       = ctrl.getCamera();
		
		scrollbar.attachTo( cam, evalWorld, evalView );
	}
	
	
	
	private static void linkAction( ActionListener action, JButton button, JMenuItem menuItem ) {
		if (button   != null) button  .addActionListener( action );
		if (menuItem != null) menuItem.addActionListener( action );
	}
	
	
	
	private static void linkActionUndoRedo( EditorController ctrl, final EditorMenuBar menubar, final EditorToolbar toolbar ) {
		linkAction( Actions.getUndoAction(ctrl), toolbar.buttUndo, menubar.itemEditUndo );
		linkAction( Actions.getRedoAction(ctrl), toolbar.buttRedo, menubar.itemEditRedo );
		
		menubar.itemEditUndo.setEnabled( false );
		menubar.itemEditRedo.setEnabled( false );
		toolbar.buttUndo    .setEnabled( false );
		toolbar.buttRedo    .setEnabled( false );
		
		final HistoryManager<EditorWorld> manager = ctrl.getHistoryManager();
		
		manager.addOnChangeCallback( new Callback() {
			public void execute() {
				menubar.itemEditUndo.setEnabled( manager.canUndo() );
				menubar.itemEditRedo.setEnabled( manager.canRedo() );
				toolbar.buttUndo    .setEnabled( manager.canUndo() );
				toolbar.buttRedo    .setEnabled( manager.canRedo() );
			}
		});
	}
}




