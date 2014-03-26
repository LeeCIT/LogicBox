package logicBox.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import logicBox.gui.editor.EditorPanel;
import logicBox.gui.help.HelpFrame;


public class KeyboardShortcuts {
	
	private EditorPanel panel;
	
	public KeyboardShortcuts(EditorPanel editorPanel) {
		this.panel = editorPanel;
		
		setUpHelpFrameAction();
		
		
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
