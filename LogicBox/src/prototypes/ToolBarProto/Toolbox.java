package prototypes.ToolBarProto;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;



public class Toolbox extends JToolBar
{	
	public Toolbox() {}
	
	
	/**
	 * Creates a toolbox with a frame title and the default orientation
	 * @param frameName
	 * @param vertical
	 */
	public Toolbox(String frameName, int vertical) {
		super(frameName, vertical);
	}


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
	 * Add a the category heading and 
	 * @param heading
	 * @param items
	 */
	public void addCategory(String heading) {
		add(new JLabel(heading));
		this.add(new JToolBar.Separator());
	}
	
	
	

}
