package rightClickPrototype;

import java.awt.event.*;
import java.util.Hashtable;

import javax.swing.*;

import logicBox.util.Callback;

public class RightClick extends MouseAdapter{

	JPopupMenu popup;
	JMenuItem menuItem;

	static Hashtable<String, JMenuItem> menuHolder = new Hashtable<String, JMenuItem>();


	public RightClick() {}

	/**
	 * Will create the context menu with the items in the string array
	 * @param menuItems	What to display in the context menu
	 */
	public RightClick(String[] menuItems) {
		popup = new JPopupMenu();

		for (int i=0; i<menuItems.length; i++) {
			menuItem = new JMenuItem(menuItems[i]);
			popup.add(menuItem);
			menuHolder.put(menuItems[i], menuItem);
		}
	}



	/**
	 * When the mouse is pressed a popup contextual menu is shown
	 */
	public void mousePressed(MouseEvent evt) {
		if (SwingUtilities.isRightMouseButton(evt)) {
			popup.show(evt.getComponent(),
					evt.getX(), evt.getY());
		}
	}


	/**
	 * Add a callback to the rightClick event
	 * @param menuName	The name of the click event
	 * @param callback	The action of the click
	 */
	public static void addCallbackToClickEvent(String menuName, Callback callback) {
		JMenuItem menuItem = menuHolder.get(menuName);
		// TODO add callback to menuItem
	}




}
