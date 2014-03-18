


package logicBox.gui.editor.toolbar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

import net.miginfocom.swing.MigLayout;



/**
 * The toolbar situated above the editor with standard and commonly used controls.
 * TODO this is a prototype, develop
 * TODO make special button type so icons won't look like crap. Look at the Eclipse ones for reference.
 * @author Lee Coakley, John Murphy
 */
public class EditorToolbar extends JToolBar
{
	public EditorToolBarButton newFileButt;
	public EditorToolBarButton openFileButt;
	public EditorToolBarButton printButt;
	
	public EditorToolBarButton undoButt;
	public EditorToolBarButton redoButt;
	
	public EditorToolBarButton editTextButt;
	public EditorToolBarButton enableGrid;
	public EditorToolBarButton centreCamButt;
	public EditorToolBarButton toolBoxButt;
	
	
	
	public EditorToolbar() {
		super( HORIZONTAL );
		setFloatable( false );

		setLayout( new MigLayout( "gap 1, insets 0", "[]", "[]") );
		setUpToolbar();
	}
	
	
	
	/**
	 * Set up the buttons
	 */
	private void setUpToolbar() {
		ImageIcon newFile   = loadIconFromResource( "icons/New16px.png"       );
		ImageIcon openFile  = loadIconFromResource( "icons/Open16px.png"      );
		ImageIcon undo      = loadIconFromResource( "icons/Undo16px.png"      );
		ImageIcon redo      = loadIconFromResource( "icons/Redo16px.png"      );
		ImageIcon editText  = loadIconFromResource( "icons/EditText16px.png"  );
		ImageIcon grid      = loadIconFromResource( "icons/Grid16px.png"      );
		ImageIcon centreCam = loadIconFromResource( "icons/CentreCam16px.png" );
		ImageIcon toolBox   = loadIconFromResource( "icons/ToolBox16px.png"   );
		
		// Make buttons for the EditorToolbar
		newFileButt   = new EditorToolBarButton(newFile,   "New File"          );
		openFileButt  = new EditorToolBarButton(openFile,  "OpenFile"          );
		undoButt      = new EditorToolBarButton(undo,      "Undo"              );
		redoButt      = new EditorToolBarButton(redo,      "Redo"              );
		editTextButt  = new EditorToolBarButton(editText,  "Add Label"         );
		enableGrid    = new EditorToolBarButton(grid,      "Show/Hide grid"    );
		centreCamButt = new EditorToolBarButton(centreCam, "Centre Camera"     ); 
		toolBoxButt   = new EditorToolBarButton(toolBox,   "Show/Hide Toolbox" );
		
		String buttParam = "w 30, h 27";
		
		add( newFileButt,  buttParam ); // New circuit
		add( openFileButt, buttParam ); // Open circuit
		add( new JSeparator(JSeparator.VERTICAL) );
		
		add( undoButt, buttParam ); // Undo
		add( redoButt, buttParam ); // Redo
		add( new JSeparator(JSeparator.VERTICAL) );
		
		add( enableGrid,    buttParam ); // Grid toggle
		add( toolBoxButt,   buttParam ); // Toolbox toggle
		add( centreCamButt, buttParam ); // Centre camera on circuit
		add( editTextButt,  buttParam ); // Make a label for selections
		add( new JSeparator(JSeparator.VERTICAL) );
	}
	
	
	
	private ImageIcon loadIconFromResource(String path)	{
		return new ImageIcon(getClass().getClassLoader().getResource("resources/" + path));
	}
}

