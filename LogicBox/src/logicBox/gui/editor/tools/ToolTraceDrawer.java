


package logicBox.gui.editor.tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import logicBox.gui.Gfx;
import logicBox.gui.VecPath;
import logicBox.gui.editor.EditorStyle;
import logicBox.gui.editor.EditorWorld;
import logicBox.gui.editor.GraphicPinMapping;
import logicBox.gui.editor.RepaintListener;
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
	private KeyAdapter      keyListener;
	private MouseAdapter    mouseListener;
	private RepaintListener repaintListener;
	
	private EditorWorld.FindClosestPinResult traceSrc;
	private EditorWorld.FindClosestPinResult traceDest;
	
	
	
	public ToolTraceDrawer( ToolManager manager ) {
		super( manager );
		this.keyListener     = createKeyListener();
		this.mouseListener   = createMouseListener();
		this.repaintListener = createRepaintListener();
		this.tracePoints     = new Stack<>();
		this.tracePosNext    = new Vec2();
	}
	
	
	
	public void attach() {
		if (isAttached())
			return;
		
		getEditorPanel().addKeyListener( keyListener );
		getEditorPanel().addMouseListener      ( mouseListener );
		getEditorPanel().addMouseMotionListener( mouseListener );
		getEditorPanel().addRepaintListener( repaintListener );
		setAttached( true );
	}
	
	
	
	public void detach() {
		if ( ! isAttached())
			return;
		
		getEditorPanel().removeKeyListener( keyListener );
		getEditorPanel().removeMouseListener      ( mouseListener );
		getEditorPanel().removeMouseMotionListener( mouseListener );
		getEditorPanel().removeRepaintListener( repaintListener );
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
	
	
	
	private KeyAdapter createKeyListener() {
		return new KeyAdapter() {
			public void keyReleased( KeyEvent ev ) {
				if (ev.getKeyCode() == KeyEvent.VK_BACK_SPACE) // TODO doesn't work; use swing keybindings api
					if (traceInitiated)
						traceUndo();
			}
		};
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
		SnapInfo snapInfo  = getSnapInfo( getMousePosWorld() );
		double   thickness = EditorStyle.compThickness + 2;
		
		Gfx.pushColorAndSet( g, EditorStyle.colHighlightStroke );
			if (snapInfo.snapped)
				Gfx.drawThickRoundedLine( g, snapInfo.pinInfo.gpm.line, thickness );
			
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
	
	
	
	private void drawOverlappedTrace( Graphics2D g, Vec2 a, Vec2 intersect, Vec2 b ) {
		double radius = EditorStyle.compThickness * 2;
		double angleB = Geo.angleBetween( a, b );
		double angleA = angleB + 180;
		Vec2   a2i    = Geo.lenDir(radius,angleA).add( intersect );
		Vec2   b2i    = Geo.lenDir(radius,angleB).add( intersect );
		
		Color   shade = Geo.lerp( g.getColor(), new Color(0,255,0), 0.5 );
		float[] fracs = { 0.0f, 0.5f, 1.0f };
		Color[] cols  = { EditorStyle.colTraceOff, shade, EditorStyle.colTraceOff };
		Paint shadePaint = new LinearGradientPaint( a2i, b2i, fracs, cols, CycleMethod.NO_CYCLE );
		
		VecPath poly = new VecPath();
		poly.moveTo( a   );
		poly.lineTo( a2i );
		poly.moveTo( b2i );
		poly.lineTo( b   );
		
		g.draw( poly );
		
		Gfx.pushPaintAndSet( g, shadePaint );
			Gfx.pushStrokeAndSet( g, EditorStyle.strokePin );
				Gfx.drawArc( g, intersect, radius, angleA, angleB );
			Gfx.popStroke( g );
		Gfx.popPaint( g );
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
					if ( ! traceInitiated) 
						 traceStart();
					else traceAdd  ();
				
				if (isRight( ev ))
					traceCancel();
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
		
		if (tracePoints.isEmpty()) {
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
	
	
	
	/**
	 * Apply snap to Vec2 and update the source/dest info.
	 * @return Whether the trace is now completed (attached to a component)
	 * TODO it's also possible to join a trace onto another trace.
	 */
	private boolean doTraceToPinSnapping( Vec2 nextPos ) {
		SnapInfo snapInfo  = getSnapInfo( nextPos );
		boolean  completed = false;
		
		if (snapInfo.snapped) {
			boolean dupe = isGpmUsed( snapInfo.pinInfo.gpm );
			
			if ( ! dupe) {
				nextPos.setLocation( snapInfo.pos );
				
				boolean hasPoints = ! tracePoints.isEmpty(); 
				boolean isSource  = (traceChoosingOrigin && traceSrc == null);
				boolean isDest    = (hasPoints);
				
				if (isSource) {
					traceSrc = snapInfo.pinInfo;
				} 
				else if (isDest) {
					traceDest = snapInfo.pinInfo;
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
	
	
	
	private void traceUndo() {
		if ( ! tracePoints.isEmpty()) {
			 tracePoints.pop();
			 repaint();
		}
	}
	
	
	
	private void traceMove() {
		tracePosNext = getNextPos();
		repaint();
	}
	
	
	
	private void traceComplete() {
		System.out.println( "Trace completed" );
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
		Vec2     pos      = getMousePosWorld();
		SnapInfo snapInfo = getSnapInfo( pos );
		boolean  useSnap  = snapInfo.snapped && ! isGpmUsed(snapInfo.pinInfo.gpm);
		
		if (useSnap)
			 return snapInfo.pos;
		else return pos;
	}
	
	
	
	private class SnapInfo {
		public boolean snapped;
		public Vec2    pos;
		public EditorWorld.FindClosestPinResult pinInfo;
	}
	
	
	
	private SnapInfo getSnapInfo( Vec2 pos ) {
		double   snapThresh = 8 / getCamera().getZoom();
		SnapInfo snapInfo   = new SnapInfo();
		EditorWorld.FindClosestPinResult fcpRes = getWorld().findClosestPin( pos, snapThresh );
		
		if (fcpRes.foundPin) {
			snapInfo.snapped = true;
			snapInfo.pos     = fcpRes.gpm.getPinPosEnd();
			snapInfo.pinInfo = fcpRes;
		}
		
		return snapInfo;
	}
	
	
	
	private Vec2 snap( Vec2 from, Vec2 to ) {
		double angle = Geo.angleBetween( from, to );
		double dist  = Geo.distance    ( from, to );
		
		angle = Geo.roundToMultiple( angle, 45 );
		
		return from.add( Geo.lenDir(dist, angle) );
	}
	
	
	
	private List<Vec2> breakLineToFitSnap( Vec2 from, Vec2 to ) {
		List<Vec2> list = new ArrayList<>();
		
		Vec2  snapped     = snap( from, to );
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

















































