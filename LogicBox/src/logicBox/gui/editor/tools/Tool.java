


package logicBox.gui.editor.tools;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import logicBox.gui.editor.Camera;
import logicBox.gui.editor.EditorPanel;
import logicBox.gui.editor.EditorWorld;
import logicBox.gui.editor.components.EditorComponent;
import logicBox.gui.editor.graphics.GraphicTransHint;
import logicBox.util.Geo;
import logicBox.util.Vec2;



/**
 * An editor tool.
 * @author Lee Coakley
 */
public abstract class Tool
{
	private final ToolManager      manager;
	private final double           dragThreshold;
	private       boolean          attached;
	private       GraphicTransHint hint;
	
	
	
	public Tool( ToolManager manager ) {
		this.manager       = manager;
		this.dragThreshold = 8;
	}
	
	
	
	public boolean isAttached() {
		return attached;
	}
	
	
	
	protected void setAttached( boolean state ) {
		attached = state;
	}
	
	
	
	/**
	 * Allow the tool to receive events from its EditorPanel and draw.
	 */
	public abstract void attach();
	
	
	
	/**
	 * Stop the tool from receiving events and drawing.
	 */
	public abstract void detach();
	
	
	
	/**
	 * Set the tool state back to its unused state.
	 */
	public abstract void reset();
	
	
	
	protected void setTransHint( String text ) {
		removeTransHint();
		hint = new GraphicTransHint( text ); 
		getEditorPanel().addScreenRepaintListener( hint );
	}
	
	
	
	protected void removeTransHint() {
		if (hint != null)
			getEditorPanel().removeScreenRepaintListener( hint );
		
		hint = null;
	}
	
	
	
	protected boolean isLeft( MouseEvent ev ) {
		return SwingUtilities.isLeftMouseButton( ev );
	}
	
	
	
	protected boolean isRight( MouseEvent ev ) {
		return SwingUtilities.isRightMouseButton( ev );
	}
	
	
	
	protected boolean isDragThresholdMet( Vec2 origin, Vec2 now ) {
		return Geo.distance(origin,now) >= (dragThreshold / getCamera().getZoom());
	}
	
	
	
	protected Vec2 getMousePosCom() {
		return getCamera().getMousePosCom();
	}
	
	
	
	protected Vec2 getMousePosScreen() {
		return getCamera().getMousePosScreen();
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
	
	
	
	protected ToolManager getToolManager() {
		return manager;
	}
	
	
	
	protected void repaint() {
		manager.getEditorPanel().repaint();
	}
	
	
	
	protected void markHistoryChange( String what ) {
		manager.getEditorController().getHistoryManager().markChange( what );
	}
	
	
	
	protected EditorPanel getEditorPanel() {
		return manager.getEditorPanel();
	}
	
	
	
	protected EditorWorld getWorld() {
		return manager.getEditorController().getWorld();
	}
	
	
	
	protected Camera getCamera() {
		return manager.getEditorController().getCamera();
	}
	
	
	
	protected boolean isComponentAt( Vec2 pos ) {
		return null != getWorld().findTopmostAt( pos );
	}
	
	
	
	protected EditorComponent getComponentAt( Vec2 pos ) {
		return getWorld().findTopmostAt( pos );
	}
}




















