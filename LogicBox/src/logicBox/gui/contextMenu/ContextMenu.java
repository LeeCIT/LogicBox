


package logicBox.gui.contextMenu;

import java.awt.Component;
import javax.swing.*;
import logicBox.util.Vec2;



/**
 * A generic context menu.
 * @author John Murphy
 * @author Lee Coakley
 */
public class ContextMenu
{
	private JPopupMenu menu;
	
	
	
	public ContextMenu( ContextMenuItem...items ) {
		this.menu = new JPopupMenu();
		
		for (ContextMenuItem item: items)
			item.addTo( menu );
	}
	
	
	
	public void show( Component com, Vec2 pos ) {
		menu.show( com, (int) pos.x, (int) pos.y );
	}
}
