package logicBox.gui;

import java.util.ArrayList;
import java.util.List;

import prototypes.printingProto.Printing;
import logicBox.core.CreateGUI;
import logicBox.gui.menubar.CoolMenuBar;
import logicBox.gui.menubar.CoolMenuItem;
import logicBox.util.Callback;


/**
 * Creates the menubar seen at the top of page
 * @author John
 *
 */
public class MenubarCreate extends CoolMenuBar{
	public MenubarCreate() {
		// File
		CoolMenuItem fileH	= new CoolMenuItem(null,        "File",null,         '0', null, false);
		CoolMenuItem save   = new CoolMenuItem("Save", 		 null, null,         's', null, false);
		CoolMenuItem saveAs = new CoolMenuItem("Save As...", null, null,         '0', null, false);
		CoolMenuItem sep    = new CoolMenuItem(null, 		 null, null,         '0', null, true);
		CoolMenuItem open   = new CoolMenuItem("Open", 		 null, null,         'o', null, false);
		CoolMenuItem print  = new CoolMenuItem("Print",		 null, print(),      'p', null, false);
		CoolMenuItem exit   = new CoolMenuItem("Exit",		 null, exitProgram(),'e', null, false);

		
		List<CoolMenuItem> list = new ArrayList<>();
		list.add(fileH);
		list.add(save);
		list.add(saveAs);
		list.add(sep);
		list.add(open);
		list.add(sep);
		list.add(print);
		list.add(sep);
		list.add(exit);
		
		this.addHeadingAndSubHeadings(list);
	}
	
	
	
	
	protected Callback exitProgram() {
		Callback callback = new Callback() {
			public void execute() {
				System.exit(0);				
			}
		};
		return callback;
	}
	
	
	
	protected Callback print() {
		Callback callback = new Callback() {
			public void execute() {
				new Printing().setUpPrintJob(CreateGUI.currentInstance);;				
			}
		};
		return callback;
	}
}
