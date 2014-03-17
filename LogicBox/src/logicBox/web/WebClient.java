package logicBox.web;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;


public class WebClient 
{	
	private String url;
	
	public WebClient(String url)
	{
		this.url = url;
	}
	
	public void post(String request, Map<String, Object> params, final Request req, final RequestInterface ri)
	{
		HttpRequestWithBody h = Unirest.post(url + request);

		h.fields(params);
		
		h.asJsonAsync(new Callback<JsonNode>() 
		{
		    public void failed(UnirestException e) 
		    {	
		    	if(ri != null)
		    		ri.onRequestResponse(null, req, RequestInterface.status.FAILED);
		    }

		    public void completed(HttpResponse<JsonNode> response) 
		    {
		    	parseHeaders(response.getHeaders());
		    	parseErrors(response.getBody().getObject(), req.getErrors());
		    		
		    	if(ri != null)
		    		ri.onRequestResponse(response, req, RequestInterface.status.COMPLETED);
		    }

		    public void cancelled() 
		    {
		    	if(ri != null)
		    		ri.onRequestResponse(null, req, RequestInterface.status.CANCELLED);
		    }
		});	
	}
	
	public void get(String request, final Request req, final RequestInterface ri)
	{
		GetRequest r = Unirest.get(url + request);
		
		r.asJsonAsync(new Callback<JsonNode>() 
		{
		    public void failed(UnirestException e) 
		    {
		    	if(ri != null)
		    		ri.onRequestResponse(null, req, RequestInterface.status.FAILED);
		    }

		    public void completed(HttpResponse<JsonNode> response) 
		    {
		    	parseHeaders(response.getHeaders());
		    	parseErrors(response.getBody().getObject(), req.getErrors());
		    	
		    	if(ri != null)
		    		ri.onRequestResponse(response, req, RequestInterface.status.COMPLETED);
		    }

		    public void cancelled() 
		    {
		    	if(ri != null)
		    		ri.onRequestResponse(null, req, RequestInterface.status.CANCELLED);
		    }
		});	
	}
	
	public void upload(File f, Request req)
	{
		try 
		{
			HttpResponse<JsonNode> response = 
				Unirest.post(url + "user/files/upload")
				  .field("file", f)
	              .asJson();
			
			parseHeaders(response.getHeaders());
	    	parseErrors(response.getBody().getObject(), req.getErrors());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
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
		errors.clear();
		
		if(result.has("error"))
		{	
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
			catch (Exception e) 
			{
				e.printStackTrace();
			}

			return true;
		}
		
		return false;
	}
}
