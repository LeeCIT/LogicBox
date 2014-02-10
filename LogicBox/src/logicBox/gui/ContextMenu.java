package logicBox.gui;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;


/**
 * Adds a context menu to a component
 * @author John
 *
 */
public class ContextMenu extends MouseAdapter{

	/**
	 * Default constructor
	 */
	public ContextMenu() {}
	
	
	
	public ContextMenu(List<ContextMenuItem> items) {
		JPopupMenu menu = new JPopupMenu();
		
		for (ContextMenuItem item: items)
			item.addTo( menu );
	}
}
