
package prototypes.menuBar;

import java.awt.BorderLayout;

import javax.swing.*;

import logicBox.gui.GuiUtil;
import prototypes.snappingProto.SnappingPrototype;

public class CoolMenuBar extends JMenuBar
{
	
	public CoolMenuBar() {
		super();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		GuiUtil.setNativeLookAndFeel();
		JFrame frame  = new JFrame("Demo");		
		JMenuBar menu = new JMenuBar();		
		
		JMenu file = new JMenu("File");
		file.add(new JMenuItem("Save"));
		file.add(new JMenuItem("Save As..."));
		file.add(new JSeparator());
		file.add(new JMenuItem("Load..."));
		
		JMenu view = new JMenu("View");
		view.add(new JMenuItem("Dock toolbox"));
		
		JMenu edit = new JMenu("Edit");
		view.add(new JMenuItem("Colour scheme"));
		
		menu.add(file);
		menu.add(view);
		menu.add(edit);
		
		frame.add(menu, BorderLayout.NORTH);
		
		// Demo
		frame.addComponentListener(new SnappingPrototype());
		frame.setSize(600, 600);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
