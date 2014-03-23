


package logicBox.gui.editor.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import logicBox.gui.Gfx;
import logicBox.gui.editor.GraphicComActive;
import logicBox.gui.editor.RepaintListener;
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
	
	
	
	public ToolPlacer( ToolManager manager ) {
		super( manager );
		this.mouseListener   = createEventListener();
		this.repaintListener = createRepaintListener();
	}
	
	
	
	public void attach() {
		if (isAttached())
			return;
		
		getEditorPanel().addMouseListener      ( mouseListener );
		getEditorPanel().addMouseMotionListener( mouseListener );
		getEditorPanel().addRepaintListener( repaintListener );
		setAttached( true );
	}
	
	
	
	public void detach() {
		if ( ! isAttached())
			return;
		
		getEditorPanel().removeMouseListener      ( mouseListener );
		getEditorPanel().removeMouseMotionListener( mouseListener );
		getEditorPanel().removeRepaintListener( repaintListener );
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
					placementComplete();
				
				if (SwingUtilities.isRightMouseButton( ev ))
					placementCancel();
			}
			
			public void mouseMoved( MouseEvent ev ) {
				if (placementInitiated)
					placementMove();
			}
			
			public void mouseDragged( MouseEvent ev ) {
				if (placementInitiated)
					placementMove();
			}
		};
	}
	
	
	
	private RepaintListener createRepaintListener() {
		return new RepaintListener() {
			public void draw( Graphics2D g ) {
				if (placementInitiated) {
					Gfx.pushCompositeAndSet( g, 0.5 );
						Gfx.drawOrientationOverlay( g, placementGraphic.getPos(), placementGraphic.getBbox().getBiggest()*1.4, placementGraphic.getAngle() );
						placementGraphic.draw( g );
					Gfx.popComposite( g );
				}
			}
		};
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
		
		graphic.transformTo( getMousePosWorld(), 0 );
		repaint();
	}
	
	
	
	private void placementArm() {
		placementArmed = true;
		placementGraphic.setHighlighted( true );
		repaint();
	}
	
	
	
	private void placementComplete() {
		placementPos = getMousePosWorld();
		placementCallback.execute( placementPos );
		placementGraphic.setHighlighted( false );
		repaint();
	}
	
	
	
	private void placementMove() {
		placementPos = getMousePosWorld();
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



















