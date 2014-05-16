


package logicBox.gui.editor;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;



/**
 * Handles the interface for saving/loading circuit files.
 * Remembers last location the user opened/saved from.
 * @author John Murphy
 * @author Lee Coakley
 */
public class FileManager 
{
	public static final String fileExtension = ".lbx";
	
	private static String lastDir;
	
	private JFrame       frame;
	private JFileChooser chooser;
	private File         file;
	
	
	
	/**
	 * Open the file open dialog
	 * @param frame	The frame that the dialog is to come from
	 */
	public FileManager( JFrame frame ) {
		this.frame   = frame;
		this.chooser = new JFileChooser( lastDir );
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter( "LogicBox circuit", "lbx" );
		chooser.setFileFilter( filter );
	}
	
	
	
	/**
	 * Show the file open dialogue.
	 * @return File chosen, or null if cancelled.
	 */
	public File openFile() {
		return choose( false );
	}
	
	
	
	/**
	 * Show the file save dialogue.
	 * You have to check for file existence manually!
	 * @return File chosen, or null if cancelled.
	 */
	public File saveFile() {
		return choose( true );
	}
	
	
	
	private File choose( boolean saving ) {
		int choice;
		
		if (saving)
			 choice = chooser.showSaveDialog( frame );
		else choice = chooser.showOpenDialog( frame );
		
		if (choice == JFileChooser.APPROVE_OPTION) {
			file    = chooser.getSelectedFile();
			lastDir = file.getParent();
			return file;
		}
		
		return null;
	}
	
	
	
	public static boolean hasFileExtension( String path ) {
		return path.toLowerCase().endsWith( FileManager.fileExtension.toLowerCase() );
	}
}





















