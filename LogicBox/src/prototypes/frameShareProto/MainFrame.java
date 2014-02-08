package prototypes.frameShareProto;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import prototypes.frameShareProto.ToolSelectionEnum.ToolSelection;
import logicBox.gui.GuiUtil;

public class MainFrame extends JFrame{
	
	public static MainFrame currentInstance;
	private String toolSeletion = "Nothing here";
	int mouseXPos = 0;
	int mouseYPos = 0;
	
	
	
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
					//I know nasty things are going on here
					ToolSelection sel = ToolBar.selection;
					toolSeletion = GetToolBarSelectionAsString.getToolbarselectionAsString(sel);
					mouseXPos = evt.getX();
					mouseYPos = evt.getY();
					repaint();
				}
			}
		});
	}
		
	
	public void paint(Graphics g) {
		g.drawString(toolSeletion, mouseXPos, mouseYPos);
	}
}
