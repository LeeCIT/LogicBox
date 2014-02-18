


package logicBox.gui.editor;
import java.awt.Graphics2D;
import logicBox.sim.component.Component;
import logicBox.util.Vec2;



/**
 * 
 * @author Lee Coakley
 */
public class EditorComponent
{
	protected Component        com;
	protected GraphicComActive graphic;
	
	protected Vec2   pos;
	protected double angle;
	
	
	
	public EditorComponent( Component com, GraphicComActive gca, Vec2 pos ) {
		this.com     = com;
		this.graphic = gca;
		this.pos     = pos;
	}
	
	
	
	public void draw( Graphics2D g ) {
		graphic.draw( g, pos, angle );
	}
}
