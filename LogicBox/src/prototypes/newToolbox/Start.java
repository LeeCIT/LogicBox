
package prototypes.newToolbox;

import javax.swing.JFrame;


public class Start 
{
	
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		
		CoolToolbox tool = new CoolToolbox(frame);
				
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

}
