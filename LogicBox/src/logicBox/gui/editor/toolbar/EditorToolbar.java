


package logicBox.gui.editor.toolbar;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JToolBar;
import logicBox.gui.IconLoader;
import logicBox.gui.editor.toolbar.EditorToolBarButton;
import net.miginfocom.swing.MigLayout;



/**
 * The toolbar situated above the editor with standard and commonly used controls.
 * That's a lot of butts...
 * @author Lee Coakley, John Murphy
 */
public class EditorToolbar extends JToolBar
{
	public EditorToolBarButton buttNew;
	public EditorToolBarButton buttOpen;
	public EditorToolBarButton buttSave;
	public EditorToolBarButton buttPrint;
	
	public EditorToolBarButton buttUndo;
	public EditorToolBarButton buttRedo;
	
	public EditorToolBarButton buttCut;
	public EditorToolBarButton buttCopy;
	public EditorToolBarButton buttPaste;
	
	public EditorToolBarButton buttToggleGrid;
	public EditorToolBarButton buttCameraRecentre;
	public EditorToolBarButton buttToggleToolbox;
	
	public EditorToolBarButton buttHelp;
	
	
	
	public EditorToolbar() {
		super( HORIZONTAL );
		setFloatable( false );
		createButtons();
	}
	
	
	
	private void createButtons() {
		setLayout( new MigLayout( "gap 0, insets 0", "[]", "[]" ) );
		
		buttNew            = addButton( "New16px.png"      , "New file"         , false );
		buttOpen           = addButton( "Open16px.png"     , "Open file"        , false );
		buttSave           = addButton( "Save16px.png"     , "Save file"        , false );
		buttPrint          = addButton( "Print16px.png"    , "Print file"       , false );
		                   
		buttUndo           = addButton( "Undo16px.png"     , "Undo"             , true  );
		buttRedo           = addButton( "Redo16px.png"     , "Redo"             , false );
		
		buttCut            = addButton( "Cut16px.png"      , "Cut"              , true  );
		buttCopy           = addButton( "Copy16px.png"     , "Copy"             , false );
		buttPaste          = addButton( "Paste16px.png"    , "Paste"            , false );
		
		buttToggleGrid     = addButton( "Grid16px.png"     , "Show/hide grid"   , true  );
		buttCameraRecentre = addButton( "CentreCam16px.png", "Centre camera"    , false );
		buttToggleToolbox  = addButton( "ToolBox16px.png"  , "Show/hide toolbox", false );
		
		buttHelp           = addButton( "Help16px.png"     , "Show help"        , true  );
	}
	
	
	
	private EditorToolBarButton addButton( String icoName, String tooltip, boolean sepBefore ) {
		Icon                icon  = IconLoader.load( icoName );		
		EditorToolBarButton butt  = new EditorToolBarButton( icon, tooltip );
		String              param = (sepBefore) ? "gap 12px" : "";
		
		add( butt, param );
		
		return butt;
	}
}










