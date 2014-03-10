


package logicBox.gui.editorToolbar;

import javax.swing.ImageIcon;
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
		
		ImageIcon newFile   = new ImageIcon( "icons/New16px.png"       );
		ImageIcon openFile  = new ImageIcon( "icons/Open16px.png"      );
		ImageIcon undo      = new ImageIcon( "icons/Undo16px.png"      );
		ImageIcon redo      = new ImageIcon( "icons/Redo16px.png"      );
		ImageIcon editText  = new ImageIcon( "icons/EditText16px.png"  );
		ImageIcon grid      = new ImageIcon( "icons/Grid16px.png"      );
		ImageIcon centreCam = new ImageIcon( "icons/CentreCam16px.png" );
		ImageIcon toolBox   = new ImageIcon( "icons/ToolBox16px.png"   );
		
		
		// Make buttons for the EditorToolbar
		EditorToolBarButton newFileButt   = new EditorToolBarButton(newFile,  "New File"         );
		EditorToolBarButton openFileButt  = new EditorToolBarButton(openFile, "OpenFile"         );
		EditorToolBarButton undoButt      = new EditorToolBarButton(undo,     "Undo"             );
		EditorToolBarButton redoButt      = new EditorToolBarButton(redo,     "Redo"             );
		EditorToolBarButton editTextButt  = new EditorToolBarButton(editText, "Add Label"        );
		EditorToolBarButton enableGrid    = new EditorToolBarButton(grid,     "Show/Hide grid"   );
		EditorToolBarButton centreCamButt = new EditorToolBarButton(centreCam, "Centre Camera"   ); 
		EditorToolBarButton toolBoxButt   = new EditorToolBarButton(toolBox, "Show/Hide Toolbox" );
		
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
}

