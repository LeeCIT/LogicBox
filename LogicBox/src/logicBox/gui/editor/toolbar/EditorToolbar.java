


package logicBox.gui.editor.toolbar;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JToolBar;
import logicBox.gui.IconEnum;
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
	
	public EditorToolBarButton buttSelectNone;
	public EditorToolBarButton buttSelectAll;
	public EditorToolBarButton buttSelectInvert;
	public EditorToolBarButton buttSelectBlackBox;
	
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
		
		buttNew            = addButton( IconEnum.newFile,       "New circuit"      , false );
		buttOpen           = addButton( IconEnum.openFile,      "Open circuit"     , false );
		buttSave           = addButton( IconEnum.saveFile,      "Save circuit"     , false );
		buttPrint          = addButton( IconEnum.print,         "Print circuit"    , false );
		                                                        
		buttUndo           = addButton( IconEnum.undo,          "Undo"             , true  );
		buttRedo           = addButton( IconEnum.redo,          "Redo"             , false );
		                                                        
		buttCut            = addButton( IconEnum.cut,           "Cut"              , true  );
		buttCopy           = addButton( IconEnum.copy,          "Copy"             , false );
		buttPaste          = addButton( IconEnum.paste,         "Paste"            , false );
		
		buttSelectAll      = addButton( IconEnum.selectAll,     "Select all"       , true  );
		buttSelectNone     = addButton( IconEnum.selectNone,    "Select none"      , false );
		buttSelectInvert   = addButton( IconEnum.selectInverse, "Invert selection" , false );
		buttSelectBlackBox = addButton( IconEnum.selectBlack,   "Create black-box" , false );
		
		buttToggleGrid     = addButton( IconEnum.grid,          "Show/hide grid"   , true  );
		buttCameraRecentre = addButton( IconEnum.camera,        "Centre camera"    , false );
		buttToggleToolbox  = addButton( IconEnum.toolbox,       "Show/hide toolbox", false );
		                                                        
		buttHelp           = addButton( IconEnum.help,          "Show help"        , true  );
	}
	
	
	
	private EditorToolBarButton addButton( IconEnum iconEnum, String tooltip, boolean sepBefore ) {
		Icon                icon  = IconLoader.load( iconEnum );		
		EditorToolBarButton butt  = new EditorToolBarButton( icon, tooltip );
		String              param = (sepBefore) ? "gap 12px" : "";
		
		add( butt, param );
		
		return butt;
	}
}










