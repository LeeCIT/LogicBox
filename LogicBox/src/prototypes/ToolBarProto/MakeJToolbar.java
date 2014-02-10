package prototypes.ToolBarProto;

import java.util.List;

import javax.swing.JToolBar;



public class MakeJToolbar extends JToolBar
{	
	public MakeJToolbar() {}
	
	
	
	public MakeJToolbar(List<ToolbarItem> items) {
		for (ToolbarItem toolbarItem: items)
			toolbarItem.addTo(this);
	}
	

}
