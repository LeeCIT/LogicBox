package prototypes.menuBar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import logicBox.util.Callback;

public class CoolMenuItem extends JMenuItem
{
	public CoolMenuItem (Icon icon, String title, final Callback callback, char mnemonic) {
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
	}	
}
