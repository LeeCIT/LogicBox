


package logicBox.gui.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.AbstractButton;
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
}
