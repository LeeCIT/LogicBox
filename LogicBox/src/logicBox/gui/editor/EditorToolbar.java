


package logicBox.gui.editor;

import javax.swing.JButton;
import javax.swing.JToolBar;
import net.miginfocom.swing.MigLayout;



/**
 * The toolbar situated above the editor with standard and commonly used controls.
 * TODO this is a prototype
 * @author Lee Coakley
 */
public class EditorToolbar extends JToolBar
{
	public EditorToolbar() {
		super( HORIZONTAL );
		setFloatable( false );
		
		setLayout( new MigLayout() );
		
		add( new JButton("New") );
		add( new JButton("Open") );
		add( new JButton("Undo") );
		add( new JButton("Redo") );
	}
}

