


package logicBox.gui.editor.tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import logicBox.gui.Gfx;
import logicBox.gui.VecPath;
import logicBox.gui.editor.EditorWorld;
import logicBox.gui.editor.SimMapper;
import logicBox.gui.editor.components.EditorComponent;
import logicBox.gui.editor.components.EditorComponentActive;
import logicBox.gui.editor.components.EditorComponentJunction;
import logicBox.gui.editor.components.EditorComponentTrace;
import logicBox.gui.editor.graphics.EditorStyle;
import logicBox.gui.editor.graphics.GraphicPinMapping;
import logicBox.gui.editor.graphics.RepaintListener;
import logicBox.sim.Simulation;
import logicBox.sim.component.connective.Pin;
import logicBox.sim.component.connective.Trace;
import logicBox.util.Bbox2;
import logicBox.util.Geo;
import logicBox.util.Line2;
import logicBox.util.Vec2;



/**
 * Allows drawing of traces.
 * Manipulating them and integrating them into the world will be separate.
 * @author Lee Coakley
 */
public class ToolTraceDrawer extends Tool
{
	private boolean         traceInitiated;
	private boolean         traceChoosingOrigin;
	private boolean         traceArmed;
	private Stack<Vec2>     tracePoints;
	private Vec2            tracePosNext;
	private MouseAdapter    mouseListener;
	private RepaintListener repaintListener;
	
	private EditorWorld.FindClosestPinResult traceSrc;
	private EditorWorld.FindClosestPinResult traceDest;
	
	
	
	public ToolTraceDrawer( ToolManager manager ) {
		super( manager );
		this.mouseListener   = createMouseListener();
		this.repaintListener = createRepaintListener();
		this.tracePoints     = new Stack<>();
		this.tracePosNext    = new Vec2();
	}
	
	
	
	public void attach() {
		if (isAttached())
			return;
		
		getEditorPanel().addMouseListener      ( mouseListener );
		getEditorPanel().addMouseMotionListener( mouseListener );
		getEditorPanel().addWorldRepaintListener( repaintListener );
		setTransHint();
		setAttached( true );
	}
	
	
	
	public void detach() {
		if ( ! isAttached())
			return;
		
		getEditorPanel().removeMouseListener      ( mouseListener );
		getEditorPanel().removeMouseMotionListener( mouseListener );
		getEditorPanel().removeWorldRepaintListener( repaintListener );
		removeTransHint();
		setAttached( false );
	}
	
	
	
	public void reset() {
		traceInitiated      = false;
		traceChoosingOrigin = false;
		traceArmed          = false;
		traceSrc            = null;
		traceDest           = null;
		tracePoints.clear();
	}
	
	
	
	private void setTransHint() {
		setTransHint(
		    "Left-click to add to the trace.\n" +
		    "Right-click to remove the last segment added, or cancel if there are no more segments.\n" +
		    "The trace ends when you connect it to another component.\n" +
		    "Hold down the Control key and left-click to end the trace without attaching to anything."
		);
	}
	
	
	
	private RepaintListener createRepaintListener() {
		return new RepaintListener() {
			public void draw( Graphics2D g ) {
				drawFeedback( g );
			}
		};
	}
	
	
	
	private void drawFeedback( Graphics2D g ) {
		drawPinHighlights( g );
		
		if (traceChoosingOrigin || ! traceInitiated)
			Gfx.drawCircle( g, tracePosNext, 7, getCol(traceChoosingOrigin), true );
		
		if ( (!traceInitiated) || tracePoints.isEmpty())
			return;
		
		Gfx.drawCircle( g, tracePosNext, 5, getCol(traceArmed), true );
		
		Gfx.pushStrokeAndSet( g, EditorStyle.strokeTrace );
			Gfx.pushColorAndSet( g, EditorStyle.colTraceOff );
				drawExistingLines( g );
				drawNextLine( g );
			Gfx.popColor ( g );
		Gfx.popStroke( g );
	}
	
	
	
