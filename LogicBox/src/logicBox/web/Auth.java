package logicBox.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Auth 
{
	private WebClient wc = null;
	private ArrayList<String> errors = new ArrayList<String>();
	
	public Auth(String url)
	{
		this.wc = new WebClient(url);
	}
	
	public boolean register(String email, String password)
	{
		Map<String, Object> map = new HashMap<String, Object>(); 
		
		map.put("email", email);
		map.put("password", password);
		
		JSONObject result = wc.post("register", map).getObject();
		
		return !hasErrors(result);
	}
	
	public ArrayList<String> getErrors()
	{
		return errors;
	}
	
	private boolean hasErrors(JSONObject result)
	{
		if(result.has("error"))
		{
			errors.clear();
			
			try 
			{
				JSONObject messages = result.getJSONObject("messages");
				
				@SuppressWarnings("unchecked") // Need to use a legacy API, unfortunate
				Iterator<String> i = messages.keys();
				
				while(i.hasNext())
				{
					JSONArray a = (JSONArray) messages.get(i.next());
					
					errors.add(a.getString(0));
				}
					
			} 
			catch (JSONException e) 
			{
				e.printStackTrace();
			}

			return true;
		}
		
		return false;
	}
}
