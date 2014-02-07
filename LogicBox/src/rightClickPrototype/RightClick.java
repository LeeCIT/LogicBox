package rightClickPrototype;

import java.awt.event.*;
import javax.swing.*;

public class RightClick extends MouseAdapter{
	
	JPopupMenu popup;
	JMenuItem menuItem;
	
	public RightClick() {}
	
	/**
	 * Will create the context menu with the items in teh string array
	 * @param menuItems	What to display in teh context menu
	 */
	public RightClick(String[] menuItems) {
		
		popup = new JPopupMenu();
		
		for (int i=0; i<menuItems.length; i++) {
			menuItem = new JMenuItem(menuItems[i]);
			popup.add(menuItem);
		}
	}
	
	
	public void mousePressed(MouseEvent evt) {
		if (SwingUtilities.isRightMouseButton(evt)) {
			popup.show(evt.getComponent(),
					evt.getX(), evt.getY());
		}
	}
		
		
		
		
}