	private void drawPinHighlights( Graphics2D g ) {
		PinSnapInfo pinSnapInfo  = getPinSnapInfo( getMousePosWorld() );
		double   thickness = EditorStyle.compThickness + 2;
		
		Gfx.pushColorAndSet( g, EditorStyle.colHighlightStroke );
			if (pinSnapInfo.snapped)
				Gfx.drawThickRoundedLine( g, pinSnapInfo.pinInfo.gpm.line, thickness );
			
			if (traceSrc != null)
				Gfx.drawThickRoundedLine( g, traceSrc.gpm.line, thickness );
			
			if (traceDest != null)
				Gfx.drawThickRoundedLine( g, traceDest.gpm.line, thickness );
		Gfx.popColor( g );
	}
	
	
	
	private Color getCol( boolean armed ) {
		return (armed) ? EditorStyle.colHighlightStroke : EditorStyle.colTraceOff;
	}
	
	
	
	private void drawNextLine( Graphics2D g ) {
		Gfx.pushStrokeAndSet( g, EditorStyle.strokeTracePlace );
			Gfx.pushColorAndSet( g, EditorStyle.colHighlightStroke );
			
				List<Vec2> points = breakLineToFitSnap( tracePoints.peek(), tracePosNext );
				
				VecPath polyLineVis = new VecPath();
				polyLineVis.moveTo( points.get(0) );
				
				for (int i=1; i<points.size(); i++)
					polyLineVis.lineTo( points.get(i) );
				
				g.draw( polyLineVis );
			
			Gfx.popColor ( g );
		Gfx.popStroke( g );
	}
	
	
	
	private void drawExistingLines( Graphics2D g ) {
		if (traceSrc != null)
			drawConnection( g, tracePoints.get(0) );
		
		VecPath polyLine = new VecPath();
		polyLine.moveTo( tracePoints.get(0) );
		
		for (int i=1; i<tracePoints.size(); i++)
			polyLine.lineTo( tracePoints.get(i) );
			
		g.draw( polyLine );
	}
	
	
	
	private void drawConnection( Graphics2D g, Vec2 pos ) {
		double radius = 3;
		
		Gfx.pushStrokeAndSet( g, EditorStyle.strokeBubble );
			Gfx.pushAntialiasingStateAndSet( g, false );
				Gfx.drawCircle( g, pos, radius, EditorStyle.colBackground, true );
			Gfx.popAntialiasingState( g );
			
			Gfx.drawCircle( g, pos, radius, EditorStyle.colTraceOff, false );
		Gfx.popStroke( g );
	}
	
	
	
	private MouseAdapter createMouseListener() {
		return new MouseAdapter() {
			public void mousePressed( MouseEvent ev ) {
				if (isLeft( ev ))
					if ( ! traceInitiated)
						 traceStartChoosingOrigin();
					else traceArm();
			}
			
			
			public void mouseReleased( MouseEvent ev ) {
				if (isLeft( ev ))
					if ( ! traceInitiated) { 
						 traceStart();
					} else {
						if (ev.isControlDown() && traceHasPoints())
							 traceCompleteForce();
						else traceAdd();
					}
				
				if (isRight( ev ))
					if (traceHasPoints())
						 traceUndo();
					else traceCancel();
			}
			
			
			public void mouseMoved( MouseEvent ev ) {
				traceMove();
			}
			
			
			public void mouseDragged( MouseEvent ev ) {
				traceMove();
			}
		};
	}
	
	
	
	public void traceStartChoosingOrigin() {
		traceChoosingOrigin = true;
		tracePosNext        = getNextPos();
		repaint();
	}
	
	
	
	private void traceArm() {
		traceArmed          = true;
		traceChoosingOrigin = ! traceInitiated;
		repaint();
	}
	
	
	
	private void traceStart() {
		traceAdd();
		traceInitiated      = true;
		traceChoosingOrigin = false;
	}
	
	
	
	private void traceAdd() {
		traceArmed = false;
		
		Vec2    nextPos   = getMousePosWorld();
		boolean completed = doTraceToPinSnapping( nextPos );
		
		if ( ! traceHasPoints()) {
			tracePoints.push( nextPos );
		} else {
			List<Vec2> points = breakLineToFitSnap( tracePoints.peek(), nextPos );
			points.remove( 0 );
			for (Vec2 v: points)
				tracePoints.push( v );
		}
		
		if (completed)
			traceComplete();
		
		repaint();
	}
	
	
	
	private void traceCompleteForce() {
		traceAdd();
		traceComplete();
	}
	
	
	
