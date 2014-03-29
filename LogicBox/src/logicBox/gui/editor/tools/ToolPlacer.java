


package logicBox.gui.editor.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import logicBox.gui.Gfx;
import logicBox.gui.editor.EditorCreationParam;
import logicBox.gui.editor.EditorStyle;
import logicBox.gui.editor.GraphicComActive;
import logicBox.gui.editor.RepaintListener;
import logicBox.util.CallbackParam;
import logicBox.util.Geo;
import logicBox.util.Vec2;



/**
 * Places components.
 * @author Lee Coakley
 */
public class ToolPlacer extends Tool
{
	private MouseAdapter    mouseListener;
	private RepaintListener repaintListener;
	
	private CallbackParam<EditorCreationParam> placementCallback;
	private GraphicComActive placementGraphic;
	private boolean          placementInitiated;
	private boolean          placementArmed;
	private Vec2             placementPos;
	private double           placementAngle;
	
	
	
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
		placementGraphic   = null;
		placementCallback  = null;
		placementInitiated = false;
		placementArmed     = false;
	}
	
	
	
	private void setTransHint() {
		setTransHint(
		    "Left-click to place.\n" +
		    "Right-click to cancel.\n" +
		    "Hold down shift key to rotate."
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
				
				if (ev.isShiftDown())
					 placementRotate();
				else placementMove();
			}
			
			
			public void mouseDragged( MouseEvent ev ) {
				mouseMoved( ev ); // Same thing
			}
		};
	}
	
	
	
	private RepaintListener createRepaintListener() {
		return new RepaintListener() {
			public void draw( Graphics2D g ) {
				if (placementInitiated) {
					drawOverlay( g );
					
					Gfx.pushCompositeAndSet( g, 0.5 );
						placementGraphic.draw( g );
					Gfx.popComposite( g );
				}
			}
		};
	}
	
	
	
	private void drawOverlay( Graphics2D g ) {
		Vec2   pos    = placementGraphic.getPos();
		double angle  = placementGraphic.getAngle();
		double radius = placementGraphic.getBbox().getEnclosingRadius() * 1.5;
		
		Gfx.pushColorAndSet( g, EditorStyle.colSelectionStroke );
			Gfx.drawOrientationOverlay( g, pos, radius, angle );
		Gfx.popColor( g );
	}
	
	
	
	/**
	 * Begin placing a component.
	 * @param createCallback What to do when the placement completes.  Note that it can be cancelled.
	 * @param graphic The graphic used to show where the component will be placed.  
	 * 				  The graphic will be modified by the tool, so don't reuse it.
	 */
	public void placementStart( GraphicComActive graphic, CallbackParam<EditorCreationParam> createCallback ) {
		placementCallback  = createCallback;
		placementGraphic   = graphic;
		placementInitiated = true;
		placementArmed     = false;
		
		graphic.transformTo( getSnappedMousePos(), 0 );
		repaint();
	}
	
	
	
	private void placementArm() {
		placementArmed = true;
		placementGraphic.setHighlighted( true );
		repaint();
	}
	
	
	
	private void placementComplete() {
		placementPos = getSnappedMousePos();
		EditorCreationParam param = new EditorCreationParam( placementPos, placementAngle );
		placementCallback.execute( param );
		markHistoryChange();
		placementGraphic.setHighlighted( false );
		repaint();
	}
	
	
	
	private void placementMove() {
		placementPos = getSnappedMousePos();
		placementGraphic.transformTo( placementPos, placementAngle );
		repaint();
	}
	
	
	
	private void placementRotate() {
		Vec2   pos     = getMousePosWorld();
		double angle   = Geo.angleBetween( placementPos, pos );
		double snapped = Geo.roundToMultiple( angle, 45 );
		
		placementAngle = snapped;
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
		
		getToolManager().releaseControl();
	}
	
	
	
	private Vec2 getSnappedMousePos() {
		return Geo.snapNear( getMousePosWorld(), 16 );
	}
}



















