


package logicBox.gui.editor.tools;

import logicBox.gui.editor.Camera;
import logicBox.gui.editor.EditorPanel;
import logicBox.gui.editor.EditorWorld;



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
}
