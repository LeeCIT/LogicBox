


package logicBox.gui.editor;

import java.io.File;
import logicBox.fileManager.FileOpen;
import logicBox.gui.GUI;
import logicBox.gui.printing.EditorPrinter;



/**
 * Contains actions common to shortcuts, buttons and menu items.
 * Things like opening files, printing and so on.
 * @author John Murphy
 */
public abstract class CommonActions
{
	/**
	 * When a file is opened this is called and the JFile chooser is brought up
	 * @return	The file that was picked
	 */
	public static File openFile( EditorFrame frame ) {
		FileOpen file = new FileOpen( GUI.getMainFrame() );
		return file.getPickedFile();
	}
	
	
	
	/**
	 * When the print function is called
	 */
	public static void print( EditorFrame frame ) {
		EditorPrinter print = new EditorPrinter();
		print.setUpPrintJob( GUI.getMainFrame() );
	}
}
