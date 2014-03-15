


package logicBox.gui.editor;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import logicBox.gui.Gfx;
import logicBox.util.Bbox2;
import logicBox.util.Geo;
import logicBox.util.Vec2;



/**
 * Allows components to be selected.
 * Just a demo for now.
 * TODO deduplicate from ToolDragger: merge or make superclass
 * @author Lee Coakley
 */
public class ToolSelector extends Tool
{
	private double          dragThreshold;
	private boolean         dragInitiated;
	private boolean         dragging;
	private Vec2            dragInitiatedAt;
	private Vec2            dragPosNow;
	private MouseAdapter    eventListener;
	private RepaintListener repaintListener;
	
	
	
	public ToolSelector( EditorPanel panel, EditorWorld world, Camera cam, ToolManager manager ) {
		super( panel, world, cam, manager );
		this.eventListener   = createEventListener();
		this.repaintListener = createRepaintListener();
	}
	
	
	
	public void attach() {
		if (isAttached())
			return;
		
		panel.addMouseListener      ( eventListener );
		panel.addMouseMotionListener( eventListener );
		panel.addRepaintListener( repaintListener );
		setAttached( true );
	}



	public void detach() {
		if ( ! isAttached())
			return;
		
		panel.removeMouseListener      ( eventListener );
		panel.removeMouseMotionListener( eventListener );
		panel.removeRepaintListener( repaintListener );
		setAttached( false );
	}
	
	
	
	private MouseAdapter createEventListener() {
		return new MouseAdapter() {
			public void mousePressed( MouseEvent ev ) {
				if (SwingUtilities.isLeftMouseButton( ev ))
					dragInitiate( cam.getMousePosWorld() );
			}
			
			public void mouseReleased( MouseEvent ev ) {
				if (SwingUtilities.isLeftMouseButton( ev ))
					dragComplete();
			}
			
			public void mouseDragged( MouseEvent ev ) {
				dragMove( cam.getMousePosWorld() );
			}
		};
	}
	
	
	
	private RepaintListener createRepaintListener() {
		return new RepaintListener() {
			public void draw( Graphics2D g ) {
				if (dragging)
					drawSelection( g );
			}
		};
	}
	
	
	
	protected void drawSelection( Graphics2D g ) {
		double zoom      = cam.getZoom();
		float  thickness = (float) (EditorStyle.compThickness / zoom);
		double radius    = (int)   (12.0 / zoom);
		Bbox2  bbox      = getDragBbox();
		
		Gfx.pushStrokeAndSet( g, EditorStyle.makeSelectionStroke(thickness) );
			Gfx.pushColorAndSet( g, EditorStyle.colSelectionStroke );
				Gfx.drawRegionRounded( g, bbox, radius, false );
			Gfx.popColor( g );
		Gfx.popStroke( g );
	}
	
	
	
	private Bbox2 getDragBbox() {
		return Bbox2.createFromPoints( dragInitiatedAt, dragPosNow );
	}
	
	
	
	private void dragInitiate( Vec2 pos ) {		
		EditorComponent ecom = world.findTopmostAt( pos );
		
		if (ecom != null)
			return;
		
		dragInitiated   = true;
		dragInitiatedAt = pos;
	}
	
	
	
	private void dragMove( Vec2 pos ) {
		if (dragInitiated) {
			if ( ! dragging) 
				if (isDragThresholdMet(pos)) {
					dragging      = true;
					dragInitiated = false;
				}
		}
		
		if (dragging) {
			dragPosNow = pos;
			panel.repaint();
		}
	}
	
	
	
	private boolean isDragThresholdMet( Vec2 pos ) {
		return Geo.distance(pos,dragInitiatedAt) >= (dragThreshold / cam.getZoom());
	}
	
	
	
	private void dragComplete() {
		for (EditorComponent ecom: world.find( getDragBbox() ))
			ecom.graphic.setSelected( true );
		
		dragInitiated = false;
		dragging      = false;
		panel.setCursor( new Cursor(Cursor.DEFAULT_CURSOR) );
		panel.repaint();
	}
}



















