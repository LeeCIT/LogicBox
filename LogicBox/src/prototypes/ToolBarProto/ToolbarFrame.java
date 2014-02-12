package prototypes.ToolBarProto;

import java.awt.Component;
import java.awt.Window;
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

		ToolboxItemStore andGate = new ToolboxItemStore(new JButton("And Gate"), null, null);
		ToolboxItemStore orGate  = new ToolboxItemStore(new JButton("Or Gate"), null, null);

		
//		List<ToolboxItem> toolbarList = new ArrayList<ToolboxItem>();
//		toolbarList.add(andGate);
//		toolbarList.add(orGate);
		
		ToolBox tool = new ToolBox();
//		tool.addSeparator();
//		tool.add(new JLabel("Gates"));
//		tool.addSeparator();
//		tool.addToolBoxItemList(toolbarList);
//		tool.addSeparator();
		
		
		// Panel test
		ToolboxButtonCallback andGatea = new ToolboxButtonCallback(new JButton("And gate"), null);
		ToolboxButtonCallback orGatea  = new ToolboxButtonCallback(new JButton("Or gate"),  null);
		ToolboxButtonCallback demo     = new ToolboxButtonCallback(new JButton("Demo"),  null);
		ToolboxButtonCallback nub      = new ToolboxButtonCallback(new JButton("nub"),  null);


		
		
		List<ToolboxButtonCallback> buttons = new ArrayList<ToolboxButtonCallback>();
		buttons.add(andGatea);
		buttons.add(orGatea);
		buttons.add(demo);
		buttons.add(nub);
				
		ToolboxPanel pan = new ToolboxPanel(buttons);
		ToolboxUtil.addCategory(tool, "Gates");
		
		tool.add(pan);	
		
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



		

		// Demo
		addComponentListener(new SnappingPrototype());
		setSize(150, 300);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
	}
}
