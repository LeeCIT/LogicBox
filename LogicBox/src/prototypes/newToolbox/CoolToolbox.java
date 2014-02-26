
package prototypes.newToolbox;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class CoolToolbox extends JDialog
{
	public CoolToolbox(JFrame parent) {
		super(parent);
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setVisible(true);
	}
}
