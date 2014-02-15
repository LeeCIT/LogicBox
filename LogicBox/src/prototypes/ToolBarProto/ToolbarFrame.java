package prototypes.ToolBarProto;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.*;

import javax.swing.*;

import java.util.*;

import logicBox.gui.GuiUtil;
import logicBox.gui.editor.EditorPanel;
import prototypes.snappingProto.SnappingPrototype;

public class ToolbarFrame extends JFrame{
	private JFrame frame = this;


	public ToolbarFrame() {
		GuiUtil.setNativeLookAndFeel();
		
		
		Toolbox tool = new Toolbox("LogicBox Toolbox", JToolBar.VERTICAL);
		
		// Panel test
		ToolboxButtonCallback andGate  = new ToolboxButtonCallback(new JButton("And gate"), null);
		ToolboxButtonCallback orGate   = new ToolboxButtonCallback(new JButton("Or gate"),  null);
		ToolboxButtonCallback demo     = new ToolboxButtonCallback(new JButton("Demo"),  null);
		ToolboxButtonCallback nub      = new ToolboxButtonCallback(new JButton("nub"),  null);
		
		List<ToolboxButtonCallback> buttons = new ArrayList<ToolboxButtonCallback>();
		buttons.add(andGate);
		buttons.add(orGate);
		buttons.add(demo);
		buttons.add(nub);
				
		ToolboxButtonCallback bulb  = new ToolboxButtonCallback(new JButton("Bulb"), null);
		ToolboxButtonCallback led   = new ToolboxButtonCallback(new JButton("Led"),  null);
		ToolboxButtonCallback multi = new ToolboxButtonCallback(new JButton("Multi"),  null);
		ToolboxButtonCallback nubs  = new ToolboxButtonCallback(new JButton("nubs"),  null);
		
		List<ToolboxButtonCallback> other = new ArrayList<ToolboxButtonCallback>();
		other.add(bulb);
		other.add(led);
		other.add(multi);
		other.add(nubs);
		
		// Add buttons to the panel so they display correctly. in lines of 3
		tool.addCategory("Gates");
		tool.addListOfButtons(buttons);
		tool.addCategory("Others");
		tool.addListOfButtons(other);
		
		JPanel editPan = new EditorPanel();
				
		tool.addSnapping(frame, 10);
		
		//ToolboxUtil.preventToolBoxhorizontalOrientation(tool);
		tool.setMargin(new Insets(0, 2, 0, 0));
		add(editPan, BorderLayout.CENTER);
		add(tool, BorderLayout.WEST);

		// Demo
		addComponentListener(new SnappingPrototype());
		setSize(600, 600);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
}
