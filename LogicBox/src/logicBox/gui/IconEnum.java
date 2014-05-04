


package logicBox.gui;



/**
 * Maps icons to their location.
 * Icons can be used in multiple places, this prevents errors and data duplication.
 * @author Lee Coakley
 */
public enum IconEnum
{
	newFile,
	openFile,
	saveFile,
	print,
	cancel,
	undo,
	redo,
	cut,
	copy,
	paste,
	selectAll,
	selectNone,
	selectInverse,
	selectBlack,
	grid,
	camera,
	toolbox,
	help,
	delete,
	editText,
	title,
	connect,
	disconnect
	;
	
	
	
	public String getFilename() {
		String suffix = "16px.png";
		
		switch (this) {
			case newFile      : return "New"            + suffix;
			case openFile     : return "Open"           + suffix;
			case saveFile     : return "Save"           + suffix;
			case print        : return "Print"          + suffix;
			case cancel       : return "Cancel"         + suffix;
			case undo         : return "Undo"           + suffix;
			case redo         : return "Redo"           + suffix;
			case cut          : return "Cut"            + suffix;
			case copy         : return "Copy"           + suffix;
			case paste        : return "Paste"          + suffix;
			case selectAll    : return "SelectAll"      + suffix;
			case selectNone   : return "SelectNone"     + suffix;
			case selectInverse: return "SelectInverse"  + suffix;
			case selectBlack  : return "SelectBlackBox" + suffix;
			case grid         : return "Grid"           + suffix;
			case camera       : return "CentreCam"      + suffix;
			case toolbox      : return "Toolbox"        + suffix;
			case help         : return "Help"           + suffix;
			case delete       : return "Delete"         + suffix;
			case editText     : return "EditText"       + suffix;
			case title        : return "Title"          + suffix;
			case connect      : return "Connect"        + suffix;
			case disconnect   : return "Disconnect"     + suffix;
			
			default:
				throw new RuntimeException( "No filename defined for IconEnum " + this + "." );
		}
	}
}

