


package logicBox.gui.editor.components;

import logicBox.gui.editor.graphics.GraphicJunction;
import logicBox.gui.editor.graphics.GraphicPinMapping;
import logicBox.sim.component.connective.Junction;
import logicBox.util.Vec2;



/**
 * A representation of a junction in the editor GUI.
 * @author Lee Coakley
 */
public class EditorComponentJunction extends EditorComponent
{
	private static final long serialVersionUID = 1L;
	
	private Junction        com;
	private GraphicJunction graphic;
	
	
	
	public EditorComponentJunction( Junction com, Vec2 pos ) {
		super( com );
		this.com     = com;
		this.graphic = new GraphicJunction( pos );
	}
	
	
	
	public Junction getComponent() {
		return com;
	}
	
	
	
	public GraphicJunction getGraphic() {
		return graphic;
	}
	
	
	
	public void setPos( Vec2 pos ) {
		graphic.setPos( pos );
		signalTransformChange();
	}
	
	
	
	public Vec2 getPos() {
		return graphic.getPos();
	}
	
	
	
	public void setAngle( double angle ) {
		// Do nothing
	}
	
	
	
	public double getAngle() {
		return 0;
	}
	
	
	
	public GraphicPinMapping findPinNear( Vec2 pos, double radius ) {
		return null;
	}
}
