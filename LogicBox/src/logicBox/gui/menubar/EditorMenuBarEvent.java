package logicBox.gui.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import logicBox.gui.cloud.LoginPanel;

public class EditorMenuBarEvent 
{
	public static void handleLoginEvent(JMenuItem m)
	{
		m.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				LoginPanel lp = LoginPanel.getInstance();
				
				lp.setVisible(true);
			}
		});
	}
}