package prototypes.ToolBarProto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import logicBox.util.Callback;

public class ToolbarItemData extends ToolbarItem
{
	private JButton  button;
	private Callback callback;
	private Icon 	 icon;

	
	public ToolbarItemData(JButton button, Icon icon, Callback callback) {
		this.button		= button;
		this.icon 		= icon;
		this.callback 	= callback;
	}


	protected void addTo(final JToolBar toolbar) {		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				callback.execute();					
			}
		});
		
		if (icon != null)
			button.setIcon(icon);
		
		toolbar.add(button);
	}

}
