


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
	public EditorToolBarButton saveFileButt;
	public EditorToolBarButton printFileButt;
	
	public EditorToolBarButton undoButt;
	public EditorToolBarButton redoButt;
	
	public EditorToolBarButton enableGridButt;
	public EditorToolBarButton centreCamButt;
	public EditorToolBarButton toolBoxButt;
	
	
	
	public EditorToolbar() {
		super( HORIZONTAL );
		setFloatable( false );
		createButtons();
	}
	
	
	
	private void createButtons() {
		setLayout( new MigLayout( "gap 0, insets 0", "[]", "[]" ) );
		
		newFileButt    = addButton( "New16px.png"      , "New file"         , false );
		openFileButt   = addButton( "Open16px.png"     , "Open file"        , false );
		saveFileButt   = addButton( "Save16px.png"     , "Save file"        , false );
		printFileButt  = addButton( "Print16px.png"    , "Print file"       , false );
		
		undoButt       = addButton( "Undo16px.png"     , "Undo"             , true  );
		redoButt       = addButton( "Redo16px.png"     , "Redo"             , false );
		
		enableGridButt = addButton( "Grid16px.png"     , "Show/hide grid"   , true  );
		centreCamButt  = addButton( "CentreCam16px.png", "Centre camera"    , false );
		toolBoxButt    = addButton( "ToolBox16px.png"  , "Show/hide toolbox", false );
	}
	
	
	
	private EditorToolBarButton addButton( String icoName, String tooltip, boolean sepBefore ) {
		Icon                icon  = loadIconFromResource( icoName );		
		EditorToolBarButton butt  = new EditorToolBarButton( icon, tooltip );
		String              param = (sepBefore) ? "gap 16px" : "";
		
		add( butt, param );
		
		return butt;
	}
	
	
	
	private Icon loadIconFromResource( String name ) {
		return new ImageIcon( getClass().getClassLoader().getResource("resources/icons/" + name) );
	}
}










