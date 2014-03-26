
package logicBox.gui;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
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
		
		
	}
	
	
	
	/**
	 * Printing shortcut
	 */
	private void setUpPrinting() {
		String print = "print";
		
		panel.getInputMap().put( KeyStroke.getKeyStroke( "control P" ), print );
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
		
		panel.getInputMap().put(KeyStroke.getKeyStroke("F1"), help);
		panel.getActionMap().put(help, new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				new HelpFrame();
			}
		});	
	}
}
