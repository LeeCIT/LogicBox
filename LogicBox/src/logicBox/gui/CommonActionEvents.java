
package logicBox.gui;

import java.io.File;

import logicBox.fileManager.FileOpen;
import logicBox.gui.printing.Printing;

public class CommonActionEvents {
	
	
	/**
	 * When a file is opened this is called and the JFile chooser is brought up
	 * @return	The file that was picked
	 */
	public static File openFileAction() {
		FileOpen file = new FileOpen( GUI.getMainFrame() );
		return file.getPickedFile();
	}
	
	
	
	
	/**
	 * When the print function is called
	 */
	public static void printAction() {
		Printing print = new Printing();
		print.setUpPrintJob( GUI.getMainFrame() );
	}
	

}
