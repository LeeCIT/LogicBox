


package logicBox.gui.editor;

import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import logicBox.util.CallbackParam;
import logicBox.util.Vec2;



/**
 * Places components.
 * @author Lee Coakley
 */
public class ToolPlacer extends Tool
{
	private MouseAdapter    mouseListener;
	private RepaintListener repaintListener;
	
	private GraphicComActive    placementGraphic;
	private CallbackParam<Vec2> placementCallback;
	private boolean             placementInitiated;
	private boolean             placementArmed;
	private Vec2                placementPos;
	private double              placementAngle;
	
	
	
	public ToolPlacer( EditorPanel panel, EditorWorld world, Camera cam, ToolManager manager ) {
		super( panel, world, cam, manager );
		this.mouseListener   = createEventListener();
		this.repaintListener = createRepaintListener();
	}
	
	
	
	public void attach() {
		if (isAttached())
			return;
		
		panel.addMouseListener      ( mouseListener );
		panel.addMouseMotionListener( mouseListener );
		panel.addRepaintListener( repaintListener );
		setAttached( true );
	}
	
	
	
	public void detach() {
		if ( ! isAttached())
			return;
		
		panel.removeMouseListener      ( mouseListener );
		panel.removeMouseMotionListener( mouseListener );
		panel.removeRepaintListener( repaintListener );
		setAttached( false );
	}
	
	
	
	private MouseAdapter createEventListener() {
		return new MouseAdapter() {
			public void mousePressed( MouseEvent ev ) {
				if (placementInitiated)
				if (SwingUtilities.isLeftMouseButton( ev ))
					placementArm();
			}
			
			public void mouseReleased( MouseEvent ev ) {
				if ( ! placementInitiated)
					return;
				
				if (placementArmed)
				if (SwingUtilities.isLeftMouseButton( ev ))
					placementComplete( cam.getMousePosWorld() );
				
				if (SwingUtilities.isRightMouseButton( ev ))
					placementCancel();
			}
			
			public void mouseMoved( MouseEvent ev ) {
				if (placementInitiated)
					placementMove( cam.getMousePosWorld() );
			}
			
			public void mouseDragged( MouseEvent ev ) {
				if (placementInitiated)
					placementMove( cam.getMousePosWorld() );
			}
		};
	}
	
	
	
	private RepaintListener createRepaintListener() {
		return new RepaintListener() {
			public void draw( Graphics2D g ) {
				if (placementInitiated)
					placementGraphic.draw( g );
			}
		};
	}
	
	
	
	private void repaint() {
		panel.repaint();
	}
	
	
	
	/**
	 * Begin placing a component.
	 * @param createCallback What to do when the placement completes.  Note that it can be cancelled.
	 * @param graphic The graphic used to show where the component will be placed.  
	 * 				  The graphic will be modified by the tool, so don't reuse it.
	 */
	public void placementStart( GraphicComActive graphic, CallbackParam<Vec2> createCallback ) {
		placementCallback  = createCallback;
		placementGraphic   = graphic;
		placementInitiated = true;
		placementArmed     = false;
		
		graphic.transformTo( cam.getMousePosWorld(), 0 );
		repaint();
	}
	
	
	
	private void placementArm() {
		placementArmed = true;
		placementGraphic.setHighlighted( true );
		repaint();
	}
	
	
	
	private void placementComplete( Vec2 pos ) {
		placementPos = pos;
		placementCallback.execute( placementPos );
		repaint();
	}
	
	
	
	private void placementMove( Vec2 pos ) {
		placementPos = pos;
		placementGraphic.transformTo( placementPos, placementAngle );
		repaint();
	}
	
	
	
	private void placementCancel() {
		placementInitiated = false;
		placementArmed     = false;
		placementGraphic   = null;
		placementCallback  = null;
		placementPos       = null;
		repaint();
	}
}



















