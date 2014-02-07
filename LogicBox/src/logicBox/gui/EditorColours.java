


package logicBox.gui;
import java.awt.Color;



/**
 * Defines the colour palette used by the editor.
 * @author Lee Coakley
 */
public class EditorColours
{
	public static Color background      = new Color(  55,  37,  37 );
	public static Color grid            = new Color(  89,  51,  72 );
	public static Color traceOff        = new Color(  58, 122,  54 );
	public static Color traceOn         = new Color( 146, 248, 122 );
	public static Color junctionOff     = new Color( 208, 135,   8 );
	public static Color junctionOn      = new Color( 255, 227,  39 );
	public static Color componentStroke = new Color( 243,  75,  99 );
	public static Color componentFill   = new Color( 112,  18,  58 );
	
	
	
	
	public static Color makeHighlight( Color col ) {
		int highlight = 64;
		
		return new Color(
			Math.min( col.getRed  () + highlight, 255 ),
			Math.min( col.getGreen() + highlight, 255 ),
			Math.min( col.getBlue () + highlight, 255 )
		);
	}
}
