package logicBox.web;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

public interface RequestInterface 
{
	public enum status
	{
		COMPLETED, CANCELLED, FAILED
	};
	
	void onRequestResponse(HttpResponse<JsonNode> response, User a, status s);
}
