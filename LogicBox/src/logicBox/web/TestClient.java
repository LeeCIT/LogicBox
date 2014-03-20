package logicBox.web;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

public class TestClient
{
	public static void main(String[] args) 
	{
		Request r = new Request("http://lvh.me/");
		
		r.setRequestInterface(new RequestInterface()
		{
			@Override
			public void onRequestResponse(HttpResponse<JsonNode> response,
					Request request, status status) 
			{
				System.out.println(response.getBody().toString());
			}
		});
		
		r.login("demo@demo.com", "demo");
	}
}
