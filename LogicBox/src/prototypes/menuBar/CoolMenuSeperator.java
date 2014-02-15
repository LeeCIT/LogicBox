
package prototypes.menuBar;

import javax.swing.JMenuBar;
import javax.swing.JSeparator;

/**
 * 
 * @author John
 *
 */
public class CoolMenuSeperator extends CoolMenuItemData
{
	public void addTo(JMenuBar menuBar) {
		menuBar.add(new JSeparator());
	}
}
