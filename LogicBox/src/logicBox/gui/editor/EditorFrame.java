


package logicBox.gui.editor;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import net.miginfocom.swing.MigLayout;



/**
 * Contains the main editor interface.
 * @author Lee Coakley
 */
public class EditorFrame extends JFrame
{
	public EditorFrame( EditorPanel panel ) {
		super( "LogicBox" );
		setLayout( new MigLayout( "gap 0", "[grow,fill][]", "[grow,fill][]" ) );
		
		EditorScrollBar x = new EditorScrollBar( panel, JScrollBar.HORIZONTAL );
		EditorScrollBar y = new EditorScrollBar( panel, JScrollBar.VERTICAL   );
		
		add( panel );
		add( x, "cell 0 1" );
		add( y, "cell 1 0" );
	}
}
