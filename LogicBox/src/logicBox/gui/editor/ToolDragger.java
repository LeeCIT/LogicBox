


package logicBox.gui.editor;

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
 *		- If they move the mouse more than a certain threshold, they begin dragging any underlying component
 *      - If they now press RMB, the drag is cancelled
 *      - If they release LMD again, the drag is completed
 *      - They can pan and zoom at the same time.
 *      - Holding shift rotates the component.
 *      - Holding ctrl flips the component.
 * @author Lee Coakley
 */
public class ToolDragger
{
	private double dragThreshold;
	
	private EditorPanel panel;
	private EditorWorld world;
	private Camera      cam;
	
	private boolean         dragInitiated;
	private boolean         dragging;
	private Vec2            dragInitiatedAt;
	private Vec2            dragOffset;
	private EditorComponent draggedComponent;
	
	
	
	public ToolDragger( EditorPanel panel, EditorWorld world, Camera cam ) {
		this.panel = panel;
		this.world = world;
		this.cam   = cam;
		
		dragThreshold = 4;
		
		setupActions();
	}
	
	
	
	private void setupActions() {
		MouseAdapter adapter = new MouseAdapter() {
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
			
			public void mouseMoved( MouseEvent ev ) {
				dragMove( cam.getMousePosWorld() );
			}
			
			public void mouseDragged( MouseEvent ev ) {
				dragMove( cam.getMousePosWorld() );
			}
		};
		
		panel.addMouseListener      ( adapter );
		panel.addMouseMotionListener( adapter );
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
			world.move( draggedComponent, Geo.snapNear( pos.add( dragOffset ), 64 ) );
			panel.repaint();
		}
	}
	
	
	
	private void dragComplete() {	
		System.out.println( "drag complete" );
		dragInitiated = false;
		dragging      = false;
		panel.repaint();
	}
	
	
	
	private void dragCancel() {
		if ( ! dragging)
			return;
		
		world.move( draggedComponent, dragInitiatedAt.add(dragOffset) );
		
		dragging = false;
		panel.repaint();
	}
}



















