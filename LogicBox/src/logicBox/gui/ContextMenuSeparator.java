


package logicBox.gui;
import javax.swing.JPopupMenu;


/**
 * @author Lee Coakley
 */
public class ContextMenuSeparator extends ContextMenuItem
{
	protected void addTo( JPopupMenu menu ) {
		menu.addSeparator();
	}
}
