


package logicBox.gui.editor.toolbox;

import logicBox.gui.editor.GraphicComActive;
import logicBox.util.Util;



/**
 * A button specialised for use in the Toolbox class.
 * @author Lee Coakley
 */
public class ToolboxButtonCom extends ToolboxButtonGraphic
{
	public ToolboxButtonCom( GraphicComActive graphic, String tooltip ) {
		super( makeGraphic(graphic), tooltip );
	}
	
	
	
	protected static GraphicComActive makeGraphic( GraphicComActive graphic ) {
		GraphicComActive gca = Util.deepCopy( graphic );
		gca.setInverted( true );
		gca.setFillAntialias( true );
		return gca;
	}
}























