package prototypes.ToolBarProto.toolBox;

import javax.swing.JToolBar;

public class ToolBoxSeperator extends ToolboxItem
{
	/**
	 * Add a toolbox separator
	 */
	public void addTo(JToolBar toolbar) {
		toolbar.addSeparator();		
	}

}
