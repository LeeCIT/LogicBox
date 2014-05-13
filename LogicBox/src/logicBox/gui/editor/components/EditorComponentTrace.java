


package logicBox.gui.editor.components;

import java.util.List;
import logicBox.gui.editor.graphics.GraphicPinMapping;
import logicBox.gui.editor.graphics.GraphicTrace;
import logicBox.sim.component.connective.Trace;
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
		this.graphic = new GraphicTrace( points );
		onWorldChange();
	}
	
	
	
	public void onWorldChange() {
		if ( ! graphic.isSelected())
			graphic.setPowered( com.getState() && getWorldPowerState() );
		
		graphic.setConnectedSource( com.isSourceConnected() );
		graphic.setConnectedDest  ( com.isDestConnected()   );
	}
	
	
	
	public Trace getComponent() {
		return com;
	}
	
	
	
	public GraphicTrace getGraphic() {
		return graphic;
	}
	
	
	
	public void setPos( Vec2 pos ) {
		List<Vec2> points = graphic.getPoints();
		Vec2       delta  = Geo.delta( getPosStart(), pos );
		
		for (Vec2 v: points)
			v.setLocation( v.add(delta) );
		
		graphic.setFromPoints( points );
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
	
	
	
	public Vec2 getPosStart() {
		return graphic.getPoints().get(0);
	}
	
	
	
	public Vec2 getPosEnd() {
		List<Vec2> points = getGraphic().getPoints();
		return points.get( points.size() - 1 );
	}
}
