


package logicBox.gui.editor;

import java.util.ArrayList;
import java.util.List;
import logicBox.gui.VecPath;
import logicBox.sim.PinIoMode;
import logicBox.util.BezierCubic2;
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
	public static GraphicComActive generateGateRelay() {
		return generateGateRelay( false );
	}
	
	
	
	public static GraphicComActive generateGateNot() {
		return generateGateRelay( true );
	}
	
	
	
	public static GraphicComActive generateGateAnd( int pinCount ) {
		return generateGateAnd( pinCount, false );
	}
	
	
	
	public static GraphicComActive generateGateNand( int pinCount ) {
		return generateGateAnd( pinCount, true );
	}
	
	
	
	public static GraphicComActive generateGateOr( int pinCount ) {
		return generateGateOr( pinCount, false, false );
	}
	
	
	
	public static GraphicComActive generateGateNor( int pinCount ) {
		return generateGateOr( pinCount, false, true );
	}
	
	
	
	public static GraphicComActive generateGateXor() {
		return generateGateOr( 2, true, false );
	}
	
	
	
	public static GraphicComActive generateGateXnor() {
		return generateGateOr( 2, true, true );
	}
	
	
	
	
	
	private static final double baseSize        = 64;
	private static final double pinLenFrac      = 0.5;
	private static final double bubbleFrac      = 0.1;
	private static final int    pinGrowthThresh = 4;
	
	
	
	private static GraphicComActive generateGateRelay( boolean invert ) {
		Region r            = getBaseRegion();
		Vec2   size         = r.getSize();
		double pinLength    = size.x * pinLenFrac;
		double bubbleRadius = size.x * bubbleFrac;
		double thickness    = EditorStyle.compThickness;
		
		Vec2 pinOutPos = r.getRightMiddle();
		Vec2 pinOutEnd = pinOutPos.add( pinLength, 0 );
		Vec2 bubblePos = pinOutPos.add( bubbleRadius + thickness*0.3, 0 );
		
		Vec2 pinInPos = r.getLeftMiddle();
		Vec2 pinInEnd = pinInPos.add( -pinLength, 0 );
		
		Vec2 topLeft = r.getTopLeft();
		Vec2 botLeft = r.getBottomLeft();
		
		VecPath polyBody = new VecPath();
		polyBody.moveTo( botLeft );
		polyBody.lineTo( pinOutPos );
		polyBody.lineTo( topLeft );
		polyBody.closePath();
		
		List<Line2> pinLines = new ArrayList<>();
		pinLines.add( new Line2(pinOutPos, pinOutEnd) );
		pinLines.add( new Line2(pinInPos,  pinInEnd ) );
		
		VecPath polyPins = new VecPath();
		for (Line2 pin: pinLines) {
			polyPins.moveTo( pin.a );
			polyPins.lineTo( pin.b );
		}
		
		GraphicComActive gate = new GraphicComActive(
			polyBody,
			polyPins,
			null,
			genPinMappings( pinLines, 1 )
		);
		
		if (invert)
			gate.enableBubble( bubblePos, bubbleRadius );
		
		return gate;
	}
	
	
	
	private static GraphicComActive generateGateAnd( int pinCount, boolean invert ) {
		Region r            = getBaseRegion();
		Vec2   size         = r.getSize();
		double pinLength    = size.x * pinLenFrac;
		double bubbleRadius = size.x * bubbleFrac;
		double thickness    = EditorStyle.compThickness;
		double flatFrac     = 0.5;
		
		applyPinGrowth( r, pinCount );
		
		Vec2 bezRefTr = r.getTopRight();
		Vec2 bezRefBr = r.getBottomRight();
		
		Vec2 pinOutPos = r.getRightMiddle();
		Vec2 pinOutEnd = pinOutPos.add( pinLength, 0 );
		Vec2 bubblePos = pinOutPos.add( bubbleRadius + thickness*0.55, 0 );
		
		Vec2 topLeft    = r.getTopLeft();
		Vec2 topFlatEnd = Geo.lerp( topLeft,    bezRefTr,  flatFrac );
		Vec2 topBezC1   = Geo.lerp( topFlatEnd, bezRefTr,  0.5      );
		Vec2 topBezC2   = Geo.lerp( bezRefTr,   pinOutPos, 0.5      );
		
		Vec2 botLeft    = r.getBottomLeft();
		Vec2 botFlatEnd = Geo.lerp( botLeft,    bezRefBr,  flatFrac );
		Vec2 botBezC1   = Geo.lerp( botFlatEnd, bezRefBr,  0.5      );
		Vec2 botBezC2   = Geo.lerp( bezRefBr,   pinOutPos, 0.5      );
		
		List<Line2> pinLines = new ArrayList<>();
		pinLines.add( new Line2(pinOutPos, pinOutEnd) );
		
		for (Vec2 pos: distributePins(topLeft, botLeft, pinCount)) {
			Vec2  endPos = pos.add( -pinLength, 0 );
			Line2 line   = new Line2( pos, endPos );
			pinLines.add( line );
		}
		
		VecPath polyBody = new VecPath();
		polyBody.moveTo ( topLeft );
		polyBody.lineTo ( topFlatEnd );
		polyBody.curveTo( topBezC1, topBezC2, pinOutPos );
		polyBody.curveTo( botBezC2, botBezC1, botFlatEnd );
		polyBody.lineTo ( botFlatEnd );
		polyBody.lineTo ( botLeft );
		polyBody.closePath();
		
		VecPath polyPins = new VecPath();
		for (Line2 pin: pinLines) {
			polyPins.moveTo( pin.a );
			polyPins.lineTo( pin.b );
		}
		
		GraphicComActive gate = new GraphicComActive(
			polyBody,
			polyPins,
			null,
			genPinMappings( pinLines, 1 )
		);
		
		if (invert)
			gate.enableBubble( bubblePos, bubbleRadius );
		
		return gate;
	}
	
	
	
	private static GraphicComActive generateGateOr( int pinCount, boolean isXor, boolean invert ) {
		Region r            = getBaseRegion();
		Vec2   size         = r.getSize();
		double pinLength    = size.x * pinLenFrac;
		double bubbleRadius = size.x * bubbleFrac;
		double thickness    = EditorStyle.compThickness;
		double flatFrac     = 1.0 / 8.0;
		
		applyPinGrowth( r, pinCount );
		
		Vec2 bezRefTr = r.getTopRight();
		Vec2 bezRefBr = r.getBottomRight();
		
		Vec2 pinOutPos = r.getRightMiddle();
		Vec2 pinOutEnd = pinOutPos.add( pinLength, 0 );
		Vec2 bubblePos = pinOutPos.add( bubbleRadius + thickness*0.35, 0 );
		
		Vec2 topLeft    = r.getTopLeft();
		Vec2 topFlatEnd = Geo.lerp( topLeft,    bezRefTr, flatFrac );
		Vec2 topBezC1   = Geo.lerp( topFlatEnd, bezRefTr, 0.5      );
		Vec2 topBezC2   = pinOutPos;
		
		Vec2 botLeft    = r.getBottomLeft();
		Vec2 botFlatEnd = Geo.lerp( botLeft,    bezRefBr, flatFrac );
		Vec2 botBezC1   = Geo.lerp( botFlatEnd, bezRefBr, 0.5      );
		Vec2 botBezC2   = pinOutPos;
		
		double rearConOffs = size.x * 0.33;
		Vec2   rearC1      = topLeft.add( rearConOffs,  rearConOffs );
		Vec2   rearC2      = botLeft.add( rearConOffs, -rearConOffs );
		
		List<Line2>  pinLines         = new ArrayList<>(); 
		double       pinOutExtentLeft = topLeft.x - pinLength;
		Line2        pinOutTerminal   = new Line2( pinOutExtentLeft, topLeft.y, pinOutExtentLeft, botLeft.y );
		BezierCubic2 pinOutContact    = new BezierCubic2( topLeft, rearC1, rearC2, botLeft );
		List<Line2>  pinOutLines      = genPinLines( pinOutTerminal, pinOutContact, new Vec2(1,0), pinCount, true );
		pinLines.add( new Line2(pinOutPos, pinOutEnd) );
		pinLines.addAll( pinOutLines );
		
		VecPath polyBody = new VecPath();
		polyBody.moveTo ( topLeft );
		polyBody.lineTo ( topFlatEnd );
		polyBody.curveTo( topBezC1, topBezC2, pinOutPos );
		polyBody.curveTo( botBezC2, botBezC1, botFlatEnd );
		polyBody.lineTo ( botFlatEnd );
		polyBody.lineTo ( botLeft );
		polyBody.curveTo( rearC2, rearC1, topLeft );
		polyBody.closePath();
		
		VecPath polyPins = new VecPath();
		for (Line2 pin: pinLines) {
			polyPins.moveTo( pin.a );
			polyPins.lineTo( pin.b );
		}
		
		VecPath polyXor = null;
		if (isXor) {
			Vec2 xorOffset    = new Vec2( -thickness*2, 0 );
			Vec2 xorOffsetMod = xorOffset.multiply( 1.5 );
			Vec2 xorRearTl    = topLeft.add( xorOffsetMod );
			Vec2 xorRearC1    = rearC1 .add( xorOffset    );
			Vec2 xorRearC2    = rearC2 .add( xorOffset    );
			Vec2 xorRearBr    = botLeft.add( xorOffsetMod );
			
			polyXor = new VecPath();
			polyXor.moveTo ( xorRearTl );
			polyXor.curveTo( xorRearC1, xorRearC2, xorRearBr );
		}
		
		GraphicComActive gate = new GraphicComActive(
			polyBody,
			polyPins,
			polyXor,
			genPinMappings( pinLines, 1 )
		);
		
		if (invert)
			gate.enableBubble( bubblePos, bubbleRadius );
		
		return gate;
	}
	
	
	
	/**
	 * inOffset == first index of the input pins
	 */
	private static List<GraphicPinMapping> genPinMappings( List<Line2> pinLines, int inOffset ) {
		final List<GraphicPinMapping> pinMappings = new ArrayList<>();
		
		for (int i=0; i<pinLines.size(); i++) {
			PinIoMode mode  = (i < inOffset) ? PinIoMode.output : PinIoMode.input;
			int       index = i;
			
			if (mode == PinIoMode.input)
				index -= inOffset;
			
			pinMappings.add( new GraphicPinMapping( pinLines.get(i), mode, index ) );
		}
		
		return pinMappings;
	}
	
	
	
	// Not used now, but will be useful for mux/demux and the like
	private static List<Line2> genPinLines( Line2 from, Line2 to, Vec2 uv, int count, boolean reverse ) {
		List<Line2> lines = new ArrayList<>();
		
		for (Vec2 pos: distributePins(from.a, from.b, count)) {
			Line2 intersector = new Line2( pos, pos.add(uv.multiply(1024)) );
			Line2.IntersectResult ir = intersector.intersect( to );
			
			if (ir.intersects) {
				Line2 pinLine = new Line2( pos, ir.pos );
				
				if (reverse)
					pinLine = pinLine.swapEndpoints();
				
				lines.add( pinLine );
			}
		}
		
		return lines;
	}
	
	
	
	private static List<Line2> genPinLines( Line2 from, BezierCubic2 to, Vec2 uv, int count, boolean reverse ) {
		List<Line2> lines = new ArrayList<>();
		
		for (Vec2 pos: distributePins(from.a, from.b, count)) {
			Line2 intersector = new Line2( pos, pos.add(uv.multiply(1024)) );
			BezierCubic2.IntersectResult ir = to.intersect( intersector, 1024 );
			
			if (ir.intersects) {
				Line2 pinLine = new Line2( pos, ir.posList.get(0) );
				
				if (reverse)
					pinLine = pinLine.swapEndpoints();
				
				lines.add( pinLine );
			}
		}
		
		return lines;
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
	
	
	
	private static Region getBaseRegion() {
		double half = baseSize * 0.5;
		return new Region( new Vec2(-half), new Vec2(half) );
	}
	
	
	
	private static double getPinSpacingGrowth() {
		return getBaseRegion().getSize().y * 0.25;
	}
	
	
	
	private static void applyPinGrowth( Region r, int pinCount ) {
		if (pinCount > pinGrowthThresh)
			r.br.y += getPinSpacingGrowth() * (pinCount - pinGrowthThresh);
	}
}














































