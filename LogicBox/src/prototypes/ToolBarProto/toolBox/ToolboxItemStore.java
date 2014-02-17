package prototypes.ToolBarProto.toolBox;

import javax.swing.JButton;
import javax.swing.JToolBar;

public class ToolboxItemStore extends ToolboxItem
{
	private JButton  button;


	
	public ToolboxItemStore(JButton button) {
		this.button	= button;
	}


	protected void addTo(final JToolBar toolbar) {				
		toolbar.add(button);
	}

}
