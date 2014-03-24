


package logicBox.gui.editor;

import java.awt.Graphics2D;
import java.util.List;
import logicBox.sim.component.Component;
import logicBox.util.Line2;
import logicBox.util.Vec2;



/**
 * A representation of a trace in the editor GUI.
 * @author Lee Coakley
 * TODO
 */
public class EditorComponentTrace extends EditorComponent
{
	private List<Line2>       lines;
	private GraphicPinMapping attachStart;
	private GraphicPinMapping attachEnd;
	
	
	
	public EditorComponentTrace( Component com, GraphicComActive gca, Vec2 pos ) {
		super( com, gca, pos );
	}
	
	
	
	public void draw( Graphics2D g ) {
		
	}
}
