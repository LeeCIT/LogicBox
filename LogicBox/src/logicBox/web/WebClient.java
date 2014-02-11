package logicBox.web;

import java.util.Map;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;


public class WebClient 
{
	private String url;
	
	public WebClient(String url)
	{
		this.url = url;
	}
	
	public JsonNode post(String request, Map<String, Object> params)
	{
		try 
		{	
			HttpRequestWithBody h = Unirest.post(url + request);

			h.fields(params);
			
			HttpResponse<JsonNode> result = h.asJson();
			
			parseHeaders(result.getHeaders());
			
			return result.getBody();
		} 
		catch (UnirestException e) 
		{
			e.printStackTrace();
		}		
		
		return null;
	}
	
	private void parseHeaders(Map<String, String> headers)
	{
		if(headers.containsKey("set-cookie"))
		{
			Unirest.setDefaultHeader("Cookie", headers.get("set-cookie"));
		}
	}
}
