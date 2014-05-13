


package logicBox.gui.editor.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import logicBox.gui.editor.EditorComponent;
import logicBox.gui.editor.EditorWorld.FindClosestPinResult;
import logicBox.gui.editor.graphics.EditorStyle;
import logicBox.gui.editor.graphics.Graphic;
import logicBox.gui.editor.graphics.RepaintListener;
import logicBox.sim.component.Component;
import logicBox.sim.component.ComponentActive;
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
	
	
	
	public ToolHighlighter( ToolManager manager ) {
		super( manager );
		this.eventListener   = createEventListener();
		this.repaintListener = createRepaintListener();
	}
	
	
	
	public void attach() {
		if (isAttached())
			return;
		
		getEditorPanel().addMouseListener      ( eventListener );
		getEditorPanel().addMouseMotionListener( eventListener );
		getEditorPanel().addWorldRepaintListener( repaintListener );
		setAttached( true );
	}
	
	
	
	public void detach() {
		if ( ! isAttached())
			return;
		
		getEditorPanel().removeMouseListener      ( eventListener );
		getEditorPanel().removeMouseMotionListener( eventListener );
		getEditorPanel().removeWorldRepaintListener( repaintListener );
		setAttached( false );
	}
	
	
	
	public void reset() {
		lastComponent = null;
		curComponent  = null;
		removeTransHint();
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
			removeTransHint();
		}
		
		curComponent = getComponentAt( getMousePosWorld() );
		
		if (curComponent != null) {
			lastComponent = curComponent;
			changed = true;
			setTransHint( curComponent, getMousePosWorld() );
		}
		
		if (changed)
			repaint();
	}
	
	
	
	protected void setTransHint( EditorComponent ecom, Vec2 pos ) {
		if (ecom == null)
			return;
		
		String               str    = ecom.getComponentName();
		FindClosestPinResult result = getWorld().findClosestPin( pos, EditorStyle.compThickness * 0.5 );
		
		if (result.foundPin) {
			Component com = ecom.getComponent();
			
			if (ecom.getComponent() instanceof ComponentActive)
				str += "\n" + ((ComponentActive) com).getPinName( result.gpm.mode, result.gpm.index );
		}
		
		setTransHint( str );
	}
}

















































