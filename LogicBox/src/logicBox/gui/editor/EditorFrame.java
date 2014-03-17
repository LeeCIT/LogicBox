


package logicBox.gui.editor;
import javax.swing.JFrame;
import javax.swing.JScrollBar;

import logicBox.gui.editorToolbar.EditorToolbar;
import net.miginfocom.swing.MigLayout;



/**
 * Contains the main editor interface.
 * @author Lee Coakley
 */
public class EditorFrame extends JFrame
{
	public EditorFrame( EditorPanel panel ) {
		super( "LogicBox" );
		setLayout( new MigLayout( "insets 0, gap 0", "[grow,fill][]", "[][grow,fill][]" ) );
		
		EditorToolbar   toolbar = new EditorToolbar();
		EditorScrollBar x       = new EditorScrollBar( panel, JScrollBar.HORIZONTAL );
		EditorScrollBar y       = new EditorScrollBar( panel, JScrollBar.VERTICAL   );
		
		add( toolbar, "span 2, wrap" );
		add( panel,   "grow"         );
		add( x,       "cell 0 2"     );
		add( y,       "cell 1 1"     );
	}
}
