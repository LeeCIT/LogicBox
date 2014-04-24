package logicBox.gui.cloud;

import java.io.File;

import org.json.JSONObject;

import logicBox.gui.GUI;
import logicBox.gui.editor.menubar.EditorMenuBar;
import logicBox.web.Request;
import logicBox.web.RequestInterface;
import logicBox.web.User;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

public class CloudController {
	
	private static User user;
	
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
	
	public static User authUser(JSONObject json) {
		user = new User(json);
		
		return user;
	}
	
	public static User getUser() {
		return user;
	}
	
	public static void syncFile(File f) {
		new SyncWorker(f).start();
	}
	
	public static void setAuthState(boolean state) {
		EditorMenuBar emb = GUI.getMainFrame().getEditorMenuBar();
		
		emb.itemCloudLogin.setVisible(!state);
		emb.itemCloudRegister.setVisible(!state);
		emb.itemCloudFiles.setVisible(state);
		emb.itemCloudLogout.setVisible(state);
		
		if(!state) user = null;
	}
	
	private static class SyncWorker extends Thread {	
		File f;

		public SyncWorker(File f) {
			this.f = f;
		}
		
	    public void run() {
	        Request r = new Request();
	        
	        r.upload(f);
	        
			if(r.hasErrors()) 
				GUI.showErrorList(GUI.getMainFrame(), r.getErrors(), "Upload Failure");
			else
				GUI.showMessage(GUI.getMainFrame(), "File uploaded", "File was uploaded successfully!");
	    }
	}
}
