


package logicBox.gui.editor.graphics;
import java.awt.Graphics2D;



/**
 * Allows editor drawing to be modularised by delegating drawing to tools. 
 * @author Lee Coakley
 */
public interface RepaintListener extends Drawable {
	public void draw( Graphics2D g );
}
