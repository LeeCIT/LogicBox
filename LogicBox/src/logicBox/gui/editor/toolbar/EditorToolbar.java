


package logicBox.gui.editor.toolbar;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JToolBar;
import logicBox.gui.editor.toolbar.EditorToolBarButton;
import net.miginfocom.swing.MigLayout;



/**
 * The toolbar situated above the editor with standard and commonly used controls.
 * @author Lee Coakley, John Murphy
 */
public class EditorToolbar extends JToolBar
{
	public EditorToolBarButton newFileButt;
	public EditorToolBarButton openFileButt;
	public EditorToolBarButton undoButt;
	public EditorToolBarButton redoButt;
	public EditorToolBarButton editTextButt;
	public EditorToolBarButton enableGrid;
	public EditorToolBarButton centreCamButt;
	public EditorToolBarButton toolBoxButt;
	
	
	
	public EditorToolbar() {
		super( HORIZONTAL );
		setFloatable( false );
		addButtons();
	}
	
	
	
	/**
	 * Set up the buttons
	 */
	private void addButtons() {
		setLayout( new MigLayout( "gap 1, insets 0", "[]", "[]" ) );
		
		newFileButt   = addButton( "New16px.png"      , "New file"         , false );
		openFileButt  = addButton( "Open16px.png"     , "OpenFile"         , false );
		undoButt      = addButton( "Undo16px.png"     , "Undo"             , true  );
		redoButt      = addButton( "Redo16px.png"     , "Redo"             , false );
		enableGrid    = addButton( "Grid16px.png"     , "Show/hide grid"   , true  );
		centreCamButt = addButton( "CentreCam16px.png", "Centre camera"    , false ); 
		toolBoxButt   = addButton( "ToolBox16px.png"  , "Show/hide toolbox", false );
	}
	
	
	
	private EditorToolBarButton addButton( String icoName, String tooltip, boolean sepBefore ) {
		Icon                icon  = loadIconFromResource( icoName );		
		EditorToolBarButton butt  = new EditorToolBarButton( icon, tooltip );
		String              param = "w 22px, h 18px";
		
		if (sepBefore)
			param += ", gap 10px";
		
		add( butt, param );
		
		return butt;
	}
	
	
	
	private Icon loadIconFromResource( String name ) {
		return new ImageIcon( getClass().getClassLoader().getResource("resources/icons/" + name ) );
	}
}










