


package logicBox.gui.editor.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import logicBox.gui.editor.Camera;
import logicBox.gui.editor.EditorComponent;
import logicBox.gui.editor.EditorPanel;
import logicBox.gui.editor.EditorWorld;
import logicBox.gui.editor.Graphic;
import logicBox.gui.editor.RepaintListener;
import logicBox.util.Vec2;



/**
 * Indicates which component will be affected by user action.
 * It changes the colour and also redraws the component over any others.
 * @author Lee Coakley
 */
public class ToolHighlighter extends Tool
{
	private EditorComponent lastComponent;
	private EditorComponent curComponent;
	private MouseAdapter    eventListener;
	private RepaintListener repaintListener;
	
	
	
	public ToolHighlighter( EditorPanel panel, EditorWorld world, Camera cam, ToolManager manager ) {
		super( panel, world, cam, manager );
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
				doHighlight();
			}
			
			public void mouseDragged( MouseEvent ev ) {
				doHighlight();
			}
		};
	}
	
	
	
	private RepaintListener createRepaintListener() {
		return new RepaintListener() {
			public void draw( Graphics2D g ) {
				if (curComponent != null) {
					Graphic graphic = curComponent.getGraphic();
					boolean state   = graphic.isHighlighted();
					
					graphic.setHighlighted( true );
					graphic.draw( g );
					graphic.setHighlighted( state );
				}
			}
		};
	}
	
	
	
	private void doHighlight() {
		boolean changed = false;
		
		if (lastComponent != null) {
			lastComponent = null;
			changed = true;
		}
		
		curComponent = getComponentAt( cam.getMousePosWorld() );
		
		if (curComponent != null) {
			lastComponent = curComponent;
			changed = true;
		}
		
		if (changed)
			panel.repaint();
	}
}

















































