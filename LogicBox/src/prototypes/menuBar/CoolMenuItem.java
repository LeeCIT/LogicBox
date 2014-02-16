package prototypes.menuBar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JMenuItem;

import logicBox.util.Callback;

/**
 * Make cool items to pass into the coolMenu extended methods
 * A heading will be the top level view e.g "File" When making one leave all other fields to null and separator to false 
 * To use a separator make isSeparator true and leave other prams to null
 * @author John
 *
 */
public class CoolMenuItem extends JMenuItem
{
	private String heading;
	private boolean isSeparator;
	
	public CoolMenuItem (String title, String heading, final Callback callback, char mnemonic, Icon icon, boolean isSeparator) {
		super();
		
		if ( title != null ){
			this.setText(title);
		}
		if ( icon != null ) {
			this.setIcon(icon);
		}
		if ( mnemonic != 0 ) {
			this.setMnemonic(mnemonic);
		}
		if (callback != null) {
			this.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					callback.execute();					
				}
			});
		}
		
		this.isSeparator = isSeparator;
		this.heading 	 = heading;
	}

	public String getHeading() {
		return heading;
	}

	public boolean isSeparator() {
		return isSeparator;
	}
}
