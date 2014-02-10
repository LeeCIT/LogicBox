package prototypes.ToolBarProto;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
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
		
		//toolbar.setLayout(new MigLayout());
		
		
		
		
		
		
		toolbar.addComponentListener(new SnappingPrototype());		
		
		// Fix to a terrible flaw in JToolbar
		toolbar.addHierarchyListener(new HierarchyListener() {			
			public void hierarchyChanged(HierarchyEvent e) {
					JToolBar bar = (JToolBar) e.getComponent();
					final Window topLevel = SwingUtilities.windowForComponent(bar);
				
				if (topLevel instanceof JDialog) {
					((JDialog) topLevel).addComponentListener(new SnappingPrototype(frame));
				}    
			}
		});

		
		
		add(toolbar);
		
		
		// Demo
		addComponentListener(new SnappingPrototype());
		setSize(150, 300);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
	}
}
