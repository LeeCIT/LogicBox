package logicBox.gui.cloud;

import logicBox.gui.GUI;
import logicBox.gui.editor.menubar.EditorMenuBar;
import logicBox.web.Request;
import logicBox.web.RequestInterface;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

public class CloudController {
	
	public static void handleLogoutRequest() {
		Request r = new Request();
		
		r.setRequestInterface(new RequestInterface() 
		{
			@Override
			public void onRequestResponse(HttpResponse<JsonNode> res, Request req, status sta) 
			{
				if(sta != status.COMPLETED)
					GUI.showError(GUI.getMainFrame(), "Couldn't complete request!", "Request Failure");
				else
				{
					setAuthState(false);
					
					GUI.showMessage(GUI.getMainFrame(), "Successfully logged out!", "Logout success");
				}
			}
		});
		
		r.logout();
	}
	
	public static void setAuthState(boolean state) {
		EditorMenuBar emb = GUI.getMainFrame().getEditorMenuBar();
		
		emb.itemCloudLogin.setVisible(!state);
		emb.itemCloudFiles.setVisible(state);
		emb.itemCloudLogout.setVisible(state);
	}
}