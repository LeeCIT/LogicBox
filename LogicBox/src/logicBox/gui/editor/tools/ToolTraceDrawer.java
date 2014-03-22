


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
import java.util.Stack;
import logicBox.gui.Gfx;
import logicBox.gui.VecPath;
import logicBox.gui.editor.Camera;
import logicBox.gui.editor.EditorPanel;
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
	
	
	
	public ToolTraceDrawer( EditorPanel panel, EditorWorld world, Camera cam, ToolManager manager ) {
		super( panel, world, cam, manager );
		this.keyListener     = createKeyListener();
		this.mouseListener   = createMouseListener();
		this.repaintListener = createRepaintListener();
		this.tracePoints     = new Stack<>();
		this.tracePosNext    = new Vec2();
	}
	
	
	
	public void attach() {
		if (isAttached())
			return;
		
		panel.addKeyListener( keyListener );
		panel.addMouseListener      ( mouseListener );
		panel.addMouseMotionListener( mouseListener );
		panel.addRepaintListener( repaintListener );
		setAttached( true );
	}
	
	
	
	public void detach() {
		if ( ! isAttached())
			return;
		
		panel.removeKeyListener( keyListener );
		panel.removeMouseListener      ( mouseListener );
		panel.removeMouseMotionListener( mouseListener );
		panel.removeRepaintListener( repaintListener );
		setAttached( false );
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
		SnapInfo snapInfo  = getSnapInfo( cam.getMousePosWorld() );
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
			if (traceArmed)
				 Gfx.pushColorAndSet( g, EditorStyle.colHighlightStroke );
			else Gfx.pushColorAndSet( g, EditorStyle.colTraceOff );
			
				Line2 ref = new Line2( tracePoints.peek(), tracePosNext );
				Line2.IntersectResult ir = null;
			
				for (int i=0; i<tracePoints.size()-1; i++) {
					Line2 com = new Line2( tracePoints.get(i), tracePoints.get(i+1) );
					Line2.IntersectResult emir = ref.intersect( com );
					
					if (emir.intersects)
					if (Geo.distance( tracePoints.peek(), emir.pos) > 1.0/16.0 )
						ir = emir;
				}
				
				if (ir != null && ir.intersects) {
					drawOverlappedTrace( g, ref.a, ir.pos, ref.b );
				} else {
					VecPath polyLineVis = new VecPath();
					polyLineVis.moveTo( tracePoints.peek() );
					polyLineVis.lineTo( tracePosNext       );
					g.draw( polyLineVis );
				}
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
		
		Paint lastPaint = g.getPaint();
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
		
		g.setPaint( shadePaint );
		Gfx.pushStrokeAndSet( g, EditorStyle.strokePin );
		Gfx.drawArc( g, intersect, radius, angleA, angleB );
		Gfx.popStroke( g );
		g.setPaint( lastPaint );
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
					if (traceInitiated)
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
		panel.repaint();
	}
	
	
	
	private void traceArm() {
		traceArmed          = true;
		traceChoosingOrigin = ! traceInitiated;
		panel.repaint();
	}
	
	
	
	private void traceStart() {
		traceAdd();
		traceInitiated      = true;
		traceChoosingOrigin = false;
	}
	
	
	
	private void traceAdd() {
		traceArmed = false;
		
		Vec2    nextPos   = cam.getMousePosWorld();
		boolean completed = doTraceToPinSnapping( nextPos );
		
		tracePoints.push( nextPos );
		
		if (completed)
			traceComplete();
		
		panel.repaint();
	}
	
	
	
	/**
	 * Apply snap to Vec2 and update the source/dest info.  If true is returned the trace is completed.
	 * @return Whether the trace is now completed.
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
				
				System.out.println( "isSource:  " + isSource  );
				System.out.println( "isDest:    " + isDest    );
				System.out.println( "hasPoints: " + hasPoints );
				
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
			 panel.repaint();
		}
	}
	
	
	
	private void traceMove() {
		tracePosNext = getNextPos();
		panel.repaint();
	}
	
	
	
	private void traceComplete() {
		System.out.println( "Trace completed" );
		traceFinishCommon();
	}
	
	
	
	private void traceCancel() {
		traceFinishCommon();
	}
	
	
	
	private void traceFinishCommon() {
		traceInitiated      = false;
		traceChoosingOrigin = false;
		traceSrc            = null;
		traceDest           = null;
		tracePoints.clear();
		panel.repaint();
	}
	
	
	
	private Vec2 getNextPos() {
		Vec2     pos      = cam.getMousePosWorld();
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
		double   snapThresh = 8 / cam.getZoom();
		SnapInfo snapInfo   = new SnapInfo();
		EditorWorld.FindClosestPinResult fcpRes = world.findClosestPin( pos, snapThresh );
		
		if (fcpRes.foundPin) {
			snapInfo.snapped = true;
			snapInfo.pos     = fcpRes.gpm.getPinPosEnd();
			snapInfo.pinInfo = fcpRes;
		}
		
		return snapInfo;
	}
	
	
	
	private Vec2 getSnappedMousePos() {
		Vec2 pos = cam.getMousePosWorld();
		return pos;
		
		/** TODO worry about nice lines later.
		 * TODO insert a break so if a line is off, another is inserted to make it 45 degs
		if (traceInitiated && ! tracePoints.isEmpty())
			 return snap( tracePoints.peek(), pos );
		else return pos;
		*/
	}
	
	
	
	private Vec2 snap( Vec2 from, Vec2 to ) {
		double angle = Geo.angleBetween( from, to );
		double dist  = Geo.distance    ( from, to );
		
		angle = Geo.roundToMultiple( angle, 45 );
		
		return from.add( Geo.lenDir(dist, angle) );
	}
}

















































