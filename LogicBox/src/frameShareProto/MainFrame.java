package frameShareProto;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import logicBox.gui.GUI;
import logicBox.gui.GuiUtil;

public class MainFrame extends JFrame{
	
	public static MainFrame currentInstance;
	
	
	
	public MainFrame() {
		

		GuiUtil.setNativeLookAndFeel();
		
		setupMouseListener();
		
		setTitle("Demo Main");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 700);
		setVisible(true);
		setLocationRelativeTo(null);//Loads the window in the centre		
	}
	
	
	
	private void setupMouseListener() {
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt){
				if (SwingUtilities.isLeftMouseButton(evt)) {
					//Work for left click goes here
					ToolBar.currentInstance.getSelectedToolBarItem();
					
				}
			}
		});
	}
}
