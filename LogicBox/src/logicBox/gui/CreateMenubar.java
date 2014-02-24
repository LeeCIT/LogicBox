package logicBox.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import prototypes.printingProto.Printing;
import logicBox.core.CreateGUI;
import logicBox.fileManager.FileOpen;
import logicBox.fileManager.SaveFile;
import logicBox.gui.menubar.CoolMenuBar;
import logicBox.gui.menubar.CoolMenuItem;
import logicBox.util.Callback;


/**
 * Creates the menubar seen at the top of page
 * @author John
 *
 */
public class CreateMenubar extends CoolMenuBar{
	public CreateMenubar() {
		// File
		CoolMenuItem fileH	= new CoolMenuItem(null,        "File",null,         '0', null, false);
		CoolMenuItem save   = new CoolMenuItem("Save", 		 null, null,         's', null, false);
		CoolMenuItem saveAs = new CoolMenuItem("Save As...", null, null,         '0', null, false);
		CoolMenuItem sep    = new CoolMenuItem(null, 		 null, null,         '0', null, true);
		CoolMenuItem open   = new CoolMenuItem("Open", 		 null, openFile(),   'o', null, false);
		CoolMenuItem print  = new CoolMenuItem("Print",		 null, print(),      'p', null, false);
		CoolMenuItem exit   = new CoolMenuItem("Exit",		 null, exitProgram(),'e', null, false);
		
		// LogicBox
		CoolMenuItem logicBoxH  = new CoolMenuItem(null,         "LogicBox", null, '0', null, false);
		CoolMenuItem newAccount = new CoolMenuItem("New Account", null,      null, '0', null, false);
		CoolMenuItem logIn      = new CoolMenuItem("Log In",      null,      null, 'i', null, false);
		CoolMenuItem logout     = new CoolMenuItem("Log Out",     null,      null, 'u', null, false);

		// File add
		List<CoolMenuItem> fileList = new ArrayList<>();
		fileList.add(fileH);
		fileList.add(save);
		fileList.add(saveAs);
		fileList.add(sep);
		fileList.add(open);
		fileList.add(sep);
		fileList.add(print);
		fileList.add(sep);
		fileList.add(exit);
		
		// Logicbox add
		List<CoolMenuItem> logicBoxList = new ArrayList<>();
		logicBoxList.add(logicBoxH);
		logicBoxList.add(newAccount);
		logicBoxList.add(logIn);
		logicBoxList.add(logout);
		
		// Default if enabled or not
		logout.setEnabled(false);
		
		this.addHeadingAndSubHeadings(fileList);
		this.addHeadingAndSubHeadings(logicBoxList);
	}
	
	
	
	// Simply exit the program
	protected Callback exitProgram() {
		Callback callback = new Callback() {
			public void execute() {
				System.exit(0);				
			}
		};
		return callback;
	}
	
	
	// Opens the print dialog. TODO Needs to be sorted out
	protected Callback print() {
		Callback callback = new Callback() {
			public void execute() {
				new Printing().setUpPrintJob(CreateGUI.currentInstance.getEditorPane());;				
			}
		};
		return callback;
	}
	
	
	
	// Open the file open dialog
	protected Callback openFile() {
		Callback callback = new Callback() {
			public void execute() {
				FileOpen fileopen = new FileOpen(CreateGUI.currentInstance);
				File file = fileopen.getPickedFile(); //TODO do something with the file
			}
		};
		return callback;
	}
	
	
	
	// Open the save dialog
	protected Callback saveAs() {
		Callback callback = new Callback() {
			public void execute() {
				new SaveFile(CreateGUI.currentInstance); // TODO actually save something				
			}
		};
		return saveAs();
	}
}
