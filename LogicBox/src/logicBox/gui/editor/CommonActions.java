


package logicBox.gui.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import logicBox.fileManager.FileOpen;
import logicBox.gui.printing.EditorPrinter;



/**
 * Contains actions common to shortcuts, buttons and menu items.
 * Things like opening files, printing and so on.
 * @author John Murphy
 * @author Lee Coakley
 */
public abstract class CommonActions
{
	/**
	 * When a file is opened this is called and the JFile chooser is brought up
	 */
	public static void addOpenCircuitListener( AbstractButton abutt, final EditorFrame frame ) {
		abutt.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				FileOpen fileOpen = new FileOpen( frame );
				File     file     = fileOpen.getPickedFile();
				
				if (file != null)
					frame.getEditorPanel().getWorld().loadCircuit( file );
			}
		});
	}
	
	
	
	/**
	 * When the print function is called
	 */
	public static void addPrintCircuitListener( AbstractButton abutt, final EditorFrame frame ) {
		abutt.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				new EditorPrinter().setUpPrintJob( frame );;
			}
		});
	}
	
	
	
	public static void addRecentreCameraListener( AbstractButton abutt, final EditorFrame frame ) {
		abutt.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				frame.getEditorPanel().recentreCamera();
			}
		});
	}
	
	
	
	public static void addUndoListener( AbstractButton butt, final EditorFrame frame ) {
		butt.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				undoAction( frame );
			}
		});
	}
	
	
	
	public static void addRedoListener( AbstractButton butt, final EditorFrame frame ) {
		butt.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				redoAction( frame );
			}
		});
	}
	
	
	
	private static void undoAction( EditorFrame frame ) {
		historyAction( frame, true );
	}
	
	
	
	private static void redoAction( EditorFrame frame ) {
		historyAction( frame, false );
	}
	
	
	
	private static void historyAction( EditorFrame frame, boolean undoing ) {
		HistoryManager<EditorWorld> manager = frame.getEditorPanel().getHistoryManager();
		JMenuItem menuUndo = frame.getEditorMenuBar().itemEditUndo;
		JButton   buttUndo = frame.getEditorToolbar().undoButt;
		JMenuItem menuRedo = frame.getEditorMenuBar().itemEditRedo;
		JButton   buttRedo = frame.getEditorToolbar().redoButt;
		
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



















