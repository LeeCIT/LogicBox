


package logicBox.gui.editor;
import java.awt.Graphics2D;
import logicBox.sim.component.Component;
import logicBox.util.Vec2;



/**
 * An object which can be manipulated by the editor.
 * Not sure if traces will be classified this way.  They may be a special case.
 * @author Lee Coakley
 */
public class EditorComponent implements Drawable
{
	protected Component        com;
	protected GraphicComActive graphic;
	private   EditorWorld      world;
	
	
	
	public EditorComponent( Component com, GraphicComActive gca, Vec2 pos ) {
		this.com     = com;
		this.graphic = gca;
		setPos( pos );
	}
	
	
	
	public void draw( Graphics2D g ) {
		graphic.draw( g );
	}
	
	
	
	public void setPos( Vec2 pos ) {
		graphic.transformTo( pos, getAngle() );
		signalTransformChange();
	}	
	
	
	
	public void setAngle( double angle ) {
		graphic.transformTo( getPos(), angle );
		signalTransformChange();
	}
	
	
	
	public Vec2 getPos() {
		return graphic.getPos();
	}
	
	
	
	public double getAngle() {
		return graphic.getAngle();
	}
	
	
	
	/**
	 * Link with a world.  Only one world may be linked.  Only EditorWorld may call this method.
	 */
	protected void linkToWorld( EditorWorld world ) {
		this.world = world;
	}
	
	
	
	protected void unlinkFromWorld() {
		this.world = null;
	}
	
	
	
	private void signalTransformChange() {
		if (world != null)
			world.onComponentTransform( this );
	}
}
