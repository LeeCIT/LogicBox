


package logicBox.gui.editor.tools;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import logicBox.gui.editor.Camera;
import logicBox.gui.editor.EditorComponent;
import logicBox.gui.editor.EditorPanel;
import logicBox.gui.editor.EditorWorld;
import logicBox.util.Geo;
import logicBox.util.Vec2;



/**
 * An editor tool.
 * @author Lee Coakley
 */
public abstract class Tool
{
	protected ToolManager manager;
	
	private boolean attached;
	private double  dragThreshold = 8;
	
	
	
	public Tool( ToolManager manager ) {
		this.manager = manager;
	}
	
	
	
	public boolean isAttached() {
		return attached;
	}
	
	
	
	protected void setAttached( boolean state ) {
		attached = state;
	}
	
	
	
	public abstract void attach();
	
	
	
	public abstract void detach();
	
	
	
	protected boolean isLeft( MouseEvent ev ) {
		return SwingUtilities.isLeftMouseButton( ev );
	}
	
	
	
	protected boolean isRight( MouseEvent ev ) {
		return SwingUtilities.isRightMouseButton( ev );
	}
	
	
	
	protected boolean isDragThresholdMet( Vec2 origin, Vec2 now ) {
		return Geo.distance(origin,now) >= (dragThreshold / getCamera().getZoom());
	}
	
	
	
	protected Vec2 getMousePosWorld() {
		return getCamera().getMousePosWorld();
	}
	
	
	
	protected void setCursor( Cursor cursor ) {
		getEditorPanel().setCursor( cursor );
	}
	
	
	
	protected void resetCursor() {
		getEditorPanel().setCursor( Cursor.getDefaultCursor() );
	}
	
	
	
	protected void repaint() {
		manager.getEditorPanel().repaint();
	}
	
	
	
	protected EditorPanel getEditorPanel() {
		return manager.getEditorPanel();
	}
	
	
	
	protected EditorWorld getWorld() {
		return manager.getEditorPanel().getWorld();
	}
	
	
	
	protected Camera getCamera() {
		return manager.getEditorPanel().getCamera();
	}
	
	
	
	protected boolean isComponentAt( Vec2 pos ) {
		return null != getWorld().findTopmostAt( pos );
	}
	
	
	
	protected EditorComponent getComponentAt( Vec2 pos ) {
		return getWorld().findTopmostAt( pos );
	}
}




















