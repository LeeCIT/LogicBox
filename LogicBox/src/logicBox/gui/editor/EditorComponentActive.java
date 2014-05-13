


package logicBox.gui.editor;
import logicBox.gui.editor.graphics.GraphicComActive;
import logicBox.gui.editor.graphics.GraphicPinMapping;
import logicBox.sim.component.ComponentActive;
import logicBox.util.Vec2;



/**
 * An active component which can be manipulated by the editor.
 * @author Lee Coakley
 */
public class EditorComponentActive extends EditorComponent
{
	private static final long serialVersionUID = 1L;
	
	private ComponentActive  com;
	private GraphicComActive graphic;
	
	
	
	public EditorComponentActive( ComponentActive com, GraphicComActive gca, Vec2 pos, double angle ) {
		super( com );
		this.com     = com;
		this.graphic = gca;
		setPosAngle( pos, angle );
	}
	
	
	
	public EditorComponentActive( ComponentActive com, GraphicComActive gca, Vec2 pos ) {
		this( com, gca, pos, gca.getAngle() );
	}
	
	
	
	public ComponentActive getComponent() {
		return com;
	}
	
	
	
	public GraphicComActive getGraphic() {
		return graphic;
	}
	
	
	
	public void setPos( Vec2 pos ) {
		graphic.transformTo( pos, getAngle() );
		signalTransformChange();
	}	
	
	
	
	public void setAngle( double angle ) {
		graphic.transformTo( getPos(), angle );
		signalTransformChange();
	}
	
	
	
	public void setPosAngle( Vec2 pos, double angle ) {
		graphic.transformTo( pos, angle );
		signalTransformChange();
	}	
	
	
	
	public Vec2 getPos() {
		return graphic.getPos();
	}
	
	
	
	public double getAngle() {
		return graphic.getAngle();
	}
	
	
	
	public GraphicPinMapping findPinNear( Vec2 pos, double radius ) {
		return graphic.findClosestPin( pos, radius );
	}
}
