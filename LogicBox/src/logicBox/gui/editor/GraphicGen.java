


package logicBox.gui.editor;

import java.util.ArrayList;
import java.util.List;
import logicBox.gui.VecPath;
import logicBox.sim.PinIoMode;
import logicBox.util.Geo;
import logicBox.util.Line2;
import logicBox.util.Region;
import logicBox.util.Vec2;



/**
 * Generates graphical representations for components along with metadata
 * which allows them to be interfaced with the simulation.
 * @author Lee Coakley
 */
public class GraphicGen
{
	private static final double baseSize        = 64;
	private static final double flatFrac        = 0.5;
	private static final double pinLenFrac      = 0.5;
	private static final double bubbleFrac      = 0.1;
	private static final int    pinGrowthThresh = 4;
	
	
	
	private static Region getBaseRegion() {
		double half = baseSize * 0.5;
		return new Region( new Vec2(-half), new Vec2(half) );
	}
	
	
	
	public static GraphicComActive generateAndGate( int pinCount, boolean invert ) {
		final Region r            = getBaseRegion();
		final Vec2   size         = r.getSize();
		final double pinLength    = size.x * pinLenFrac;
		final double bubbleRadius = size.x * bubbleFrac;
		final double thickness    = EditorStyle.compThickness;
		
		if (pinCount > pinGrowthThresh)
			r.br.y += getPinSpacingGrowth() * (pinCount - pinGrowthThresh);
		
		final Vec2 bezRefTr = r.getTopRight();
		final Vec2 bezRefBr = r.getBottomRight();
		
		final Vec2  pinOutPos = r.getRightMiddle();
		final Vec2  pinOutEnd = new Vec2( pinOutPos.x + pinLength, pinOutPos.y );
		final Line2 pinOut    = new Line2( pinOutPos, pinOutEnd );
		final Vec2  bubblePos = new Vec2( pinOutPos.x + bubbleRadius + thickness*0.55, pinOutPos.y );
		
		final Vec2 topLeft    = r.getTopLeft();
		final Vec2 topFlatEnd = Geo.lerp( topLeft,    bezRefTr,  flatFrac );
		final Vec2 topBezC1   = Geo.lerp( topFlatEnd, bezRefTr,  0.5      );
		final Vec2 topBezC2   = Geo.lerp( bezRefTr,   pinOutPos, 0.5      );
		
		final Vec2 botLeft    = r.getBottomLeft();
		final Vec2 botFlatEnd = Geo.lerp( botLeft,    bezRefBr,  flatFrac );
		final Vec2 botBezC1   = Geo.lerp( botFlatEnd, bezRefBr,  0.5      );
		final Vec2 botBezC2   = Geo.lerp( bezRefBr,   pinOutPos, 0.5      );
		
		final List<Line2> pins = new ArrayList<>();
		pins.add( pinOut );
		
		final List<Vec2> pinDistrib = distributePins( topLeft, botLeft, pinCount );
		for (Vec2 pos: pinDistrib) {
			Vec2  endPos = new Vec2( pos.x-pinLength, pos.y );
			Line2 line   = new Line2( pos, endPos );
			pins.add( line );
		}
		
		final VecPath polyBody = new VecPath();
		polyBody.moveTo ( topLeft );
		polyBody.lineTo ( topFlatEnd );
		polyBody.curveTo( topBezC1, topBezC2, pinOutPos );
		polyBody.curveTo( botBezC2, botBezC1, botFlatEnd );
		polyBody.lineTo ( botFlatEnd );
		polyBody.lineTo ( botLeft );
		polyBody.closePath();
		
		final VecPath polyPins = new VecPath();
		for (Line2 pin: pins) {
			polyPins.moveTo( pin.a );
			polyPins.lineTo( pin.b );
		}
		
		final List<GraphicPinMapping> pinMappings = new ArrayList<>();
		pinMappings.add( new GraphicPinMapping( pinOut, PinIoMode.output, 0 ) );
		
		int inOffset = 1;
		for (int i=inOffset; i<pins.size(); i++)
			pinMappings.add( new GraphicPinMapping( pins.get(i), PinIoMode.input, i-inOffset ) );
		
		GraphicComActive gate = new GraphicComActive( polyBody, polyPins, pinMappings );
		
		if (invert)
			gate.enableBubble( bubblePos, bubbleRadius );
		
		return gate;
	}
	
	
	
	private static List<Vec2> distributePins( Vec2 a, Vec2 b, int count ) {
		List<Vec2> list = new ArrayList<>();
		
		double dist   = Geo.distance( a, b );
		double step   = dist / count;
		double offset = step * 0.5;
		Vec2   cursor = new Vec2( a.x, a.y + offset );
		
		for (int i=0; i<count; i++) {
			list.add( cursor.copy() );
			cursor.y += step;
		}
		
		return list;
	}
	
	
	
	private static double getPinSpacingGrowth() {
		return getBaseRegion().getSize().y * 0.25;
	}
}














































