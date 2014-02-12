package prototypes.ToolBarProto;

import java.util.List;

import javax.swing.JToolBar;



public class MakeToolbox extends JToolBar
{	
	public MakeToolbox() {}
	
	
	
	public MakeToolbox(List<ToolboxItem> items) {
		for (ToolboxItem toolbarItem: items)
			toolbarItem.addTo(this);
	}
	

}
