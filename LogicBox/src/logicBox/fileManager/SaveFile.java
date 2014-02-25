
package logicBox.fileManager;

import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import logicBox.gui.GUI;



public class SaveFile 
{
	/**
	 * Saves chooses a location to save the program state
	 * TODO Must save
	 * @param frame
	 */
	public SaveFile(JFrame frame) {		
		JFileChooser chooser = new JFileChooser();

		FileNameExtensionFilter filter = new FileNameExtensionFilter("Logicbox", "lbx");
		chooser.setFileFilter(filter);

		int returnVal = chooser.showSaveDialog(frame);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				FileWriter fw = new FileWriter(chooser.getSelectedFile() + ".lbx");
				//fw.write(); TODO receive input to print something out to the file
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}



	
	
	
	
	
	
	
	
	



	
	
	
	
	
	
	
	public static void main(String[] args) {
		GUI.setNativeStyle();
		final JFrame frame = new JFrame("Save file demo");
		JButton saveFile   = new JButton("Save file");


		saveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new SaveFile(frame);
			}
		});


		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add("Center", saveFile);
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
}
