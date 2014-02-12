package prototypes.ToolBarProto;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.*;

import javax.swing.*;

import java.util.*;

import logicBox.gui.GuiUtil;
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
		ToolboxPanel pan = new ToolboxPanel(buttons);
		tool.addCategory("Gates");
		tool.add(pan);
		tool.addCategory("Others");
		ToolboxPanel pans = new ToolboxPanel(other);
		tool.add(pans);
		
		
		add(tool);

		ToolboxUtil.addSnapping(tool, frame);

		// Demo
		addComponentListener(new SnappingPrototype());
		setSize(150, 300);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
	}
}
