package prototypes.ToolBarProto;

import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

public class ToolboxUtil{
	
	/**
	 * Add a category to the JToolbar
	 * @param toolbox	The toolbox to add the category to
	 * @param heading	Category name and what is shown
	 */
	public static void addCategory(JToolBar toolbox, String heading) {
		toolbox.add(new JLabel(heading));
		toolbox.add(new JToolBar.Separator());
	}
}
