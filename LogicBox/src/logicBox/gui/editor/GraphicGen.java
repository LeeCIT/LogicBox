


package logicBox.gui.editor;

import java.util.ArrayList;
import java.util.List;
import logicBox.gui.VecPath;
import logicBox.sim.Component;
import logicBox.sim.PinIoMode;
import logicBox.util.Geo;
import logicBox.util.Region;
import logicBox.util.Vec2;



/**
 * Generates graphical representations for components and metadata that allows them to be 
 * interfaced with the simulation.
 * @author Lee Coakley
 */
public class GraphicGen
{
	private static final double baseSize        = 64;
	private static final double flatFrac        = 0.5;
	private static final double pinLenFrac      = 0.5;
	private static final double bubbleFrac      = 0.1;
	private static final int    pinGrowthThresh = 4;
	
	
	
	public static void generate( Component com ) {
		
	}
	
	
	
	private static Region getBaseRegion() {
		double half = baseSize * 0.5;
		return new Region( new Vec2(-half), new Vec2(half) );
	}
	
	
	
	// TODO: map pins by index top-down, position, IoMode
	public static GraphicComActive generateAndGate( int pinCount, boolean invert ) {
		final Region r            = getBaseRegion();
		final Vec2   size         = r.getSize();
		final double pinLength    = size.x * pinLenFrac;
		final double bubbleRadius = size.x * bubbleFrac;
		final double thickness    = EditorStyle.compThickness;
		
		if (pinCount > pinGrowthThresh)
			r.br.y += getPinSpacingGrowth() * (pinCount-pinGrowthThresh);
		
		final Vec2 bezRefTr = r.getTopRight();
		final Vec2 bezRefBr = r.getBottomRight();
		
		final Vec2 pinOutPos = r.getRightMiddle();
		final Vec2 pinOutEnd = new Vec2( pinOutPos.x + pinLength, pinOutPos.y );
		final Vec2 bubblePos = new Vec2( pinOutPos.x + bubbleRadius + thickness*0.55, pinOutPos.y );
		
		final Vec2 topLeft    = r.getTopLeft();
		final Vec2 topFlatEnd = Geo.lerp( topLeft,    bezRefTr,  flatFrac );
		final Vec2 topBezC1   = Geo.lerp( topFlatEnd, bezRefTr,  0.5      );
		final Vec2 topBezC2   = Geo.lerp( bezRefTr,   pinOutPos, 0.5      );
		
		final Vec2 botLeft    = r.getBottomLeft();
		final Vec2 botFlatEnd = Geo.lerp( botLeft,    bezRefBr,  flatFrac );
		final Vec2 botBezC1   = Geo.lerp( botFlatEnd, bezRefBr,  0.5      );
		final Vec2 botBezC2   = Geo.lerp( bezRefBr,   pinOutPos, 0.5      );
		
		final List<Vec2> pinPos = new ArrayList<>();
		pinPos.add( pinOutPos );
		pinPos.add( pinOutEnd );
		
		final List<Vec2> pinDistrib = distributePins( topLeft, botLeft, pinCount );
		for (Vec2 pos: pinDistrib) {
			pinPos.add( pos );
			pinPos.add( new Vec2(pos.x-pinLength, pos.y) );
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
		for (int i=0; i<pinPos.size(); i+=2) {
			polyPins.moveTo( pinPos.get(i)   );
			polyPins.lineTo( pinPos.get(i+1) );
		}
		
		final List<GraphicPinMapping> pinMappings = new ArrayList<>();
		pinMappings.add( new GraphicPinMapping( pinOutEnd, PinIoMode.output, 0 ) );
		
		for (int i=3; i<pinPos.size(); i+=2)
			pinMappings.add( new GraphicPinMapping( pinPos.get(i), PinIoMode.input, (i-2)/2 ) );
		
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














































