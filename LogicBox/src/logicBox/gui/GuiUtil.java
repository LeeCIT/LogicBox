package logicBox.gui;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import prototypes.frameShareProto.MainFrame;



/**
 * 
 * @author john
 * version 1
 */
public class GuiUtil {
	
	public static void setNativeLookAndFeel()
	{
		try 
		{
	        // Set System L&F
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
	    } 
		catch (Exception e) {
				e.printStackTrace();		
		}
	}
	
	
	
	/**
	 * Show a modal error dialogue.
	 * @param title
	 * @param message
	 */
	public static void showError(String title, String message) {
		JOptionPane.showMessageDialog(MainFrame.currentInstance, message, title, JOptionPane.ERROR_MESSAGE);
	}
	
	
	/**
	 * Show a modal helpful message
	 * @param title
	 * @param message
	 */
	public static void showHelpMessage(String title, String message) {
		JOptionPane.showMessageDialog(MainFrame.currentInstance, message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	
}