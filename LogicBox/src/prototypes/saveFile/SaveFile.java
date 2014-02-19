
package prototypes.saveFile;

import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import logicBox.gui.GUI;



public class SaveFile {

	private File   saveFile;
	private File   fileToSave;
	private String demoText   = "demo output";

	public SaveFile(JFrame frame, File fileTosave) {
		this.fileToSave = fileTosave;
		
		JFileChooser chooser = new JFileChooser();

		FileNameExtensionFilter filter = new FileNameExtensionFilter("Logicbox", "lbx");
		chooser.setFileFilter(filter);

		int returnVal = chooser.showSaveDialog(frame);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			System.out.println("You chose to save this file: " + chooser.getSelectedFile().getName());
			saveFile = chooser.getSelectedFile();
			try {
				FileWriter fw = new FileWriter(chooser.getSelectedFile() + ".lbx");
				fw.write(demoText);
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
				new SaveFile(frame, null);
			}
		});


		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add("Center", saveFile);
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
}
