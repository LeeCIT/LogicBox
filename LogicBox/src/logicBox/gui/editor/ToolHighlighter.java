


package logicBox.gui.editor;

import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import logicBox.util.Vec2;



/**
 * Indicates which component will be affected by user action.
 * It changes the colour and also redraws the component over any others.
 * @author Lee Coakley
 */
public class ToolHighlighter extends Tool
{
	private EditorPanel panel;
	private EditorWorld world;
	private Camera      cam;
	
	private EditorComponent lastComponent;
	private EditorComponent curComponent;
	private MouseAdapter    eventListener;
	private RepaintListener repaintListener;
	
	
	
	public ToolHighlighter( EditorPanel panel, EditorWorld world, Camera cam ) {
		this.panel           = panel;
		this.world           = world;
		this.cam             = cam;
		this.eventListener   = createEventListener();
		this.repaintListener = createRepaintListener();
	}
	
	
	
	public void attach() {
		if (isAttached())
			return;
		
		panel.addMouseMotionListener( eventListener );
		panel.addRepaintListener( repaintListener );
		setAttached( true );
	}
	
	
	
	public void detach() {
		if ( ! isAttached())
			return;
		
		panel.removeMouseMotionListener( eventListener );
		panel.removeRepaintListener( repaintListener );
		setAttached( false );
	}
	
	
	
	private MouseAdapter createEventListener() {
		return new MouseAdapter() {			
			public void mouseMoved( MouseEvent ev ) {
				doHighlight( cam.getMousePosWorld() );
			}
			
			public void mouseDragged( MouseEvent ev ) {
				doHighlight( cam.getMousePosWorld() );
			}
		};
	}
	
	
	
	private RepaintListener createRepaintListener() {
		return new RepaintListener() {
			public void draw( Graphics2D g ) {
				if (curComponent != null)
					curComponent.draw( g );
			}
		};
	}
	
	
	
	private void doHighlight( Vec2 pos ) {
		boolean changed = false;
		
		if (lastComponent != null) {
			lastComponent.graphic.setHighlighted( false );
			lastComponent = null;
			changed = true;
		}
		
		curComponent = world.findTopmostAt( pos );
		
		if (curComponent != null) {
			curComponent.graphic.setHighlighted( true );
			lastComponent = curComponent;
			changed = true;
		}
		
		if (changed)
			panel.repaint();
	}
}

















































