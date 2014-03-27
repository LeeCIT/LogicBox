


package logicBox.gui.contextMenu;

import java.awt.event.*;
import javax.swing.*;



/**
 * Adds a context menu to a component
 * @author John
 */
public class ContextMenu extends MouseAdapter
{
	public ContextMenu( ContextMenuItem...items ) {
		JPopupMenu menu = new JPopupMenu();
		
		for (ContextMenuItem item: items)
			item.addTo( menu );
	}
}
