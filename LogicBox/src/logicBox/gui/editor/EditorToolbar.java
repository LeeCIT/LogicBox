


package logicBox.gui.editor;

import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import net.miginfocom.swing.MigLayout;



/**
 * The toolbar situated above the editor with standard and commonly used controls.
 * TODO this is a prototype, develop
 * TODO make special button type so icons won't look like crap. Look at the Eclipse ones for reference.
 * @author Lee Coakley
 */
public class EditorToolbar extends JToolBar
{
	public EditorToolbar() {
		super( HORIZONTAL );
		setFloatable( false );
		
		setLayout( new MigLayout("gap 1, insets 0", "[]", "[]") );
		
		String buttParam = "w 30, h 27";
		
		add( new JButton("N"), buttParam ); // New circuit
		add( new JButton("O"), buttParam ); // Open circuit
		add( new JSeparator(JSeparator.VERTICAL) );
		
		add( new JButton("U"), buttParam ); // Undo
		add( new JButton("R"), buttParam ); // Redo
		add( new JSeparator(JSeparator.VERTICAL) );
		
		add( new JButton("G"), buttParam ); // Grid toggle
		add( new JButton("T"), buttParam ); // Toolbox toggle
		add( new JButton("C"), buttParam ); // Centre camera on circuit
		add( new JSeparator(JSeparator.VERTICAL) );
	}
}

