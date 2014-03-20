


package logicBox.gui.editor;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import logicBox.gui.VecPath;
import logicBox.sim.component.PinIoMode;
import logicBox.util.Bbox2;
import logicBox.util.BezierCubic2;
import logicBox.util.Geo;
import logicBox.util.Line2;
import logicBox.util.Vec2;



/**
 * Generates graphical representations for components along with metadata
 * which allows them to be interfaced with the simulation.
 * @author Lee Coakley
 */
public abstract class GraphicGen
{
	public static GraphicComActive generateGateBuffer() {
		return generateGateBuffer( false );
	}	
	
	public static GraphicComActive generateGateNot() {
		return generateGateBuffer( true );
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
	
	public static GraphicComActive generateGateXor( int pinCount ) {
		return generateGateOr( pinCount, true, false );
	}
	
	public static GraphicComActive generateGateXnor( int pinCount ) {
		return generateGateOr( pinCount, true, true );
	}
	
	public static GraphicComActive generateMux( int pinInputs, int pinSelects, int pinOutputs ) {
		return generatePlexer( pinInputs, pinSelects, pinOutputs, false );
	}
	
	public static GraphicComActive generateDemux( int pinInputs, int pinSelects, int pinOutputs ) {
		return generatePlexer( pinInputs, pinSelects, pinOutputs, true );
	}
	
	
	
	
	
	private static final double baseSize        = 64;
	private static final double pinLenFrac      = 0.5;
	private static final double bubbleFrac      = 0.1;
	private static final int    pinGrowthThresh = 4;
	private static final double pinLength       = baseSize * pinLenFrac;
	private static final double bubbleRadius    = baseSize * bubbleFrac;
	private static final double thickness       = EditorStyle.compThickness;
	
	
	
	public static GraphicComActive generatePlaceholder() {
		Bbox2 r = getBaseRegion();
		Vec2  a = r.getNorm( 0.5, 1.0 );
		Vec2  b = r.getNorm( 1.0, 0.5 );
		Vec2  c = r.getNorm( 0.5, 0.0 );
		Vec2  d = r.getNorm( 0.0, 0.5 );
		
		return new GraphicComActive(
			genPolyBody( true, a, b, c, d ),
			null,
			null,
			new ArrayList<GraphicPinMapping>()
		);
	}
	
	
	
	private static GraphicComActive generateGateBuffer( boolean invert ) {
		Bbox2 r = getBaseRegion();
		
		Vec2 pinOutPos = r.getRightMiddle();
		Vec2 pinOutEnd = pinOutPos.add( pinLength, 0 );
		Vec2 bubblePos = pinOutPos.add( bubbleRadius + thickness*0.3, 0 );
		
		Vec2 pinInPos = r.getLeftMiddle();
		Vec2 pinInEnd = pinInPos.add( -pinLength, 0 );
		
		Vec2 topLeft = r.getTopLeft();
		Vec2 botLeft = r.getBottomLeft();
		
		List<Line2> pinLines = new ArrayList<>();
		pinLines.add( new Line2(pinOutPos, pinOutEnd) );
		pinLines.add( new Line2(pinInPos,  pinInEnd ) );
		
		GraphicComActive gate = new GraphicComActive(
			genPolyBody( true, botLeft, pinOutPos, topLeft ),
			genPolyPins( pinLines ),
			null,
			genPinMappings( pinLines, 1 )
		);
		
		if (invert)
			gate.enableBubble( bubblePos, bubbleRadius );
		
		return gate;
	}
	
	
	
	private static GraphicComActive generateGateAnd( int pinCount, boolean invert ) {
		Bbox2  r        = getBaseRegion();
		double flatFrac = 0.5;
		
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
		
		GraphicComActive gate = new GraphicComActive(
			polyBody,
			genPolyPins( pinLines ),
			null,
			genPinMappings( pinLines, 1 )
		);
		
		if (invert)
			gate.enableBubble( bubblePos, bubbleRadius );
		
		return gate;
	}
	
	
	
	private static GraphicComActive generateGateOr( int pinCount, boolean isXor, boolean invert ) {
		Bbox2  r        = getBaseRegion();
		double flatFrac = 1.0 / 8.0;
		
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
		
		double rearConOffs = r.getSize().x * 0.33;
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
			genPolyPins( pinLines ),
			polyXor,
			genPinMappings( pinLines, 1 )
		);
		
		if (invert)
			gate.enableBubble( bubblePos, bubbleRadius );
		
		return gate;
	}
	
	
	