	/**
	 * Apply snap to Vec2 and update the source/dest info.
	 * @return Whether the trace is now completed (attached to a component)
	 * TODO it's also possible to join a trace onto another trace.
	 */
	private boolean doTraceToPinSnapping( Vec2 nextPos ) {
		PinSnapInfo pinSnapInfo = getPinSnapInfo( nextPos );
		boolean     completed   = false;
		
		if (pinSnapInfo.snapped) {
			boolean dupe = isGpmUsed( pinSnapInfo.pinInfo.gpm );
			
			if ( ! dupe) {
				nextPos.setLocation( pinSnapInfo.pos );
				
				boolean hasPoints = traceHasPoints(); 
				boolean isSource  = (traceChoosingOrigin && traceSrc == null);
				boolean isDest    = (hasPoints);
				
				if (isSource) {
					traceSrc = pinSnapInfo.pinInfo;
				} 
				else if (isDest) {
					traceDest = pinSnapInfo.pinInfo;
					completed = true;
				}
			}
		}
		
		return completed;
	}
	
	
	
	private boolean isGpmUsed( GraphicPinMapping gpm ) {
		return (traceSrc  != null  &&  traceSrc .gpm == gpm)
			|| (traceDest != null  &&  traceDest.gpm == gpm);
	}
	
	
	
	private boolean traceHasPoints() {
		return ! tracePoints.isEmpty();
	}
	
	
	
	private void traceUndo() {
		if (traceHasPoints()) {
			tracePoints.pop();
			repaint();
			
			if ( ! traceHasPoints())
				reset();
		}
	}
	
	
	
	private void traceMove() {
		tracePosNext = getNextPos();
		repaint();
	}
	
	
	
	private void traceComplete() {
		traceCreate();
		traceFinishCommon();
	}
	
	
	
	private void traceCancel() {
		traceFinishCommon();
		getToolManager().releaseControl();
	}
	
	
	
	private void traceFinishCommon() {
		traceInitiated      = false;
		traceChoosingOrigin = false;
		traceSrc            = null;
		traceDest           = null;
		tracePoints.clear();
		repaint();		
	}
	
	
	
	private Vec2 getNextPos() {
		Vec2        pos         = getMousePosWorld();
		PinSnapInfo pinSnapInfo = getPinSnapInfo( pos );
		boolean     useSnap     = pinSnapInfo.snapped && ! isGpmUsed(pinSnapInfo.pinInfo.gpm);
		
		EditorComponentJunction junc = findJunction( pos );
		if (junc != null)
			return junc.getPos();
		
		if (useSnap)
			 return pinSnapInfo.pos;
		else return pos;
	}
	
	
	
	private void traceCreate() {
		Vec2 start = tracePoints.firstElement();
		Vec2 end   = tracePoints.lastElement();
		
		PinSnapInfo siA = getPinSnapInfo( start );
		PinSnapInfo siB = getPinSnapInfo( end   );
		
		Pin pinA = getSnappedPin( siA );
		Pin pinB = getSnappedPin( siB );
		
		
		// Attach to junctions: TODO make it non-shit, add visual indicators, etc
		if (pinA == null) pinA = createJunctionPin( start );
		if (pinB == null) pinB = createJunctionPin( end   );
			
		Trace trace = Simulation.connectPins( pinA, pinB );
		
		EditorComponent ecom = new EditorComponentTrace( trace, tracePoints );
		getWorld().add( ecom );
		
		markHistoryChange( "Trace create" );
		repaint();
	}
	
	
	
	private Pin createJunctionPin( Vec2 pos ) {
		EditorComponentJunction junc = findJunction( pos );
		
		if (junc != null)
			return junc.getComponent().createPin();
		
		return null;
	}
	
	
	
	private EditorComponentJunction findJunction( Vec2 pos ) {
		double radius = 8;
		Bbox2  bbox   = new Bbox2( pos.subtract(radius), pos.add(radius) );
		
		for (EditorComponent ecom: getWorld().find( bbox )) {
			if (ecom instanceof EditorComponentJunction)
				return (EditorComponentJunction) ecom;
		}
		
		return null;
	}
	
	
	
