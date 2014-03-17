package logicBox.gui.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import logicBox.gui.GUI;
import logicBox.gui.cloud.*;
import logicBox.web.*;

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
	
	public static void handleLogoutEvent(JMenuItem m)
	{
		m.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				Request r = new Request();
				
				r.setRequestInterface(new RequestInterface() 
				{
					@Override
					public void onRequestResponse(HttpResponse<JsonNode> res, Request req, status sta) 
					{
						if(sta != status.COMPLETED)
							Dialog.showError(GUI.getMainFrame(), "Couldn't complete request!", "Request Failure");
						else
						{
							EditorMenuBar.getInstance().setAuthState(false);
							
							Dialog.showInfo(GUI.getMainFrame(), "Successfully logged out!", "Logout success");
						}
					}
				});
				
				r.logout();
			}
		});
	}
}