
package logicBox.gui;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import logicBox.fileManager.FileOpen;
import logicBox.gui.editor.EditorPanel;
import logicBox.gui.edtior.printing.EditorPrinter;
import logicBox.gui.help.HelpFrame;


/**
 * Stores the keyboard shortcuts
 * @author John
 *
 */
public class KeyboardShortcuts {
	
	private EditorPanel panel;
	
	public KeyboardShortcuts(EditorPanel editorPanel) {
		this.panel = editorPanel;
		
		setUpHelpFrameAction();
		setUpPrinting();
		setUpOpenFile();
		setUpGridToggle();
	}
	
	
	
	/**
	 * Toggle the grid on and off
	 */
	private void setUpGridToggle() {
		String grid = "grid";
		String toggleGrid = "control G";
		
		panel.getInputMap().put( KeyStroke.getKeyStroke( toggleGrid ), grid );
		panel.getActionMap().put(grid, new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				EditorPanel panel = GUI.getMainFrame().getEditorPanel();
				panel.setGridEnabled( ! panel.getGridEnabled() );
			}});
	}




	/**
	 * Open file shortCut
	 */
	private void setUpOpenFile() {
		String open = "oepn";
		String openCommand = "control O";
		
		panel.getInputMap().put( KeyStroke.getKeyStroke( openCommand ), open );
		panel.getActionMap().put(open, new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				FileOpen fileOpen = new FileOpen( GUI.getMainFrame() );
				File     file     = fileOpen.getPickedFile();
				
				if (file != null)
					GUI.getMainFrame().getEditorPanel().getWorld().loadCircuit( file );
			}
		});	
	}



	/**
	 * Printing shortcut
	 */
	private void setUpPrinting() {
		String print = "print";
		String printCommand = "control P";
		
		panel.getInputMap().put( KeyStroke.getKeyStroke( printCommand ), print );
		panel.getActionMap().put(print, new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				new EditorPrinter( GUI.getMainFrame() );
			}
		});	
	}



	/**
	 * Sets up the keyboard shortcut for help
	 */
	private void setUpHelpFrameAction() {
		String help = "displayHelp";
		
		panel.getInputMap().put( KeyStroke.getKeyStroke("F1" ), help);
		panel.getActionMap().put(help, new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				new HelpFrame();
			}
		});	
	}
	
	
	
	
}
