


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
		
		
		// TODO integrate so user can scroll with toolbars too (use world.getextent and cam.getviewablearea)
		JScrollBar x = new JScrollBar( JScrollBar.HORIZONTAL );
		JScrollBar y = new JScrollBar( JScrollBar.VERTICAL   );
		
		add( panel );
		add( x, "cell 0 1" );
		add( y, "cell 1 0" );
	}
}
