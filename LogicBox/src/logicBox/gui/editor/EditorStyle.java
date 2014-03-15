


package logicBox.gui.editor;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import logicBox.util.Geo;



/**
 * Defines the colour palette and line styles used by the editor.
 * @author Lee Coakley
 */
public abstract class EditorStyle
{
	public static Color colBackground      = new Color(  55,  37,  37 );
	public static Color colGrid            = new Color(  89,  51,  72 );
	public static Color colTraceOff        = new Color(  58, 122,  54 );
	public static Color colTraceOn         = new Color( 146, 248, 122 );
	public static Color colJunctionOff     = new Color( 208, 135,   8 );
	public static Color colJunctionOn      = new Color( 255, 227,  39 );
	public static Color colComponentStroke = new Color( 243,  75,  99 );
	public static Color colComponentFill   = new Color( 112,  18,  58 );
	public static Color colHighlightStroke = new Color( 245, 238, 207 );
	public static Color colHighlightFill   = new Color( 183, 117, 126 );
	public static Color colSelectionStroke = new Color(  78, 185, 252 );
	public static Color colSelectionFill   = new Color(   0,  95, 192 );
	
	public static double gridThickness = 3.0;
	public static float  compThickness = 5.0f;
	
	public static Stroke strokeBody       = new BasicStroke( compThickness );
	public static Stroke strokePin        = new BasicStroke( compThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND );
	public static Stroke strokeTrace      = new BasicStroke( compThickness, BasicStroke.CAP_BUTT,  BasicStroke.JOIN_ROUND );
	public static Stroke strokeTracePlace = makeTracePlaceStroke();
	public static Stroke strokeBubble     = new BasicStroke( compThickness * 0.5f );
	
	
	
	public static Stroke makeSelectionStroke( float thickness ) {
		float[] dashPattern = { thickness };
		
		return new BasicStroke(
			thickness * 0.5f,
			BasicStroke.CAP_ROUND,
			BasicStroke.JOIN_ROUND,
			1.0f,
			dashPattern,
			0
		);
	}
	
	
	
	private static Stroke makeTracePlaceStroke() {
		float[] dashPattern = { compThickness * 2 };
		
		return new BasicStroke(
			compThickness,
			BasicStroke.CAP_ROUND,
			BasicStroke.JOIN_ROUND,
			1.0f,
			dashPattern,
			0
		);
	}
}
