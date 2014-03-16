


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
 * Handles dragging, rotating and selection.
 * @author Lee Coakley
 */
public class ToolContextual extends Tool
{
	private double  dragThreshold;
	private boolean dragInitiated;
	private boolean dragging;
	private Vec2    dragInitiatedAt;
	private Vec2    dragOffset;
	
	private Selection selection;
	private boolean   selectInitiated;
	private boolean   selecting;
	private Vec2      selectInitiatedAt;
	private Vec2      selectPosNow;
	
	private double    rotateStartAngle;
	
	private MouseAdapter    dragListener;
	private MouseAdapter    selectListener;
	private RepaintListener repaintListener;
	
	
	
	public ToolContextual( EditorPanel panel, EditorWorld world, Camera cam, ToolManager manager ) {
		super( panel, world, cam, manager );
		this.selection       = new Selection();
		this.dragListener    = createDragListener();
		this.selectListener  = createSelectListener();
		this.repaintListener = createRepaintListener();
		this.dragThreshold   = 4;
	}
	
	
	
	public void attach() {
		if (isAttached())
			return;
		
		panel.addMouseListener      ( dragListener );
		panel.addMouseMotionListener( dragListener );
		panel.addMouseListener      ( selectListener );
		panel.addMouseMotionListener( selectListener );
		panel.addRepaintListener( repaintListener );
		setAttached( true );
	}
	
	
	
	public void detach() {
		if ( ! isAttached())
			return;
		
		panel.removeMouseListener      ( dragListener );
		panel.removeMouseMotionListener( dragListener );
		panel.removeMouseListener      ( selectListener );
		panel.removeMouseMotionListener( selectListener );
		panel.removeRepaintListener( repaintListener );
		setAttached( false );
	}
	
	
	
	private MouseAdapter createDragListener() {
		return new MouseAdapter() {
			public void mousePressed( MouseEvent ev ) {
				if (isLeft(ev))
					dragInitiate();
				
				if (isRight(ev))
					dragCancel();
			}
			
			
			public void mouseReleased( MouseEvent ev ) {
				if (isLeft(ev))
					dragComplete();
			}
			
			
			public void mouseDragged( MouseEvent ev ) {
				if ( ! ev.isShiftDown())
					 dragMove  ();
				else rotateMove();
			}
		};
	}
	
	
	
	private MouseAdapter createSelectListener() {
		return new MouseAdapter() {
			public void mousePressed( MouseEvent ev ) {
				if (isLeft(ev))
					selectInitiate();
				
				if (isRight(ev))
					selectCancel();
			}
			
			
			public void mouseReleased( MouseEvent ev ) {
				if (isLeft(ev))
					selectComplete();
			}
			
			
			public void mouseDragged( MouseEvent ev ) {
				selectMove();
			}
		};
	}
	
	
	
	private boolean isLeft( MouseEvent ev ) {
		return SwingUtilities.isLeftMouseButton( ev );
	}
	
	
	
	private boolean isRight( MouseEvent ev ) {
		return SwingUtilities.isRightMouseButton( ev );
	}
	
	
	
	private boolean isDragThresholdMet( Vec2 origin, Vec2 now ) {
		return Geo.distance(origin,now) >= (dragThreshold / cam.getZoom());
	}
	
	
	
	private boolean isComponentPresent( Vec2 pos ) {
		return null != world.findTopmostAt( pos );
	}
	
	
	
	private void dragInitiate() {
		Vec2            pos  = cam.getMousePosWorld();
		EditorComponent ecom = world.findTopmostAt( pos );
		
		if (ecom == null)
			return;
		
		dragInitiated    = true;
		dragInitiatedAt  = pos;
		
		selection.clear();
		selection.add( ecom );
		
		dragOffset       = selection.getPos().subtract( pos );
		rotateStartAngle = selection.getAngle();
	}
	
	
	
	private void dragMove() {
		Vec2 pos = cam.getMousePosWorld();
		
		if (dragInitiated) {
			if ( ! dragging) 
				if (isDragThresholdMet(dragInitiatedAt,pos)) {
					dragging      = true;
					dragInitiated = false;
				}
		}
		
		if (dragging) {
			panel.setCursor( new Cursor(Cursor.MOVE_CURSOR) );
			//world.move( draggedComponent, Geo.snapNear( pos.add( dragOffset ), 32 ) ); // TODO snap
			selection.setPos( pos.add(dragOffset) );
			panel.repaint();
		}
	}
	
	
	
	private void rotateMove() {
		if ( ! (dragInitiated || dragging))
			return;
		
		panel.setCursor( new Cursor(Cursor.DEFAULT_CURSOR) );
		
		Vec2   pos     = cam.getMousePosWorld();
		double angle   = Geo.angleBetween( selection.getPos(), pos );
		double snapped = Geo.roundToMultiple( angle, 45 );
		selection.setAngle( snapped );
		panel.repaint();
	}
	
	
	
	private void dragComplete() {	
		dragFinishedCommon();
	}
	
	
	
	private void dragCancel() {
		boolean wasDragging = dragging;
		
		dragFinishedCommon();
		
		if ( ! wasDragging)
			return;
		
		selection.setPos  ( dragInitiatedAt.add(dragOffset) );
		selection.setAngle( rotateStartAngle );
	}
	
	
	
	private void dragFinishedCommon() {
		dragInitiated = false;
		dragging      = false;
		panel.setCursor( new Cursor(Cursor.DEFAULT_CURSOR) );
		panel.repaint();
	}
	
	
	
	private RepaintListener createRepaintListener() {
		return new RepaintListener() {
			public void draw( Graphics2D g ) {
				if (selecting)
					drawSelectionBox( g );
			}
		};
	}
	
	
	
	private void drawSelectionBox( Graphics2D g ) {
		double zoom      = cam.getZoom();
		float  thickness = (float) (EditorStyle.compThickness / zoom);
		double radius    = (int)   (8.0 / zoom);
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
	
	
	
	private void selectInitiate() {
		selectInitiated   = true;
		selectInitiatedAt = cam.getMousePosWorld();
	}
	
	
	
	private void selectMove() {
		Vec2 pos = cam.getMousePosWorld();
		
		if (selectInitiated) {
			if ( ! selecting) 
				if (isDragThresholdMet(selectInitiatedAt,pos)) {
					selecting       = true;
					selectInitiated = false;
				}
		}
		
		if (selecting) {
			selectPosNow = pos;
			panel.repaint();
		}
	}
	
	
	
	private void selectComplete() {
		for (EditorComponent ecom: world.find( getSelectBbox() ))
			selection.add( ecom );
		
		selectFinishedCommon();
	}
	
	
	
	private void selectCancel() {
		selectFinishedCommon();
	}
	
	
	
	private void selectFinishedCommon() {
		selectInitiated = false;
		selecting       = false;
		panel.setCursor( new Cursor(Cursor.DEFAULT_CURSOR) );
		panel.repaint();
	}
}



















