package prototypes.saveFile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;
import logicBox.gui.GuiUtil;


public class SaveFile {

	private String demoText   = "demo output";

	public SaveFile(JFrame frame, File fileTosave) {
		File saveFile;
		
		JFileChooser chooser = new JFileChooser();

		FileNameExtensionFilter filter = new FileNameExtensionFilter("Logicbox", "lbx");
		chooser.setFileFilter(filter);

		int returnVal = chooser.showSaveDialog(frame);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			System.out.println("You chose to save this file: " + chooser.getSelectedFile().getName());
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

		GuiUtil.setNativeLookAndFeel();
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
