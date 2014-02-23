


package logicBox.gui.editor;
import java.awt.Graphics2D;
import logicBox.sim.component.Component;
import logicBox.util.Vec2;



/**
 * An object which can be manipulated by the editor.
 * Not sure if traces will be classified this way.  They may be a special case.
 * @author Lee Coakley
 */
public class EditorComponent
{
	protected Component        com;
	protected GraphicComActive graphic;
	protected EditorWorld      world;
	
	
	
	public EditorComponent( EditorWorld world, Component com, GraphicComActive gca, Vec2 pos ) {
		this.world   = world;
		this.com     = com;
		this.graphic = gca;
		setPos( pos );
	}
	
	
	
	public void draw( Graphics2D g ) {
		graphic.draw( g );
	}
	
	
	
	public void setPos( Vec2 pos ) {
		graphic.transformTo( pos, getAngle() );
		world.onComponentTransform( this );
	}
	
	
	
	public void setAngle( double angle ) {
		graphic.transformTo( getPos(), angle );
		world.onComponentTransform( this );
	}
	
	
	
	public Vec2 getPos() {
		return graphic.getPos();
	}
	
	
	
	public double getAngle() {
		return graphic.getAngle();
	}
}
