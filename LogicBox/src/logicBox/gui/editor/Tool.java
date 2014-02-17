


package logicBox.gui.editor;



/**
 * An editor tool.
 * @author Lee Coakley
 */
public abstract class Tool
{
	private boolean attached;
	
	
	
	public boolean isAttached() {
		return attached;
	}
	
	
	
	protected void setAttached( boolean state ) {
		attached = state;
	}
	
	
	
	public abstract void attach();
	
	
	
	public abstract void detach();
}
