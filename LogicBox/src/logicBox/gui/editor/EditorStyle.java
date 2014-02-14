


package logicBox.gui.editor;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import logicBox.util.Geo;



/**
 * Defines the colour palette used by the editor.
 * @author Lee Coakley
 */
public class EditorStyle
{
	public static Color colBackground      = new Color(  55,  37,  37 );
	public static Color colGrid            = new Color(  89,  51,  72 );
	public static Color colTraceOff        = new Color(  58, 122,  54 );
	public static Color colTraceOn         = new Color( 146, 248, 122 );
	public static Color colJunctionOff     = new Color( 208, 135,   8 );
	public static Color colJunctionOn      = new Color( 255, 227,  39 );
	public static Color colComponentStroke = new Color( 243,  75,  99 );
	public static Color colComponentFill   = new Color( 112,  18,  58 );
	public static Color colSelection       = new Color( 78,  185, 252 );
	
	public static double gridThickness = 3.0;
	public static float  compThickness = 5.0f;
	
	public static Stroke strokeBody   = new BasicStroke( compThickness );
	public static Stroke strokePin    = new BasicStroke( compThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND );
	public static Stroke strokeTrace  = new BasicStroke( compThickness, BasicStroke.CAP_BUTT,  BasicStroke.JOIN_ROUND );
	public static Stroke strokeBubble = new BasicStroke( compThickness * 0.5f );
	
	private static double highlightFrac = 0.125;
	private static double selectFrac    = 0.5;
	
	
	
	public static Color makeHighlighted( Color col ) {
		return Geo.lerp( col, Color.white, highlightFrac ); // TODO make nicer
	}
	
	
	
	public static Color makeSelected( Color col ) {
		return Geo.lerp( col, colSelection, selectFrac ); // TODO make nicer
	}
}
