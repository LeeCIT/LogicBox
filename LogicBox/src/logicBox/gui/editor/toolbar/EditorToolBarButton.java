


package logicBox.gui.editor.toolbar;

import javax.swing.Icon;
import javax.swing.JButton;



/**
 * Makes buttons for the Editor toolbar
 * @author John
 */
public class EditorToolBarButton extends JButton
{
	public EditorToolBarButton( Icon icon, String tooltip ) {
		super( icon );
		setToolTipText( tooltip );
	}
}
