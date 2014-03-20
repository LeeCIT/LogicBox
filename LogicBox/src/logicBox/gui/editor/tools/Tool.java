


package logicBox.gui.editor.tools;

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
	protected EditorPanel panel;
	protected EditorWorld world;
	protected Camera      cam;
	
	private boolean attached;
	private double  dragThreshold = 8;
	
	
	
	public Tool( EditorPanel panel, EditorWorld world, Camera cam, ToolManager manager ) {
		this.panel   = panel;
		this.world   = world;
		this.cam     = cam;
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
		return Geo.distance(origin,now) >= (dragThreshold / cam.getZoom());
	}
	
	
	
	protected boolean isComponentAt( Vec2 pos ) {
		return null != world.findTopmostAt( pos );
	}
	
	
	
	protected EditorComponent getComponentAt( Vec2 pos ) {
		return world.findTopmostAt( pos );
	}
}




















