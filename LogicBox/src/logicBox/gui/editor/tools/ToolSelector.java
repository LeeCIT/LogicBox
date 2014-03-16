


package logicBox.gui.editor.tools;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import logicBox.gui.Gfx;
import logicBox.gui.editor.Camera;
import logicBox.gui.editor.EditorComponent;
import logicBox.gui.editor.EditorPanel;
import logicBox.gui.editor.EditorStyle;
import logicBox.gui.editor.EditorWorld;
import logicBox.gui.editor.RepaintListener;
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
	private boolean         selectInitiated;
	private boolean         selecting;
	private Vec2            selectInitiatedAt;
	private Vec2            selectPosNow;
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
					selectInitiate( cam.getMousePosWorld() );
			}
			
			public void mouseReleased( MouseEvent ev ) {
				if (SwingUtilities.isLeftMouseButton( ev ))
					selectComplete();
			}
			
			public void mouseDragged( MouseEvent ev ) {
				selectMove( cam.getMousePosWorld() );
			}
		};
	}
	
	
	
	private RepaintListener createRepaintListener() {
		return new RepaintListener() {
			public void draw( Graphics2D g ) {
				if (selecting)
					drawSelection( g );
			}
		};
	}
	
	
	
	private void drawSelection( Graphics2D g ) {
		double zoom      = cam.getZoom();
		float  thickness = (float) (EditorStyle.compThickness / zoom);
		double radius    = (int)   (12.0 / zoom);
		Bbox2  bbox      = getSelectBbox();
		
		Gfx.pushStrokeAndSet( g, EditorStyle.makeSelectionStroke(thickness) );
			Gfx.pushColorAndSet( g, EditorStyle.colSelectionStroke );
				Gfx.drawRegionRounded( g, bbox, radius, false );
			Gfx.popColor( g );
		Gfx.popStroke( g );
	}
	
	
	
	private Bbox2 getSelectBbox() {
		return Bbox2.createFromPoints( selectInitiatedAt, selectPosNow );
	}
	
	
	
	private void selectInitiate( Vec2 pos ) {		
		EditorComponent ecom = world.findTopmostAt( pos );
		
		if (ecom != null)
			return;
		
		selectInitiated   = true;
		selectInitiatedAt = pos;
	}
	
	
	
	private void selectMove( Vec2 pos ) {
		if (selectInitiated) {
			if ( ! selecting) 
				if (isDragThresholdMet(pos)) {
					selecting      = true;
					selectInitiated = false;
				}
		}
		
		if (selecting) {
			selectPosNow = pos;
			panel.repaint();
		}
	}
	
	
	
	private boolean isDragThresholdMet( Vec2 pos ) {
		return Geo.distance(pos,selectInitiatedAt) >= (dragThreshold / cam.getZoom());
	}
	
	
	
	private void selectComplete() {
		for (EditorComponent ecom: world.find( getSelectBbox() ))
			ecom.getGraphic().setSelected( true );
		
		selectInitiated = false;
		selecting      = false;
		panel.setCursor( new Cursor(Cursor.DEFAULT_CURSOR) );
		panel.repaint();
	}
}



















