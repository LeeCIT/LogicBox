


package logicBox.gui.editor.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import logicBox.gui.Gfx;
import logicBox.gui.editor.EditorWorld;
import logicBox.gui.editor.GraphicJunction;
import logicBox.gui.editor.RepaintListener;
import logicBox.util.Vec2;



/**
 * Places junctions onto traces.
 * @author Lee Coakley
 */
public class ToolJunctionPlacer extends Tool
{
	private MouseAdapter    mouseListener;
	private RepaintListener repaintListener;
	
	private boolean placementInitiated;
	private boolean placementArmed;
	private Vec2    placementPos;
	
	
	
	public ToolJunctionPlacer( ToolManager manager ) {
		super( manager );
		this.mouseListener   = createEventListener();
		this.repaintListener = createRepaintListener();
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
		placementInitiated = false;
		placementArmed     = false;
	}
	
	
	
	private void setTransHint() {
		setTransHint(
		    "Left-click on a trace to insert a junction.\n" +
		    "Right-click to cancel."
		);
	}
	
	
	
	private MouseAdapter createEventListener() {
		return new MouseAdapter() {
			public void mousePressed( MouseEvent ev ) {
				if (placementInitiated)
				if (isLeft( ev ))
					placementArm();
			}
			
			
			public void mouseReleased( MouseEvent ev ) {
				if ( ! placementInitiated)
					return;
				
				if (placementArmed)
				if (isLeft( ev ))
					placementComplete();
				
				if (isRight( ev ))
					placementCancel();
			}
			
			
			public void mouseMoved( MouseEvent ev ) {
				if ( ! placementInitiated)
					return;
				
				placementMove();
			}
			
			
			public void mouseDragged( MouseEvent ev ) {
				mouseMoved( ev ); // Same thing
			}
		};
	}
	
	
	
	private RepaintListener createRepaintListener() {
		return new RepaintListener() {
			public void draw( Graphics2D g ) {
				if (placementInitiated)
					drawPlacementIndicator( g );
			}
		};
	}
	
	
	
	private void drawPlacementIndicator( Graphics2D g ) {
		EditorWorld.FindClosestTraceResult result = getWorld().findClosestTrace( getMousePosWorld(), 16 );
		
		if (result.foundTrace) {
			new GraphicJunction( result.closestPos ).draw( g );
			System.out.println( result.ecom );
		}
	}
	
	
	
	/**
	 * Begin placing a component.
	 * @param createCallback What to do when the placement completes.  Note that it can be cancelled.
	 * @param graphic The graphic used to show where the component will be placed.
	 */
	public void placementStart() {
		placementInitiated = true;
		placementArmed     = false;
		repaint();
	}
	
	
	
	private void placementArm() {
		placementArmed = true;
		repaint();
	}
	
	
	
	private void placementComplete() {
		placementPos = getSnappedMousePos();
		
		markHistoryChange( "Create junction" );
		repaint();
	}
	
	
	
	private void placementMove() {
		placementPos = getSnappedMousePos();
		repaint();
	}
	
	
	
	private void placementCancel() {
		placementInitiated = false;
		placementArmed     = false;
		placementPos       = null;
		
		getToolManager().releaseControl();
		repaint();
	}
	
	
	
	private Vec2 getSnappedMousePos() {
		return getMousePosWorld();
		//return Geo.snapNear( getMousePosWorld(), 16 );
	}
}



















