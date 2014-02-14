package prototypes.printingProto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;


public class DemoStart {
	public static void main(String args[]) {
		final JFrame frame = new JFrame("Printing Demo");

		JButton printButton = new JButton("Demo printing");
		
		
		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Printing print = new Printing();
				print.setUpPrintJob(frame);
			}
		});
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add("Center", printButton);
		frame.pack();
		frame.setVisible(true);
	}
}