	private class FindTraceEndPointResult {
		public boolean              foundEndPoint;
		public EditorComponentTrace ecom;
		public boolean              atStart; // else at end
		public Vec2                 pos;
	}
	
	
	
	private FindTraceEndPointResult findUnconnectedTraceEndpoint( Vec2 pos ) {
		FindTraceEndPointResult result = new FindTraceEndPointResult();
		
		double radius = 8;
		Bbox2  bbox   = new Bbox2( pos.subtract(radius), pos.add(radius) );
		
		for (EditorComponent ecom: getWorld().find( bbox )) {
			if (ecom instanceof EditorComponentTrace) {
				EditorComponentTrace trace = (EditorComponentTrace) ecom;
				
				Vec2    posStart = trace.getPosStart();
				Vec2    posEnd   = trace.getPosEnd();
				boolean atStart  = Geo.distance(posStart, pos) <= radius;
				boolean atEnd    = Geo.distance(posEnd,   pos) <= radius;
				
				result.foundEndPoint = atStart || atEnd;
				result.ecom          = trace;
				result.atStart       = atStart;
				result.pos           = (atStart) ? posStart : posEnd;
			}
		}
		
		return null;
	}
	
	
	
	private boolean isSnappedPinConnected( PinSnapInfo si ) {
		Pin pin = getSnappedPin( si );
		
		if (pin == null)
			return false;
		
		if (pin.hasTrace())
			return true;
		
		return false;
	}
	
	
	
	private Pin getSnappedPin( PinSnapInfo si ) {
		if ( ! si.snapped)
			return null;
		
		return SimMapper.getMappedPin( (EditorComponentActive) si.pinInfo.ecom, si.pinInfo.gpm );
	}
	
	
	
	private enum SnapType {
		pin,
		junction,
		trace
	}
	
	
	
	private class PinSnapInfo {
		public boolean snapped; // True if found a pin to snap onto
		public Vec2    pos;		// Endpoint of the pin, where the trace should attach
		public EditorWorld.FindClosestPinResult pinInfo;
	}
	
	
	
	private PinSnapInfo getPinSnapInfo( Vec2 pos ) {
		double      snapThresh  = 8 / getCamera().getZoom();
		PinSnapInfo pinSnapInfo = new PinSnapInfo();
		EditorWorld.FindClosestPinResult fcpRes = getWorld().findClosestPin( pos, snapThresh );
		
		if (fcpRes.foundPin) {
			pinSnapInfo.snapped = true;
			pinSnapInfo.pos     = fcpRes.gpm.getPinPosEnd();
			pinSnapInfo.pinInfo = fcpRes;
			
			if (isSnappedPinConnected( pinSnapInfo ))
				return new PinSnapInfo();
		}
		
		return pinSnapInfo;
	}
	
	
	
	private Vec2 angularSnap( Vec2 from, Vec2 to ) {
		double angle = Geo.angleBetween( from, to );
		double dist  = Geo.distance    ( from, to );
		
		angle = Geo.roundToMultiple( angle, 45 );
		
		return from.add( Geo.lenDir(dist, angle) );
	}
	
	
	
	private List<Vec2> breakLineToFitSnap( Vec2 from, Vec2 to ) {
		List<Vec2> list = new ArrayList<>();
		
		Vec2  snapped     = angularSnap( from, to );
		Line2 lineSnapped = new Line2( from, snapped );
		Line2.IntersectResult ir = findBestBreakIntersect( lineSnapped, to );
		
		list.add( from );
		
		if (ir != null)
			list.add( ir.pos );
		
		list.add( to );
		
		return list;
	}
	
	
	
	private Line2.IntersectResult findBestBreakIntersect( Line2 line, Vec2 radiant ) {
		Line2.IntersectResult bestIr = null;
		double bestDist = Double.MAX_VALUE; // Shorter is better
		
		for (double dir=0; dir<=360; dir+=45) {
			Line2 intersector = new Line2( radiant, radiant.addPolar(65536, dir) );
			Line2.IntersectResult ir = intersector.intersect( line );
			
			if (ir.intersects) {
				double dist = Geo.distance( ir.pos, line.a );
				
				if (dist < bestDist) {
					bestIr   = ir;
					bestDist = dist;
				}
			}
		}
		
		return bestIr;
	}
}

















































