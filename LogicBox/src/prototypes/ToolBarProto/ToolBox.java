package prototypes.ToolBarProto;

import java.util.List;

import javax.swing.JPanel;
import javax.swing.JToolBar;



public class ToolBox extends JToolBar
{	
	public ToolBox() {}
	
	
	/**
	 * For adding many items at once. Takes a list of items and adds them to the toolbox
	 * @param items
	 */
	public void addToolBoxItemList(List<ToolboxItem> items) {
		for (ToolboxItem toolbarItem: items)
			toolbarItem.addTo(this);
	}
	
	
	
	/**
	 * Add a individual toolbox item
	 * @param item
	 */
	public void addToolBoxItemButton(ToolboxItem item) {
		item.addTo(this);
	}
	
	
	
	/**
	 * Add a the category heading and the list of buttons to add to the panel in groups of 3
	 * @param heading
	 * @param items
	 */
	public void addPanelWithButtons(String heading, List<ToolboxItem> items) {
		
	}
	
	
	

}
