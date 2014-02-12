package prototypes.ToolBarProto;

import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

public class ToolboxUtil{
	public static void addCategory(JToolBar toolbox, String heading) {
		toolbox.add(new JLabel(heading));
		toolbox.add(new JToolBar.Separator());
	}
}
