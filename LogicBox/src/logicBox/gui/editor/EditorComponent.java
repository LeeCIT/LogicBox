


package logicBox.gui.editor;
import java.awt.Graphics2D;
import java.io.Serializable;
import logicBox.sim.component.Component;
import logicBox.util.Vec2;



/**
 * An object which can be manipulated by the editor.
 * Not sure if traces will be classified this way.  They may be a special case.
 * @author Lee Coakley
 */
public abstract class EditorComponent implements Serializable, Drawable
{
	private static final long serialVersionUID = 1L;
	
	protected Component   com;
	private   EditorWorld world;
	
	
	
	public EditorComponent( Component com ) {
		this.com = com;
	}
	
	
	
	public abstract void setPos( Vec2 pos );
	public abstract Vec2 getPos();
	
	public abstract void   setAngle( double angle );
	public abstract double getAngle();
	
	
	
	/**
	 * Get the graphical representation of the component.
	 * Use return type covariance to return the appropriate graphic type.
	 */
	public abstract Graphic getGraphic();
	
	
	
	public void draw( Graphics2D g ) {
		getGraphic().draw( g );
	}
	
	
	
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
