


package logicBox.gui.editor;

import java.awt.Graphics2D;
import java.util.List;
import logicBox.sim.component.Trace;
import logicBox.util.Vec2;



/**
 * A representation of a trace in the editor GUI.
 * @author Lee Coakley
 */
public class EditorComponentTrace extends EditorComponent
{
	private static final long serialVersionUID = 1L;
	
	protected Trace        com;
	private   GraphicTrace graphic;
	
	
	
	public EditorComponentTrace( Trace com, GraphicPinMapping gpmA, GraphicPinMapping gpmB, List<Vec2> points ) {
		super( com );
		graphic = new GraphicTrace( points, gpmA, gpmB );
	}
	
	
	
	public Trace getComponent() {
		return com;
	}
	
	
	
	public GraphicTrace getGraphic() {
		return graphic;
	}
	
	
	
	public void draw( Graphics2D g ) {
		graphic.setPowered( com.getState() );
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
