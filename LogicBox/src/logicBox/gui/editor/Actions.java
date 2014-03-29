


package logicBox.gui.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import logicBox.fileManager.FileOpen;
import logicBox.gui.GUI;
import logicBox.gui.editor.toolbox.Toolbox;
import logicBox.gui.edtior.printing.EditorPrinter;
import logicBox.gui.help.HelpFrame;



/**
 * Contains actions common to shortcuts, buttons and menu items.
 * Things like opening files, printing and so on.
 * TODO this stuff should probably be in EditorController.
 * @author John Murphy
 * @author Lee Coakley
 */
public abstract class Actions
{
	public static ActionListener getNewAction( final EditorController ctrl ) {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				// TODO
			}
		};
	}
	
	
	
	/**
	 * When a file is opened this is called and the JFile chooser is brought up
	 */
	public static ActionListener getOpenAction( final EditorController ctrl ) {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				FileOpen fileOpen = new FileOpen( ctrl.getEditorFrame() );
				File     file     = fileOpen.getPickedFile();
				
				if (file != null)
					ctrl.getWorld().loadCircuit( file );
			}
		};
	}
	
	
	
	public static ActionListener getSaveAction( final EditorController ctrl ) {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				// TODO
			}
		};
	}
	
	
	
	public static ActionListener getSaveAsAction( final EditorController ctrl ) {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				// TODO
			}
		};
	}
	
	
	
	/**
	 * When the print function is called
	 */
	public static ActionListener getPrintAction( final EditorController ctrl ) {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				new EditorPrinter( ctrl.getEditorFrame() );
			}
		};
	}
	
	
	
	public static ActionListener getGridToggleAction( final EditorController ctrl ) {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				EditorPanel panel = ctrl.getEditorPanel();
				panel.setGridEnabled( ! panel.getGridEnabled() );
			}
		};
	}
	
	
	
	public static ActionListener getRecentreCameraAction( final EditorController ctrl ) {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				ctrl.recentreCamera();
			}
		};
	}
	
	
	
	public static ActionListener getToolboxToggleAction( final EditorController ctrl ) {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				Toolbox toolbox = Toolbox.getInstance();
				
				if (toolbox == null) {
					toolbox = new Toolbox( GUI.getMainFrame() );
					toolbox.setActiveToolManager( ctrl.getToolManager() );
				} else {
					toolbox.dispose();
				}
			}
		};
	}
	
	
	
	public static ActionListener getHelpAction( final EditorController ctrl ) {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				new HelpFrame(); // TODO this is temporary, change it later
			}
		};
	}
	
	
	
	public static ActionListener getUndoAction( final EditorController ctrl ) {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				historyAction( ctrl, true );
			}
		};
	}
	
	
	
	public static ActionListener getRedoAction( final EditorController ctrl ) {
		return new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				historyAction( ctrl, false );
			}
		};
	}
	
	
	
	private static void historyAction( EditorController ctrl, boolean undoing ) {
		EditorFrame                 frame   = ctrl.getEditorFrame();
		HistoryManager<EditorWorld> manager = ctrl.getHistoryManager();
		
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
}



















