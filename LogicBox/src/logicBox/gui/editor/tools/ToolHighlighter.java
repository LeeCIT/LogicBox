


package logicBox.gui.editor.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import logicBox.gui.editor.EditorComponent;
import logicBox.gui.editor.Graphic;
import logicBox.gui.editor.RepaintListener;



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
	
	
	
	public ToolHighlighter( ToolManager manager ) {
		super( manager );
		this.eventListener   = createEventListener();
		this.repaintListener = createRepaintListener();
	}
	
	
	
	public void attach() {
		if (isAttached())
			return;
		
		getEditorPanel().addMouseMotionListener( eventListener );
		getEditorPanel().addRepaintListener( repaintListener );
		setAttached( true );
	}
	
	
	
	public void detach() {
		if ( ! isAttached())
			return;
		
		getEditorPanel().removeMouseMotionListener( eventListener );
		getEditorPanel().removeRepaintListener( repaintListener );
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
		
		curComponent = getComponentAt( getMousePosWorld() );
		
		if (curComponent != null) {
			lastComponent = curComponent;
			changed = true;
		}
		
		if (changed)
			repaint();
	}
}

















