	private static GraphicComActive generatePlexer( int inputs, int selects, int outputs, boolean isDemux ) {
		Bbox2  r = getBaseRegion();
			   r.transform( Geo.createTransform( new Vec2(0), new Vec2(2,2), 0) );
		
		applyPinGrowth( r, Math.max(inputs,outputs) );
		
		Vec2 tl = r.getTopLeft();
		Vec2 bl = r.getBottomLeft();
		Vec2 tr = r.getNorm( 0.5, 0.25 );
		Vec2 br = r.getNorm( 0.5, 0.75 );
		
		double pinInX        = tl.x - pinLength;
		Line2  pinInTerminal = new Line2( pinInX, tl.y, pinInX, bl.y );
		Line2  pinInContact  = new Line2( tl, bl );
		Vec2   pinInUV       = new Vec2(1, 0);
		
		double pinSelY        = bl.y + pinLength;
		Line2  pinSelTerminal = new Line2( bl.x, pinSelY, br.x, pinSelY );
		Line2  pinSelContact  = new Line2( bl, br );
		Vec2   pinSelUV       = new Vec2(0, -1);
		
		double pinOutX        = tr.x + pinLength;
		Line2  pinOutTerminal = new Line2( pinOutX, tr.y, pinOutX, br.y );
		Line2  pinOutContact  = new Line2( tr, br );
		Vec2   pinOutUV       = new Vec2(-1, 0);
		
		if (isDemux) { // Swap In/Out.  std:swap this ain't.
			double swapInX        = pinInX;
			Line2  swapInTerminal = pinInTerminal;
			Line2  swapInContact  = pinInContact;
			Vec2   swapUV         = pinInUV;
				   pinInX         = pinOutX;
				   pinInTerminal  = pinOutTerminal;
				   pinInContact   = pinOutContact ;
				   pinInUV        = pinOutUV;
			       pinOutX        = swapInX;
			       pinOutTerminal = swapInTerminal;
				   pinOutContact  = swapInContact;
				   pinOutUV       = swapUV;
		}
		
		List<Line2> pinInLines  = genPinLines( pinInTerminal,  pinInContact,  pinInUV,  inputs,  true );
		List<Line2> pinSelLines = genPinLines( pinSelTerminal, pinSelContact, pinSelUV, selects, true );
		List<Line2> pinOutLines = genPinLines( pinOutTerminal, pinOutContact, pinOutUV, outputs, true );
		
		if (isDemux)
			Collections.reverse( pinSelLines );
		
		List<Line2> pinLines = new ArrayList<>();
		pinLines.addAll( pinOutLines );
		pinLines.addAll( pinInLines  );
		pinLines.addAll( pinSelLines );
		
		GraphicComActive graphic = new GraphicComActive(
			genPolyBody( true, tl, tr, br, bl ),
			genPolyPins( pinLines ),
			null,
			genPinMappings( pinLines, pinOutLines.size() )
		);
		
		Vec2            trans  = Bbox2.createFromPoints(tl,bl,tr,br).getCentre().negate();
		Vec2            scale  = new Vec2( isDemux?-1:1, 1 ); 
		AffineTransform matrix = Geo.createTransform( trans, scale, 0 );
		graphic.transform( matrix, true );
		
		return graphic;
	}
	
	
	
	private static VecPath genPolyBody( boolean close, Vec2...points ) {	
		VecPath polyBody = new VecPath();
		
		polyBody.moveTo( points[0] );
		
		for (int i=1; i<points.length; i++)
			polyBody.lineTo ( points[i] );
		
		if (close)
			polyBody.closePath();
		
		return polyBody;
	}
	
	
	
	private static VecPath genPolyPins( List<Line2> pinLines ) {
		VecPath polyPins = new VecPath();
		
		for (Line2 pin: pinLines) {
			polyPins.moveTo( pin.a );
			polyPins.lineTo( pin.b );
		}
		
		return polyPins;
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
	
	
	
	/**
	 * Generate pin lines by intersecting line segments.
	 * UV is the unit vector the intersect line is extended across.
	 * Reverse indicates whether the line order will be inverted.
	 * The side connected to the component should always be A, the terminal B.
	 */
	private static List<Line2> genPinLines( Line2 from, Line2 to, Vec2 uv, int count, boolean reverse ) {
		List<Line2> lines = new ArrayList<>();
		
		for (Vec2 pos: distributePins(from.a, from.b, count)) {
			Line2 intersector = new Line2( pos, pos.add(uv.multiply(1024)) );
			Line2.IntersectResult ir = to.intersect( intersector );
			
			if (ir.intersects) {
				Line2 pinLine = new Line2( pos, ir.pos );
				
				if (reverse)
					pinLine = pinLine.swapEndpoints();
				
				lines.add( pinLine );
			}
		}
		
		return lines;
	}
	
	
	
	/**
	 * Generate pin lines by intersecting line segments with a cubic bezier curve.
	 * UV is the unit vector the intersect line is extended across.
	 * Reverse indicates whether the line order will be inverted.
	 * The side connected to the component should always be A, the terminal B.
	 */
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
		
		double dist    = Geo.distance( a, b );
		Vec2   deltaUV = Geo.normalise( b.subtract(a) );
		Vec2   step    = deltaUV.multiply( dist / count );
		Vec2   offset  = step   .multiply( 0.5 );
		Vec2   cursor  = a.add( offset );
		
		for (int i=0; i<count; i++) {
			list.add( cursor.copy() );
			cursor = cursor.add( step );
		}
		
		return list;
	}
	
	
	
	private static Bbox2 getBaseRegion() {
		double half = baseSize * 0.5;
		return new Bbox2( new Vec2(-half), new Vec2(half) );
	}
	
	
	
	private static double getPinSpacingGrowth() {
		return getBaseRegion().getSize().y * 0.25;
	}
	
	
	
	private static void applyPinGrowth( Bbox2 r, int pinCount ) {
		if (pinCount > pinGrowthThresh)
			r.br.y += getPinSpacingGrowth() * (pinCount - pinGrowthThresh);
	}
}














































