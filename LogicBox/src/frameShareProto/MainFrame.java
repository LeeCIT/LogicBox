package frameShareProto;

import javax.swing.JFrame;

import logicBox.gui.GUI;
import logicBox.gui.GuiUtil;

public class MainFrame extends JFrame{
	
	public static MainFrame currentInstance;
	
	
	
	public MainFrame() {
		

		GuiUtil.setNativeLookAndFeel();
		
		
		setTitle("Demo Main");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 700);
		setVisible(true);
		setLocationRelativeTo(null);//Loads the window in the center		
	}
	

}
