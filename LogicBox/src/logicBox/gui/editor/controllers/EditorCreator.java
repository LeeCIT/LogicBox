


package logicBox.gui.editor.controllers;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JScrollBar;
import logicBox.gui.editor.Camera;
import logicBox.gui.editor.EditorFrame;
import logicBox.gui.editor.EditorPanel;
import logicBox.gui.editor.EditorScrollBar;
import logicBox.gui.editor.EditorWorld;
import logicBox.gui.editor.graphics.Graphic;
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
	/**
	 * Create the main GUI and link all controllers up.
	 * @param isPrimary Indicates that this is the first frame, and main controller.
	 * @return The newly created frame.
	 */
	public static EditorFrame createEditorFrame( boolean isPrimary ) {
		EditorPanel     panel   = new EditorPanel();
		EditorMenuBar   menubar = new EditorMenuBar();
		EditorToolbar   toolbar = new EditorToolbar();
		EditorScrollBar scrollX = new EditorScrollBar( JScrollBar.HORIZONTAL );
		EditorScrollBar scrollY = new EditorScrollBar( JScrollBar.VERTICAL   );
		EditorFrame     frame   = new EditorFrame( panel, menubar, toolbar, scrollX, scrollY );
		
		frame.pack();
		frame.setSize       ( new Dimension(720, 640) );
		frame.setMinimumSize( new Dimension(640, 480) );
		frame.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		frame.setVisible( true );
		
		EditorController ctrl = new EditorController( frame );
		linkFrame    ( ctrl, frame   );
		linkPanel    ( ctrl, panel   );
		linkScrollbar( ctrl, scrollX );
		linkScrollbar( ctrl, scrollY );
		
		if (isPrimary) {
			Toolbox toolbox = new Toolbox( frame );
			toolbox.setActiveToolManager( ctrl.getToolManager() );
		}
		
		linkMainActions( ctrl, frame, menubar, toolbar );
		
		if (isPrimary)
			ctrl.openCircuitFromCliArgs();
		
		return frame;
	}



	private static void linkMainActions( EditorController ctrl, EditorFrame frame, EditorMenuBar menubar, EditorToolbar toolbar ) {
		linkAction( ctrl.getNewAction   (), toolbar.buttNew,   menubar.itemFileNew    );
		linkAction( ctrl.getOpenAction  (), toolbar.buttOpen,  menubar.itemFileOpen   );
		linkAction( ctrl.getSaveAction  (), toolbar.buttSave,  menubar.itemFileSave   );
		linkAction( ctrl.getSaveAsAction(), null,              menubar.itemFileSaveAs );
		linkAction( ctrl.getPrintAction (), toolbar.buttPrint, menubar.itemFilePrint  );
		
		linkActionUndoRedo( ctrl, menubar, toolbar );
		linkAction( ctrl.getCutAction           (), toolbar.buttCut,            menubar.itemEditCut            );
		linkAction( ctrl.getCopyAction          (), toolbar.buttCopy,           menubar.itemEditCopy           );
		linkAction( ctrl.getPasteAction         (), toolbar.buttPaste,          menubar.itemEditPaste          );
		linkAction( ctrl.getDeleteAction        (), null,                       menubar.itemEditDelete         );
		linkAction( ctrl.getSelectAllAction     (), toolbar.buttSelectAll,      menubar.itemEditSelectAll      );
		linkAction( ctrl.getSelectNoneAction    (), toolbar.buttSelectNone,     menubar.itemEditSelectNone     );
		linkAction( ctrl.getSelectInvertAction  (), toolbar.buttSelectInvert,   menubar.itemEditSelectInvert   );
		linkAction( ctrl.getSelectBlackBoxAction(), toolbar.buttSelectBlackBox, menubar.itemEditSelectBlackBox );
		
		linkAction( ctrl.getGridToggleAction    (), toolbar.buttToggleGrid,     menubar.itemViewGrid      );
		linkAction( ctrl.getRecentreCameraAction(), toolbar.buttCameraRecentre, menubar.itemViewCamera    );
		linkAction( ctrl.getToolboxToggleAction (), toolbar.buttToggleToolbox,  menubar.itemViewToolbox   );
		linkAction( ctrl.getZoomResetAction     (), null,                       menubar.itemViewZoomReset );
		linkAction( ctrl.getZoomInAction        (), null,                       menubar.itemViewZoomIn    );
		linkAction( ctrl.getZoomOutAction       (), null,                       menubar.itemViewZoomOut   );
		
		linkAction( ctrl.getHelpAction (), toolbar.buttHelp, menubar.itemHelpHelp  );
		linkAction( ctrl.getAboutAction(), null,             menubar.itemHelpAbout );
		
		linkAction( ctrl.getLoginAction   (), null, menubar.itemCloudLogin    );
		linkAction( ctrl.getLogoutAction  (), null, menubar.itemCloudLogout   );
		linkAction( ctrl.getRegisterAction(), null, menubar.itemCloudRegister );
		linkAction( ctrl.getFilesAction   (), null, menubar.itemCloudFiles    );
	}
	
	
	
	private static void linkFrame( final EditorController ctrl, EditorFrame frame ) {
		frame.addWindowListener( new WindowAdapter() {
			public void windowActivated( WindowEvent ev ) {
				Toolbox toolbox = Toolbox.getInstance();
				
				if (toolbox != null)
					toolbox.setActiveToolManager( ctrl.getToolManager() );
			}
		});
		
		
		frame.addWindowListener( new WindowAdapter() {
			public void windowClosing( WindowEvent ev ) {
				ctrl.onCloseButtonPressed();
			}
		});
	}
	
	
	
	private static void linkPanel( EditorController ctrl, EditorPanel panel ) {
		Evaluator<List<Graphic>> eval = ctrl.getViewableGraphicsEvaluator();
		Camera                   cam  = ctrl.getCamera();
		
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
		linkAction( ctrl.getUndoAction(), toolbar.buttUndo, menubar.itemEditUndo );
		linkAction( ctrl.getRedoAction(), toolbar.buttRedo, menubar.itemEditRedo );
		
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




