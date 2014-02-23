package prototypes.ToolBarProto.toolBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import logicBox.util.Callback;
import net.miginfocom.swing.MigLayout;

public class ToolboxPanel extends JPanel
{
	
	public ToolboxPanel() {}
	
	/**
	 * Make a panel with the buttons arranged correctly.
	 * @param buttons a list of toolbox buttons with callback
	 */
	public ToolboxPanel(final List<ToolboxButtonCallback> buttons) {				
		this.setLayout(new MigLayout("wrap 3"));
		for (int i=0; i<buttons.size(); i++) {
			ToolboxButtonCallback item = buttons.get(i);
			JButton button             = item.getButton();
			final Callback callback    = item.getCallback();
			
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					callback.execute();
				}
			});
			this.add(button);
		}
	}

}
