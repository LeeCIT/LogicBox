
package logicBox.gui;

import java.io.File;
import logicBox.fileManager.FileOpen;

public class CommonActionEvents {
	
	
	
	public static File openFileAction() {
		FileOpen file = new FileOpen( GUI.getMainFrame() );
		return file.getPickedFile();
	}
	

}
