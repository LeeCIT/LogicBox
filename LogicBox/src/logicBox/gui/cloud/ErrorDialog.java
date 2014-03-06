package logicBox.gui.cloud;

import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ErrorDialog
{
	public static void showErrorList(JFrame f, ArrayList<String> errors, String title)
	{
		String message = "<html><ul>";
		
		for(String error : errors)
			message += "<li>" + error + "</li>";
		
		message += "</ul></html>";
		
		JOptionPane.showMessageDialog(f, message, title, JOptionPane.ERROR_MESSAGE);
	}
}
