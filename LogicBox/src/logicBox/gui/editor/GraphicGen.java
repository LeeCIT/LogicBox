


package logicBox.gui.editor;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import logicBox.gui.VecPath;
import logicBox.sim.component.PinIoMode;
import logicBox.util.Bbox2;
import logicBox.util.BezierCubic2;
import logicBox.util.Geo;
import logicBox.util.Line2;
import logicBox.util.Util;
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
			gate.setBubble( true, bubblePos, bubbleRadius );
		
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
			gate.setBubble( true, bubblePos, bubbleRadius );
		
		recentreGraphic( gate );
		
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
			gate.setBubble( true, bubblePos, bubbleRadius );
		
		recentreGraphic( gate );
		
		return gate;
	}
	
	
	
	public static GraphicComActive generateDecoder( int inputCount, int outputCount ) {
		Bbox2 r = getBaseRegion();
	  	  	  r.transform( Geo.createTransform( new Vec2(0), new Vec2(1,2), 0) );
	  	
	  	applyPinGrowth( r, outputCount );
		
		Vec2 tl = r.getTopLeft();
		Vec2 tr = r.getTopRight();
		Vec2 bl = r.getBottomLeft();
		Vec2 br = r.getBottomRight();
		
		Line2 leftContact  = new Line2( tl, bl );
		Line2 rightContact = new Line2( tr, br );
		
		Line2 leftTerminal  = leftContact .translate( -pinLength, 0 );
		Line2 rightTerminal = rightContact.translate( +pinLength, 0 );
		
		List<Line2> pinInLines  = genPinLines( leftTerminal,  leftContact,  new Vec2(+1,0), inputCount,  true );
		List<Line2> pinOutLines = genPinLines( rightTerminal, rightContact, new Vec2(-1,0), outputCount, true );
		
		List<Line2> pinLines = new ArrayList<>();
		pinLines.addAll( pinOutLines );
		pinLines.addAll( pinInLines  );
		
		GraphicComActive graphic = new GraphicComActive(
			genPolyBody( true, br, tr, tl, bl ),
			genPolyPins( pinLines ),
			null,
			genPinMappings( pinLines, pinOutLines.size() )
		);
		
		recentreGraphic( graphic );
		
		return graphic;
	}
	
	
	
	private static GraphicComActive generatePlexer( int inputs, int selects, int outputs, boolean isDemux ) {
		Bbox2 r = getBaseRegion();
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
	
	
	
	public static GraphicComActive generateFlipFlop( int inputCount ) {
		Bbox2 r = getBaseRegion();
	  	  	  r.transform( Geo.createTransform( new Vec2(0), new Vec2(1,2), 0) );
	
		Vec2 tl = r.getTopLeft();
		Vec2 tr = r.getTopRight();
		Vec2 bl = r.getBottomLeft();
		Vec2 br = r.getBottomRight();
		
		Line2 leftContact  = new Line2( tl, bl );
		Line2 rightContact = new Line2( tr, br );
		
		Line2 leftTerminal  = leftContact .translate( -pinLength, 0 );
		Line2 rightTerminal = rightContact.translate( +pinLength, 0 );
		
		List<Line2> pinInLines  = genPinLines( leftTerminal,  leftContact,  new Vec2(+1,0), inputCount, true );
		List<Line2> pinOutLines = genPinLines( rightTerminal, rightContact, new Vec2(-1,0), 2,          true );
		
		List<Line2> pinLines = new ArrayList<>();
		pinLines.addAll( pinOutLines );
		pinLines.addAll( pinInLines  );
		
		final List<GraphicPinMapping> gpms = genPinMappings( pinLines, pinOutLines.size() );
		
		Vec2    arrowPos  = gpms.get(2+1).getPinPosBody();
		double  arrowOffs = r.getSize().x * 0.3;
		VecPath polyArrow = new VecPath();
		polyArrow.moveTo( arrowPos.add( 2,        -arrowOffs*0.5 ) );
		polyArrow.lineTo( arrowPos.add( arrowOffs, 0             ) );
		polyArrow.lineTo( arrowPos.add( 2,        +arrowOffs*0.5 ) );
		
		return new GraphicComActive(
			genPolyBody( true, br, tr, tl, bl ),
			genPolyPins( pinLines ),
			polyArrow,
			gpms
		);
	}
	
	
	
	private static HashMap<GraphicPinMapping,String> genLabelMap( List<GraphicPinMapping> gpms, String...labels ) {
		HashMap<GraphicPinMapping,String> labelMap = new HashMap<>();
		
		for (int i=0; i<labels.length; i++) {
			String str = labels[i];
			labelMap.put( gpms.get(i), str );
		}
		
		return labelMap;
	}
	
	
	
	public static GraphicComActive generateFlipFlopD() {
		GraphicComActive        graphic = generateFlipFlop( 2 );
		List<GraphicPinMapping> gpms    = graphic.getGraphicPinMappings();
		
		graphic.setPinLabels( genLabelMap( gpms, "Q", "!Q", "D", "" ) );		
		return graphic;
	}
	
	
	
	public static GraphicComActive generateFlipFlopT() {
		GraphicComActive        graphic = generateFlipFlop( 2 );
		List<GraphicPinMapping> gpms    = graphic.getGraphicPinMappings();
		
		graphic.setPinLabels( genLabelMap( gpms, "Q", "!Q", "T", "" ) );
		return graphic;
	}
	
	
	
	public static GraphicComActive generateFlipFlopJK() {
		GraphicComActive        graphic = generateFlipFlop( 3 );
		List<GraphicPinMapping> gpms    = graphic.getGraphicPinMappings();
		
		graphic.setPinLabels( genLabelMap( gpms, "Q", "!Q", "J", "", "K" ) );		
		return graphic;
	}
	
	
	
	public static Graphic generateJunction() {
		return new GraphicJunction( new Vec2(0) );
	}
	
	
	
	public static Graphic generateTrace() {
		List<Vec2> points = new ArrayList<>();
		points.add( new Vec2( -24,   0) );
		points.add( new Vec2(   0,  18) );
		points.add( new Vec2(  14, -18) );
		points.add( new Vec2(  28,   0) );
		
		GraphicTrace graphic = new GraphicTrace( points );
		graphic.setConnectedSource( true );
		graphic.setConnectedDest  ( true );
		return graphic;
	}
	
	
	
	public static GraphicComActive generateSourceFixed( boolean level ) {
		Bbox2 r = getBaseRegion();
		Vec2  a = r.getNorm( 0.5, 1.0 );
		Vec2  b = r.getNorm( 1.0, 0.5 );
		Vec2  c = r.getNorm( 0.5, 0.0 );
		Vec2  d = r.getNorm( 0.0, 0.5 );
		
		Line2 pinLine = new Line2( b, b.add(pinLength,0) );
		List<Line2> pinLines = new ArrayList<>();
		pinLines.add( pinLine );
		
		List<GraphicPinMapping> gpms = genPinMappings( pinLines, pinLines.size() );
		
		GraphicComActive graphic = new GraphicComActive(
			genPolyBody( true, a, b, c, d ),
			genPolyPins( pinLines ),
			null,
			gpms
		);
		
		graphic.setPinLabels( genLabelMap( gpms, level ? "1" : "0" ) );		
		
		return graphic;
	}
	
	
	
	public static GraphicComActive generateSourceToggle() {
		return generateSourceFixed( false );
	}
	
	
	
	public static GraphicComActive generateSourceOscillator() {
		Bbox2 r = getBaseRegion();
		      r.transform( Geo.createTransform( new Vec2(0), new Vec2(2,1), 0) );
		      
		Vec2 br = r.getBottomRight();
		Vec2 tr = r.getTopRight();
		Vec2 tl = r.getTopLeft();
		Vec2 bl = r.getBottomLeft();
		Vec2 rm = r.getRightMiddle();
		
		Line2 pinLine = new Line2( rm, rm.add(pinLength,0) );
		List<Line2> pinLines = new ArrayList<>();
		pinLines.add( pinLine );
		
		VecPath polySqr = new VecPath();
		polySqr.moveTo( r.getNorm(-0.25, 1) );
		polySqr.lineTo( r.getNorm( 0.25, 1) );
		polySqr.lineTo( r.getNorm( 0.25, 0) );
		polySqr.lineTo( r.getNorm( 0.75, 0) );
		polySqr.lineTo( r.getNorm( 0.75, 1) );
		polySqr.lineTo( r.getNorm( 1.25, 1) );
		polySqr.transform( Geo.createTransform( new Vec2(0), new Vec2(0.5), 0 ) );
			
		return new GraphicComActive(
			genPolyBody( true, br, tr, tl, bl ),
			genPolyPins( pinLines ),
			polySqr,
			genPinMappings( pinLines, pinLines.size() )
		);
	}
	
	
	
	public static GraphicComActive generateDisplayLed() {
		Bbox2 r = getBaseRegion();
		
		VecPath polyBody = new VecPath();
		polyBody.moveTo ( r.getBottomMiddle() );
		polyBody.curveTo( r.getBottomRight(), r.getRightMiddle()  );
		polyBody.curveTo( r.getTopRight(),    r.getTopMiddle()    );
		polyBody.curveTo( r.getTopLeft(),     r.getLeftMiddle()   );
		polyBody.curveTo( r.getBottomLeft(),  r.getBottomMiddle() );
		polyBody.closePath();
		
		Vec2  leftMid = r.getLeftMiddle();
		Line2 pinLine = new Line2( leftMid, leftMid.add(-pinLength,0) );
		
		List<Line2> pinLines = new ArrayList<>();
		pinLines.add( pinLine );
		
		GraphicComActive graphic =  new GraphicComActive(
			polyBody,
			genPolyPins( pinLines ),
			null, 
			genPinMappings( pinLines, 0 )
		);
		
		graphic.setBubble( true, new Vec2(0), r.getSmallest() / 16 );
		graphic.setFillOverride( true, EditorStyle.colLedOn );
		
		return graphic;
	}
	
	
	
	public static GraphicSevenSeg generateDisplaySevenSeg() {
		Bbox2 r = getBaseRegion();
		  	  r.transform( Geo.createTransform( new Vec2(0), new Vec2(1,1.5), 0) );
		
		Vec2 tl = r.getTopLeft();
		Vec2 tr = r.getTopRight();
		Vec2 bl = r.getBottomLeft();
		Vec2 br = r.getBottomRight();
		
		Line2 botContact  = new Line2( bl, br );
		Line2 botTerminal = botContact.translate( 0, pinLength );
		
		List<Line2> pinLines = genPinLines( botTerminal, botContact, new Vec2(0,-1), 4, true );
		Collections.reverse( pinLines );
		
		return new GraphicSevenSeg(
			genPolyBody( true, br, tr, tl, bl ),
			genPolyPins( pinLines ),
			null,
			genPinMappings( pinLines, 0 ),
			genSegs( r )
		);
	}
	
	
	
	private static List<VecPath> genSegs( Bbox2 r ) {
		r = Util.deepCopy( r );
		r.transform( Geo.createTransform(new Vec2(0), new Vec2(0.7,0.8), 0) );		
		
		List<VecPath> segs = new ArrayList<>();
		
		double f    = 0.10;
		double fi   = 1 - f;
		double d    = 0.15;
		double h    = 0.50;
		double dh   = d * 0.35;
		Vec2   size = r.getSize();
		
		VecPath segTop = new VecPath();
		segTop.moveTo( r.getNorm(0  , 0)  );
		segTop.lineTo( r.getNorm(1  , 0)  );
		segTop.lineTo( r.getNorm(1-d, f)  );
		segTop.lineTo( r.getNorm(0+d, f)  );
		segTop.closePath();		
		
		
		VecPath segMid = new VecPath();
		segMid.moveTo( r.getNorm(f   , h   ) );
		segMid.lineTo( r.getNorm(f +d, h-dh) );
		segMid.lineTo( r.getNorm(fi-d, h-dh) );
		segMid.lineTo( r.getNorm(fi  , h   ) );
		segMid.lineTo( r.getNorm(fi-d, h+dh) );
		segMid.lineTo( r.getNorm(f +d, h+dh) );
		segMid.closePath();
		
		VecPath segBot = Util.deepCopy( segTop );
		segBot.transform( Geo.createTransform( new Vec2(0), 180 ) );
		
		VecPath segTL = Util.deepCopy( segTop );
		segTL.transform( Geo.createTransform( new Vec2(size.x*0.35,size.y*-0.3), new Vec2(1,0.7), 90 ) );
		
		VecPath segBL = Util.deepCopy( segTL );
		segBL.transform( Geo.createTransform( new Vec2(0), new Vec2(1,-1), 0 ) );
		
		VecPath segTR = Util.deepCopy( segTL );
		segTR.transform( Geo.createTransform( new Vec2(0), new Vec2(-1,1), 0 ) );
		
		VecPath segBR = Util.deepCopy( segTL );
		segBR.transform( Geo.createTransform( new Vec2(0), new Vec2(-1,-1), 0 ) );
		
		segs.add( segBR  );
		segs.add( segTR  );
		segs.add( segTop );
		segs.add( segTL  );
		segs.add( segMid );
		segs.add( segBL  );
		segs.add( segBot );
		
		return segs;
	}
	
	
	
	public static GraphicComActive generateBlackboxPin( boolean isOutput ) {
		Bbox2 r = getBaseRegion();
		
		VecPath polyBody = generateBlackboxSocket();
		
		Vec2 pinPos = r.getCentre();
		if (isOutput) {
			polyBody.moveTo( r.getNorm( 0.5, 1   ) );
			polyBody.lineTo( r.getNorm( 0.5, 0   ) );
			polyBody.lineTo( r.getNorm( 0,   0.5 ) );
			polyBody.closePath();
		} else {
			polyBody.moveTo( pinPos );
			polyBody.lineTo( r.getNorm( 0, 0 ) );
			polyBody.lineTo( r.getNorm( 0, 1 ) );
			polyBody.closePath();
		}
		
		Line2 pinLine = new Line2( pinPos, pinPos.add(pinLength,0) );
		
		List<Line2> pinLines = new ArrayList<>();
		pinLines.add( pinLine );
		
		GraphicComActive graphic = new GraphicComActive(
			polyBody,
			genPolyPins( pinLines ),
			null,
			genPinMappings( pinLines, isOutput ? 0 : 1 )
		);
		
		recentreGraphic( graphic );
		
		if (isOutput)
			graphic.transform( Geo.createTransform(new Vec2(0), new Vec2(-1,1), 0), true );
		
		return graphic;
	}
	
	
	
	private static VecPath generateBlackboxSocket() {
		Bbox2 r = getBaseRegion();
		
		VecPath poly = new VecPath();
		poly.moveTo( r.getNorm( 0.9, 1    ) );
		poly.lineTo( r.getNorm( 0.9, 0    ) );
		poly.lineTo( r.getNorm( 0.8, 0    ) );
		poly.lineTo( r.getNorm( 0.5, 0.33 ) );
		poly.lineTo( r.getNorm( 0  , 0.33 ) );
		poly.lineTo( r.getNorm( 0  , 0.66 ) );
		poly.lineTo( r.getNorm( 0.5, 0.66 ) );
		poly.lineTo( r.getNorm( 0.8, 1    ) );
		poly.lineTo( r.getNorm( 0.9, 1    ) );
		poly.closePath();
		
		poly.transform( Geo.createTransform( new Vec2(-baseSize,0), 0 ) );
		
		return poly;
	}
	
	
	
	public static GraphicComActive generateBlackBox( List<PinIoMode> left, List<PinIoMode> right, List<PinIoMode> top, List<PinIoMode> bottom ) {
		Bbox2 r = getBaseRegion();
	  	
	  	int maxPinsX = Math.max( top .size(),  bottom.size() );
	  	int maxPinsY = Math.max( left.size(),  right .size() );
	  	applyPinGrowth( r, maxPinsX, maxPinsY );
	
		Vec2 tl = r.getTopLeft();
		Vec2 tr = r.getTopRight();
		Vec2 bl = r.getBottomLeft();
		Vec2 br = r.getBottomRight();
		
		Line2 contactLeft  = new Line2( tl, bl );
		Line2 contactRight = new Line2( tr, br );
		Line2 contactTop   = new Line2( tl, tr );
		Line2 contactBot   = new Line2( bl, br );
		
		Line2 terminalLeft  = contactLeft .translate( -pinLength,  0         );
		Line2 terminalRight = contactRight.translate( +pinLength,  0         );
		Line2 terminalTop   = contactTop  .translate(  0,         -pinLength );
		Line2 terminalBot   = contactBot  .translate(  0,         +pinLength );
		
		List<Line2> pinLinesLeft  = genPinLines( terminalLeft , contactLeft , new Vec2(+1, 0), left  .size(), true );
		List<Line2> pinLinesRight = genPinLines( terminalRight, contactRight, new Vec2(-1, 0), right .size(), true );
		List<Line2> pinLinesTop   = genPinLines( terminalTop  , contactTop  , new Vec2( 0,+1), top   .size(), true );
		List<Line2> pinLinesBot   = genPinLines( terminalBot  , contactBot  , new Vec2( 0,-1), bottom.size(), true );
		
		List<Line2> pinLines = new ArrayList<>();
		pinLines.addAll( pinLinesLeft  );
		pinLines.addAll( pinLinesRight );
		pinLines.addAll( pinLinesTop   );
		pinLines.addAll( pinLinesBot   );
		
		List<PinIoMode> modes = new ArrayList<>();
		modes.addAll( left   );
		modes.addAll( right  );
		modes.addAll( top    );
		modes.addAll( bottom );
		
		List<GraphicPinMapping> gpms = new ArrayList<>();
		int indexIn  = 0;
		int indexOut = 0;
		
		for (int i=0; i<modes.size(); i++) {
			Line2     line = pinLines.get( i );
			PinIoMode mode = modes   .get( i );
			int       index;
			
			if (mode == PinIoMode.input)
				 index = indexIn ++;
			else index = indexOut++;
			
			gpms.add( new GraphicPinMapping(line, mode, index) );
		}
		
		GraphicComActive graphic = new GraphicComActive(
			genPolyBody( true, br, tr, tl, bl ),
			genPolyPins( pinLines ),
			null,
			gpms
		);
		
		recentreGraphic( graphic );
		
		return graphic;
	}
	
	
	
	private static void recentreGraphic( GraphicComActive graphic ) {
		Vec2 trans = graphic.getBbox().getCentre().negate();
		graphic.transform( Geo.createTransform(trans,0), true );
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
	
	
	
	private static void applyPinGrowth( Bbox2 r, int xAxisMaxCount, int yAxisMaxCount ) {
		if (yAxisMaxCount > pinGrowthThresh) r.br.y += getPinSpacingGrowth() * (yAxisMaxCount - pinGrowthThresh);
		if (xAxisMaxCount > pinGrowthThresh) r.br.x += getPinSpacingGrowth() * (xAxisMaxCount - pinGrowthThresh);
	}
}














































