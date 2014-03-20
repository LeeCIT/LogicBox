
package logicBox.gui.editorToolbar;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Makes buttons for the Editor toolbar
 * @author John
 *
 */
public class EditorToolBarButton extends JButton{
		
	
	public EditorToolBarButton(ImageIcon icon, String tip) {
		super();
		this.setIcon(icon);
		this.setToolTipText(tip);
	}
	

}
