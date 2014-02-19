package logicBox.gui;

import java.util.ArrayList;
import java.util.List;

import logicBox.gui.menubar.CoolMenuBar;
import logicBox.gui.menubar.CoolMenuItem;


/**
 * Creates the menubar seen at the top of page
 * @author John
 *
 */
public class MenubarCreate extends CoolMenuBar{
	public MenubarCreate() {
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
		
		this.addHeadingAndSubHeadings(list);
	}
}
