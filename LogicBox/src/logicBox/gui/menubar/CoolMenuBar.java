
package logicBox.gui.menubar;

import java.util.List;
import javax.swing.*;

/**
 * 
 * @author John
 *
 */
public class CoolMenuBar extends JMenuBar
{
	public CoolMenuBar(){
		super();
	}

	
	
	/**
	 * Pass in a list of CoolMenuItems with the first one being the heading you want at the top
	 * e.g "File"
	 * Then the subheadings e.g "Save"
	 * @param items
	 */
	public void addHeadingAndSubHeadings(List<CoolMenuItem> items) {
		JMenu menu = new JMenu();
		for (CoolMenuItem item : items) {
			if ( item.getHeading() != null ) {
				menu.setText(item.getHeading());
			}

			if ( item.isSeparator() ) {
				menu.addSeparator();
			}

			if ( !(item.getHeading() != null) && !(item.isSeparator()) ) {
				menu.add(item);
			}			
		}
		add(menu);
	}
}
