


package logicBox.gui.editor;
import javax.swing.JFrame;
import javax.swing.JScrollBar;

import logicBox.gui.editor.menubar.EditorMenuBar;
import logicBox.gui.editor.toolbar.EditorToolbar;
import net.miginfocom.swing.MigLayout;



/**
 * Contains the main editor interface.
 * @author Lee Coakley
 */
public class EditorFrame extends JFrame
{
	private EditorPanel     panel;
	private EditorMenuBar   menubar;
	private EditorToolbar   toolbar;
	private EditorScrollBar scrollX;
	private EditorScrollBar scrollY;
	
	
	
	public EditorFrame() {
		super( "LogicBox" );
		
		setupComponents();
		setupLayout();
	}
	
	
	
	public EditorPanel getEditorPanel() {
		return panel;
	}
	
	
	
	public EditorToolbar getEditorToolbox() {
		return toolbar;
	}
	
	
	
	public EditorMenuBar getEditorMenuBar() {
		return menubar;
	}
	
	
	
	private void setupComponents() {
		panel   = new EditorPanel();
		menubar = new EditorMenuBar();
		toolbar = new EditorToolbar();
		scrollX = new EditorScrollBar( panel, JScrollBar.HORIZONTAL );
		scrollY = new EditorScrollBar( panel, JScrollBar.VERTICAL   );
	}
	
	
	
	private void setupLayout() {
		setLayout( new MigLayout( "insets 0, gap 0", "[grow,fill][]", "[][grow,fill][]" ) );
		
		add( toolbar, "span 2, wrap" );
		add( panel,   "grow"         );
		add( scrollX, "cell 0 2"     );
		add( scrollY, "cell 1 1"     );
		
		setJMenuBar( menubar );
	}
}







