package prototypes.fileOpen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import logicBox.gui.GuiUtil;

public class FileOpen {
	
	private File loadFile;
	
	/**
	 * Open the file open dialog
	 * @param frame	The frame that the dialog is to come from
	 */
	public FileOpen(JFrame frame) {
		JFileChooser chooser = new JFileChooser();
		
		// File filter
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Logicbox", "lbx");
		chooser.setFileFilter(filter);
				
		int returnVal = chooser.showOpenDialog(frame);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			System.out.println("You chose to open this file: " +
					chooser.getSelectedFile().getName());
			loadFile = chooser.getSelectedFile();
		}
	}
	
	
	/**
	 * Get the file picked
	 * @return
	 */
	public File getPickedFile() {
		return loadFile;
	}


























	public static void main(String[] args) {
		
		GuiUtil.setNativeLookAndFeel();
		
		final JFrame frame = new JFrame("Open file demo");

		JButton openFile = new JButton("Open file");


		openFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new FileOpen(frame);
			}
		});


		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add("Center", openFile);
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
}
