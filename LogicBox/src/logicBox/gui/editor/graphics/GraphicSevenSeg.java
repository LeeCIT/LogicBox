


package logicBox.gui.editor.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import logicBox.gui.Gfx;
import logicBox.gui.VecPath;
import logicBox.util.Transformable;



/**
 * Seven-segment display.
 * @author Lee Coakley
 */
public class GraphicSevenSeg extends GraphicComActive
{
	private List<VecPath> segs;
	private boolean[]     segStates;
	
	
	
	public GraphicSevenSeg( VecPath polyBody, VecPath polyPins, VecPath polyAux, List<GraphicPinMapping> pinMap, List<VecPath> segs ) {
		super( polyBody, polyPins, polyAux, pinMap );
		this.segs      = segs;		
		this.segStates = new boolean[ segs.size() ];
	}
	
	
	
	public void setSegmentStates( boolean[] segs ) {
		segStates = segs;
	}
	
	
	
	public void draw( Graphics2D g ) {
		super.draw( g );
		
		for (int i=0; i<segStates.length; i++) {
			boolean powered = segStates[i];
			Color   col     = (powered) ? EditorStyle.colLedOn : EditorStyle.colLedOff;
			
			if (isHighlighted() && !powered)
				col = colFillNormal;
			
			Gfx.pushColorAndSet( g, col );
				g.fill( segs.get(i) );
			Gfx.popColor( g );
		}
	}
	
	
	
	protected List<Transformable> getTransformables() {
		List<Transformable> trans = super.getTransformables();
		trans.addAll( segs );
		return trans;
	}
}
