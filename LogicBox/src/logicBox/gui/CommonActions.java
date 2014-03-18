


package logicBox.gui;

import java.io.File;
import javax.swing.JFrame;

import logicBox.fileManager.FileOpen;
import logicBox.gui.printing.EditorPrinter;



public abstract class CommonActions
{
	/**
	 * When a file is opened this is called and the JFile chooser is brought up
	 * @return	The file that was picked
	 */
	public static File openFile( JFrame frame ) {
		FileOpen file = new FileOpen( GUI.getMainFrame() );
		return file.getPickedFile();
	}
	
	
	
	/**
	 * When the print function is called
	 */
	public static void print() {
		EditorPrinter print = new EditorPrinter();
		print.setUpPrintJob( GUI.getMainFrame() );
	}
}
