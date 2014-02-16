
package prototypes.menuBar;

import java.awt.BorderLayout;
import java.awt.Menu;
import java.awt.MenuItem;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import logicBox.gui.GuiUtil;
import prototypes.snappingProto.SnappingPrototype;

/**
 * 
 * @author John
 *
 */
public class CoolMenuBar extends JMenuBar
{
	public CoolMenuBar(){
		super();
	}

	
	
	/**
	 * Pass in a list of CoolMenuItems with the first one being the heading you want at the top
	 * e.g "File"
	 * Then the subheadings e.g "Save"
	 * @param items
	 */
	public void addHeadingAndSubHeadings(List<CoolMenuItem> items) {
		JMenu menu = new JMenu();
		for (CoolMenuItem item : items) {
			if ( item.getHeading() != null ) {
				menu.setText(item.getHeading());
			}

			if ( item.isSeparator() ) {
				menu.addSeparator();
			}

			if ( !(item.getHeading() != null) && !(item.isSeparator()) ) {
				menu.add(item);
			}			
		}
		add(menu);
	}






















	public static void main(String[] args) {
		GuiUtil.setNativeLookAndFeel();
		JFrame   frame = new JFrame("Demo");					
		
		// Make first then add list with the headings and sub headings
		CoolMenuBar menuBar = new CoolMenuBar();
		
		// New way of adding
		CoolMenuItem fileH	= new CoolMenuItem(null,        "File",null, '0', null, false);
		CoolMenuItem save   = new CoolMenuItem("Save", 		 null, null, 's', null, false);
		CoolMenuItem saveAs = new CoolMenuItem("Save As...", null, null, '0', null, false);
		CoolMenuItem sep    = new CoolMenuItem(null, 		 null, null, '0', null, true);
		CoolMenuItem open   = new CoolMenuItem("Open", 		 null, null, 'o', null, false);

		List<CoolMenuItem> list = new ArrayList<>();
		list.add(fileH);
		list.add(save);
		list.add(saveAs);
		list.add(sep);
		list.add(open);


		// New way of adding
		CoolMenuItem viewH	= new CoolMenuItem(null,          "View",null, '0', null, false);
		CoolMenuItem dock   = new CoolMenuItem("DockToolbar", null, null, 's', null, false);
		CoolMenuItem demo   = new CoolMenuItem("demo",        null, null, '0', null, false);

		List<CoolMenuItem> viewList = new ArrayList<>();
		viewList.add(viewH);
		viewList.add(dock);
		viewList.add(demo);

		
		menuBar.addHeadingAndSubHeadings(list);
		menuBar.addHeadingAndSubHeadings(viewList);

		frame.add(menuBar, BorderLayout.NORTH);


		// Demo
		frame.addComponentListener(new SnappingPrototype());
		frame.setSize(600, 600);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
