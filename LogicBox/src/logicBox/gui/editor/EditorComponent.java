


package logicBox.gui.editor;
import java.io.Serializable;
import logicBox.sim.component.Component;
import logicBox.util.Vec2;



/**
 * An object which can be manipulated by the editor.
 * Not sure if traces will be classified this way.  They may be a special case.
 * @author Lee Coakley
 */
public abstract class EditorComponent implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Component   com;
	private EditorWorld world;
	
	
	
	public EditorComponent( Component com ) {
		this.com = com;
	}
	
	
	
	public void onMouseClick() {
		if (getComponent().interactClick())
			world.simUpdate();
	}
	
	
	
	/**
	 * Do something if the world state changes (deleted component or whatever).
	 */
	public void onWorldChange() {
		
	}
	
	
	
	public abstract void setPos( Vec2 pos );
	public abstract Vec2 getPos();
	
	public abstract void   setAngle( double angle );
	public abstract double getAngle();
	
	
	
	/**
	 * Get the simulation component represented by this instance.
	 */
	public Component getComponent() {
		return com;
	}
	
	
	
	/**
	 * Get the name of the component represented by this object.
	 */
	public String getComponentName() {
		return com.getName();
	}
	
	
	
	/**
	 * Get the graphical representation of the component.
	 * Use return type covariance to return the appropriate graphic type.
	 */
	public abstract Graphic getGraphic();
	
	
	
	/**
	 * Find the pin nearest to POS within the given radius.
	 * Returns null if no pins meet the criterion.
	 */
	public abstract GraphicPinMapping findPinNear( Vec2 pos, double radius );
	
	
	
	/**
	 * Link with a world.  Only one world may be linked.  Only EditorWorld may call this method.
	 */
	protected void linkToWorld( EditorWorld world ) {
		this.world = world;
	}
	
	
	
	protected void unlinkFromWorld() {
		this.world = null;
	}
	
	
	
	protected void signalTransformChange() {
		if (world != null)
			world.onComponentTransform( this );
	}
}
