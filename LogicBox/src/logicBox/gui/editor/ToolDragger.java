


package logicBox.gui.editor;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.SwingUtilities;
import logicBox.util.Geo;
import logicBox.util.Vec2;



/**
 * Drags components around.
 * How it's intended to work:
 *		- User holds down LMB
 *		- If they move the mouse more than a certain threshold, they begin dragging the topmost underlying component
 *      - If they now press RMB, the drag is cancelled
 *      - If they release LMD again, the drag is completed
 *      - They can pan and zoom at the same time.
 *      - Holding shift rotates the component.
 *      - Holding ctrl flips the component.
 * @author Lee Coakley
 */
public class ToolDragger extends Tool
{
	private EditorPanel     panel;
	private EditorWorld     world;
	private Camera          cam;
	private double          dragThreshold;
	private boolean         dragInitiated;
	private boolean         dragging;
	private Vec2            dragInitiatedAt;
	private Vec2            dragOffset;
	private EditorComponent draggedComponent;
	private double          rotateStartAngle;
	private MouseAdapter    eventListener;
	
	
	
	public ToolDragger( EditorPanel panel, EditorWorld world, Camera cam ) {
		this.panel         = panel;
		this.world         = world;
		this.cam           = cam;
		this.eventListener = createEventListener();
		this.dragThreshold = 4;
		
		createEventListener();
		attach();
	}
	
	
	
	private MouseAdapter createEventListener() {
		return new MouseAdapter() {
			public void mousePressed( MouseEvent ev ) {
				if (SwingUtilities.isLeftMouseButton( ev ))
					dragInitiate( cam.getMousePosWorld() );
				
				if (SwingUtilities.isRightMouseButton( ev ))
					dragCancel();
			}
			
			public void mouseReleased( MouseEvent ev ) {
				if (SwingUtilities.isLeftMouseButton( ev ))
					dragComplete();
			}
			
			public void mouseDragged( MouseEvent ev ) {
				Vec2 pos = cam.getMousePosWorld();
				
				if ( ! ev.isShiftDown())
					 dragMove  ( pos );
				else rotateMove( pos );
			}
		};
	}



	public void attach() {
		if (isAttached())
			return;
		
		panel.addMouseListener      ( eventListener );
		panel.addMouseMotionListener( eventListener );
		setAttached( true );
	}



	public void detach() {
		if ( ! isAttached())
			return;
		
		panel.removeMouseListener      ( eventListener );
		panel.removeMouseMotionListener( eventListener );
		setAttached( false );
	}
	
	
	
	private void dragInitiate( Vec2 pos ) {
		List<EditorComponent> list = world.find( pos );
		EditorComponent       ecom;
		
		if (list.isEmpty())
			return;
		else ecom = list.get( list.size() - 1 );
		
		dragInitiated    = true;
		dragInitiatedAt  = pos;
		dragOffset       = ecom.pos.subtract( pos );
		draggedComponent = ecom;
		rotateStartAngle = ecom.angle;
	}
	
	
	
	private void dragMove( Vec2 pos ) {
		if (dragInitiated) {
			if ( ! dragging) 
				if (Geo.distance(pos,dragInitiatedAt) >= dragThreshold) {
					dragging      = true;
					dragInitiated = false;
				}
		}
		
		if (dragging) {
			panel.setCursor( new Cursor(Cursor.MOVE_CURSOR) );
			world.move( draggedComponent, Geo.snapNear( pos.add( dragOffset ), 32 ) );
			panel.repaint();
		}
	}
	
	
	
	protected void rotateMove( Vec2 pos ) {
		if ( ! (dragInitiated || dragging))
			return;
		
		double angle   = Geo.angleBetween( pos, draggedComponent.pos );
		double snapped = Geo.roundToMultiple( angle, 45 );
		draggedComponent.angle = snapped - 180;
		panel.repaint();
	}
	
	
	
	private void dragComplete() {	
		dragFinishedCommon();
	}
	
	
	
	private void dragCancel() {
		world.move( draggedComponent, dragInitiatedAt.add(dragOffset) );
		draggedComponent.angle = rotateStartAngle;
		dragFinishedCommon();
	}
	
	
	
	private void dragFinishedCommon() {
		dragInitiated = false;
		dragging      = false;
		panel.setCursor( new Cursor(Cursor.DEFAULT_CURSOR) );
		panel.repaint();
	}
}



















