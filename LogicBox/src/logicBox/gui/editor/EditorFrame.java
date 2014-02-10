


package logicBox.gui.editor;
import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;



/**
 * Contains the main editor interface.
 * @author Lee Coakley
 */
public class EditorFrame extends JFrame
{
	public EditorFrame() {
		super();
		setLayout( new MigLayout( "", "[grow,fill]", "[grow,fill]" ) );
	}
}
