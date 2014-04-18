


package logicBox.gui.editor;

import java.awt.Graphics2D;
import java.util.List;
import logicBox.sim.component.Trace;
import logicBox.util.Geo;
import logicBox.util.Vec2;



/**
 * A representation of a trace in the editor GUI.
 * @author Lee Coakley
 */
public class EditorComponentTrace extends EditorComponent
{
	private static final long serialVersionUID = 1L;
	
	private Trace        com;
	private GraphicTrace graphic;
	
	
	
	public EditorComponentTrace( Trace com, List<Vec2> points ) {
		super( com );
		this.com     = com;
		this.graphic = new GraphicTrace( points, null, null );
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
		List<Vec2> points = getGraphic().getPoints();
		Vec2       delta  = Geo.delta( getPosStart(), pos );
		
		for (Vec2 v: points)
			v.setLocation( v.add(delta) );
		
		getGraphic().setFromPoints( points );
		signalTransformChange();
	}
	
	
	
	public Vec2 getPos() {
		return getPosStart();
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
	
	
	
	private Vec2 getPosStart() {
		return graphic.getPoints().get(0);
	}
	
	
	
	private Vec2 getPosEnd() {
		List<Vec2> points = getGraphic().getPoints();
		return points.get( points.size() - 1 );
	}
}
