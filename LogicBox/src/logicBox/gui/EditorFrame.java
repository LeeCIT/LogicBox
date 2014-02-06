


package logicBox.gui;
import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;



public class EditorFrame extends JFrame
{
	public EditorFrame() {
		super();
		setLayout( new MigLayout( "", "[grow,fill]", "[grow,fill]" ) );
	}
}
