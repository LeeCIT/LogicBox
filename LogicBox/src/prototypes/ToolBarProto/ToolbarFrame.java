package prototypes.ToolBarProto;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToolBar;

import logicBox.gui.GuiUtil;
import prototypes.snappingProto.SnappingPrototype;

public class ToolbarFrame extends JFrame{
	
	private JLabel horizontalSeperator;
	private JToolBar toolbar;
	
	
	
	
	
	
	
	public ToolbarFrame() {
		GuiUtil.setNativeLookAndFeel();
		
		new JLabel();
		toolbar = new JToolBar("Drag");
		toolbar.addComponentListener(new SnappingPrototype(this));

		
		toolbar.add(new JButton("DemoGate"));
		toolbar.add(new JButton("XORGate"));
		toolbar.addSeparator();
		toolbar.add(new JButton("Bulb"));
		
		add(toolbar);
		
		// Demo
		
		setSize(150, 300);
		setVisible(true);
		setLocationRelativeTo(null);
	}
}
