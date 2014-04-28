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
import com.mashape.unirest.http.Unirest;

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
					
					Unirest.clearDefaultHeaders();
					
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
		
		emb.itemCloudLogin   .setEnabled(!state);
		emb.itemCloudRegister.setEnabled(!state);
		emb.itemCloudFiles   .setEnabled( state);
		emb.itemCloudLogout  .setEnabled( state);
		
		if(!state)
			user = null;
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
			{
				if(!user.getFiles().contains(f.getName()))
					user.getFiles().add(f.getName());
				
				GUI.showMessage(GUI.getMainFrame(), "File uploaded", "File was uploaded successfully!");
			}
	    }
	}
}
