


package logicBox.gui.editor;
import java.awt.Graphics2D;



/**
 * Allows editor drawing to be modularised by delegating drawing to tools. 
 * @author Lee Coakley
 */
public interface RepaintListener {
	public void draw( Graphics2D g );
}
