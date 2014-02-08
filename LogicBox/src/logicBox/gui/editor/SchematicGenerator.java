


package logicBox.gui.editor;

import java.util.ArrayList;
import java.util.List;
import logicBox.gui.VecPath;
import logicBox.util.Geo;
import logicBox.util.Region;
import logicBox.util.Vec2;



/**
 * Generates graphical representation for components.
 * @author Lee Coakley
 */
public class SchematicGenerator
{
	private static final double flatFrac   = 0.5;
	private static final double pinLenFrac = 0.5;
	private static final double bubbleFrac = 0.1;
	
	
	
	private static Region getBaseRegion() {
		return new Region( new Vec2(-32), new Vec2(32) );
	}
	
	
	
	public static GateGraphic generateNandGate( int pinCount ) {
		final Region r            = getBaseRegion();
		final double pinLength    = r.getSize().x * pinLenFrac;
		final double bubbleRadius = r.getSize().x * bubbleFrac;
		final double thickness    = EditorStyle.compThickness;
		
		if (pinCount > 4)
			r.br.y += r.getSize().y * 0.125 * (pinCount-4);
		
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
		
		List<Vec2> pinPos = new ArrayList<>();
		pinPos.add( pinOutPos );
		pinPos.add( pinOutEnd );
		
		List<Vec2> pinDistrib = distributePins( topLeft, botLeft, pinCount );
		for (Vec2 pos: pinDistrib) {
			pinPos.add( pos                              );
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
		
		List<Vec2> pinConnects = new ArrayList<>();
		pinConnects.add( pinOutEnd );
		for (int i=1; i<pinPos.size(); i+=2)
			pinConnects.add( pinPos.get(i) );
		
		GateGraphic gate = new GateGraphic( polyBody, polyPins, pinConnects );
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
}














































