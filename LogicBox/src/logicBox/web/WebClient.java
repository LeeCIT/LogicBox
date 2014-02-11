package logicBox.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;


public class WebClient 
{	
	private String url;
	
	public WebClient(String url)
	{
		this.url = url;
	}
	
	public void post(String request, Map<String, Object> params, final Auth auth, final AuthInterface ai)
	{
		HttpRequestWithBody h = Unirest.post(url + request);

		h.fields(params);
		
		h.asJsonAsync(new Callback<JsonNode>() 
		{
		    public void failed(UnirestException e) 
		    {
		    	ai.onRegisterResponse(null, AuthInterface.status.FAILED);
		    }

		    public void completed(HttpResponse<JsonNode> response) 
		    {
		    	parseHeaders(response.getHeaders());
		    	parseErrors(response.getBody().getObject(), auth.getErrors());
		    	
		    	ai.onRegisterResponse(auth, AuthInterface.status.COMPLETED);
		    }

		    public void cancelled() 
		    {
		    	ai.onRegisterResponse(null, AuthInterface.status.CANCELLED);
		    }
		});	
	}
	
	private void parseHeaders(Map<String, String> headers)
	{
		if(headers.containsKey("set-cookie"))
		{
			Unirest.setDefaultHeader("Cookie", headers.get("set-cookie"));
		}
	}
	
	private boolean parseErrors(JSONObject result, ArrayList<String> errors)
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
