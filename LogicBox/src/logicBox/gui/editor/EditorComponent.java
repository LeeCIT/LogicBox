


package logicBox.gui.editor;
import java.awt.Graphics2D;
import logicBox.sim.Component;
import logicBox.util.Vec2;



/**
 * 
 * @author Lee Coakley
 */
public class EditorComponent implements Drawable
{
	protected Component        component;
	protected GraphicComActive graphicComActive;
	
	protected Vec2   pos;
	protected double angle;
	
	
	
	public void draw( Graphics2D g, Vec2 pos, double angle ) {
		graphicComActive.draw( g, pos, angle );
	}
}
