package logicBox.gui;

import java.awt.PopupMenu;
import java.awt.event.*;
import java.util.*;
import java.util.Map.Entry;

import javax.swing.*;

import logicBox.util.Callback;

/**
 * Adds a context menu to a component
 * @author John
 *
 */
public class ContextMenu extends MouseAdapter{

	private JPopupMenu popup;
	private JMenuItem menuItem;

	private Hashtable<String, JMenuItem> menuHolder = new Hashtable<String, JMenuItem>();

	String seperatorCheck = "#-#";


	/**
	 * Default constructor
	 */
	public ContextMenu() {}



	/**
	 * Will create the context menu with the items in the string array
	 * To add a separator after the menuitem add "#-#" without quotes after the string
	 * @param menuItems	What to display in the context menu
	 */
	public ContextMenu(String[] menuItems) {
		popup = new JPopupMenu();

		for (int i=0; i<menuItems.length; i++) {			
			String menuString 		= menuItems[i];
			boolean seperatorCheck 	= checkForSequence(menuString);
			// If a separator is required get the string without the sequence
			if (seperatorCheck) {
				menuString = getMenuItemWithoutSequnce(menuString);
			}
			
			// Make the menu item and it to the popup menu
			menuItem = new JMenuItem(menuString);			
			popup.add(menuItem);
			
			// Add the separator
			if(seperatorCheck) {
				popup.add(new JSeparator());
			}
			
			// Add the menuItem to the hashmap
			menuHolder.put(menuString, menuItem);		
		}
	}



	/**
	 * When the mouse is pressed a popup contextual menu is shown
	 */
	public void mousePressed(MouseEvent evt) {
		if (SwingUtilities.isRightMouseButton(evt)) {
			popup.show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}



	/**
	 * Add a callback to the rightClick event
	 * @param menuName	The name of the click event
	 * @param callback	The action of the click
	 */
	public void addClickEvent(String menuName, final Callback callback) {
		JMenuItem menuItem = menuHolder.get(menuName);

		menuItem.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (callback != null)
					callback.execute();
			}
		});
	}



	/**
	 * Add a hashmap with strings mapped to the callbacks. 
	 * Assigns the approbate callback with the correct item
	 * @param contextMap	The mapping of the strings to callbacks
	 */
	public void mapContextMenuToCallback(HashMap<String, Callback> contextMap) {
		for (final Entry<String, Callback> entry : contextMap.entrySet()) {
			JMenuItem 		menuItem = menuHolder.get(entry.getKey());
			final Callback 	callback = entry.getValue();

			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (callback != null) {
						callback.execute();
					}
				}
			});
		}
	}



	/**
	 * Returns whether or not a separator is wanted
	 * @param toCheck
	 * @return
	 */
	private boolean checkForSequence(String toCheck) {
		if (toCheck.contains(seperatorCheck)) {
			return true;
		}		
		return false;
	}



	/**
	 * Used to take out the cheksequence out of the menu item
	 * @param menuItemWithSequence
	 * @return
	 */
	private String getMenuItemWithoutSequnce(String menuItemWithSequence) {
		int position = menuItemWithSequence.indexOf(seperatorCheck);
		return menuItemWithSequence.substring(0, position);
	}
}
