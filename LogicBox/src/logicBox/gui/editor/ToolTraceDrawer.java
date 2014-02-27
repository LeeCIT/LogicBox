


package logicBox.gui.editor;

import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;
import javax.swing.SwingUtilities;
import logicBox.gui.Gfx;
import logicBox.gui.VecPath;
import logicBox.util.Geo;
import logicBox.util.Vec2;



/**
 * Allows drawing of traces.
 * Manipulating them and integrating them into the world will be separate.
 * @author Lee Coakley
 */
public class ToolTraceDrawer extends Tool
{
	private EditorPanel panel;
	private EditorWorld world;
	private Camera      cam;
	
	private boolean         traceInitiated;
	private boolean         traceChoosingOrigin;
	private boolean         traceArmed;
	private Stack<Vec2>     tracePoints;
	private Vec2            tracePosNext;
	private KeyAdapter      keyListener;
	private MouseAdapter    mouseListener;
	private RepaintListener repaintListener;
	
	
	
	public ToolTraceDrawer( EditorPanel panel, EditorWorld world, Camera cam ) {
		this.panel           = panel;
		this.world           = world;
		this.cam             = cam;
		this.keyListener     = createKeyListener();
		this.mouseListener   = createMouseListener();
		this.repaintListener = createRepaintListener();
		this.tracePoints     = new Stack<>();
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
		if (traceChoosingOrigin)
			Gfx.drawCircle( g, tracePosNext, 5, EditorStyle.colHighlightStroke, true );
		
		if ( (!traceInitiated) || tracePoints.isEmpty())
			return;
		
		Gfx.drawCircle( g, tracePosNext, 5, EditorStyle.colTraceOff, true );
		
		Gfx.pushStrokeAndSet( g, EditorStyle.strokeTrace );
			Gfx.pushColorAndSet ( g, EditorStyle.colTraceOff );
				drawExistingLines( g );
				drawNextLine( g );
			Gfx.popColor ( g );
		Gfx.popStroke( g );
	}
	
	
	
	private void drawNextLine( Graphics2D g ) {
		Gfx.pushStrokeAndSet( g, EditorStyle.strokeTracePlace );
			if (traceArmed)
				 Gfx.pushColorAndSet( g, EditorStyle.colHighlightStroke );
			else Gfx.pushColorAndSet( g, EditorStyle.colTraceOff );
			
				VecPath polyLineVis = new VecPath();
				polyLineVis.moveTo( tracePoints.peek() );
				polyLineVis.lineTo( tracePosNext       );
				g.draw( polyLineVis );
			Gfx.popColor ( g );
		Gfx.popStroke( g );
	}
	
	
	
	private void drawExistingLines( Graphics2D g ) {
		VecPath polyLine = new VecPath();
		polyLine.moveTo( tracePoints.get(0) );
		
		for (int i=1; i<tracePoints.size(); i++)
			polyLine.lineTo( tracePoints.get(i) );
			
		g.draw( polyLine );
	}
	
	
	
	private MouseAdapter createMouseListener() {
		return new MouseAdapter() {
			public void mousePressed( MouseEvent ev ) {
				if (SwingUtilities.isLeftMouseButton( ev ))
					if ( ! traceInitiated)
						 traceStartChoosingOrigin();
					else traceArm();
			}
			
			
			public void mouseReleased( MouseEvent ev ) {
				if (SwingUtilities.isLeftMouseButton( ev ))
					if ( ! traceInitiated) 
						 traceStart();
					else traceAdd  ();
				
				if (SwingUtilities.isRightMouseButton( ev ))
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
	
	
	
	private void traceStartChoosingOrigin() {
		traceChoosingOrigin = true;
		tracePosNext        = getSnappedMousePos();
		panel.repaint();
	}
	
	
	
	private void traceArm() {
		traceArmed          = true;
		traceChoosingOrigin = ! traceInitiated;
		panel.repaint();
	}
	
	
	
	private void traceStart() {
		traceInitiated      = true;
		traceChoosingOrigin = false;
		tracePosNext        = getSnappedMousePos();
		tracePoints.push( tracePosNext );
		panel.repaint();
	}
	
	
	
	private void traceAdd() {
		traceArmed = false;
		tracePoints.push( getSnappedMousePos() );
		panel.repaint();
	}
	
	
	
	private void traceUndo() {
		if ( ! tracePoints.isEmpty()) {
			 tracePoints.pop();
			 panel.repaint();
		}
	}
	
	
	
	private void traceCancel() {
		traceInitiated      = false;
		traceChoosingOrigin = false;
		tracePoints.clear();
		panel.repaint();
	}
	
	
	
	private void traceMove() {
		tracePosNext = getSnappedMousePos();
		panel.repaint();
	}
	
	
	
	private Vec2 getSnappedMousePos() {
		Vec2 pos = cam.getMousePosWorld();
		
		if (traceInitiated && ! tracePoints.isEmpty())
			 return snap( tracePoints.peek(), pos );
		else return pos;
	}
	
	
	
	private Vec2 snap( Vec2 from, Vec2 to ) {
		double angle = Geo.angleBetween( from, to );
		double dist  = Geo.distance    ( from, to );
		
		angle = Geo.roundToMultiple( angle, 45 );
		
		return from.add( Geo.lenDir(dist, angle) );
	}
}

















































