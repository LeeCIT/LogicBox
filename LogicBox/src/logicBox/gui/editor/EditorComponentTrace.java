


package logicBox.gui.editor;

import java.awt.Graphics2D;
import java.util.List;
import logicBox.sim.component.Component;
import logicBox.util.Line2;
import logicBox.util.Vec2;



/**
 * A representation of a trace in the editor GUI.
 * @author Lee Coakley
 */
public class EditorComponentTrace extends EditorComponent
{
	private static final long serialVersionUID = 1L;
	
	private GraphicTrace graphic;
	
	
	
	public EditorComponentTrace( Component com, List<Vec2> points ) {
		super( com );
		graphic = new GraphicTrace( points, null, null );
	}
	
	
	
	public GraphicTrace getGraphic() {
		return graphic;
	}
	
	
	
	public void draw( Graphics2D g ) {
		graphic.draw( g );
	}



	public void setPos( Vec2 pos ) {
		// Do nothing
	}



	public Vec2 getPos() {
		return new Vec2();
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
