


package logicBox.gui.editor;
import logicBox.util.Bbox2;
import logicBox.util.Vec2;



/**
 * Implemented by graphic classes which have hover/select interactions. 
 * @author Lee Coakley
 */
public interface GraphicIntersector {
	/**
	 * Test whether pos is contained inside the graphic.
	 * This should be pixel-accurate or very close, it's used for mouse interaction.
	 */
	public boolean contains( Vec2 pos );
	
	
	
	/**
	 * Test whether the bounding box intersects the graphic.
	 * Usually uses for selection.
	 */
	public boolean overlaps( Bbox2 bbox );
}
