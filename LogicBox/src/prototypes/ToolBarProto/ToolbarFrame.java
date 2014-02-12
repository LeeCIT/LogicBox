package prototypes.ToolBarProto;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;
import logicBox.gui.GuiUtil;
import prototypes.snappingProto.SnappingPrototype;

public class ToolbarFrame extends JFrame{

	private final JToolBar toolbar;
	private JFrame frame = this;


	public ToolbarFrame() {
		GuiUtil.setNativeLookAndFeel();

		toolbar = new JToolBar("LogicBox Tool bar", JToolBar.VERTICAL);

		toolbar.add(new JButton("DemoGate"));
		toolbar.add(new JButton("XORGate"));
		toolbar.addSeparator();
		toolbar.add(new JButton("Bulb"));

		ToolboxItemData andGate = new ToolboxItemData(new JButton("And Gate"), null, null);
		ToolboxItemData orGate  = new ToolboxItemData(new JButton("Or Gate"), null, null);

		
		List<ToolboxItem> toolbarList = new ArrayList<ToolboxItem>();
		toolbarList.add(andGate);
		toolbarList.add(orGate);
		
		ToolBox tool = new ToolBox();
		tool.addToolBoxItem(toolbarList);
		
		
		
		
		add(tool);

	

		// Fix to a terrible flaw in JToolbar
		tool.addHierarchyListener(new HierarchyListener() {			
			public void hierarchyChanged(HierarchyEvent e) {
				JToolBar bar = (JToolBar) e.getComponent();
				final Window topLevel = SwingUtilities.windowForComponent(bar);

				if (topLevel instanceof JDialog) {
					((JDialog) topLevel).addComponentListener(new SnappingPrototype(frame));
				}    
			}
		});



		//add(toolbar);


		// Demo
		addComponentListener(new SnappingPrototype());
		setSize(150, 300);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
	}
}
