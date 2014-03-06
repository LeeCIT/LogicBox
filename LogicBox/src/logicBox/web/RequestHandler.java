package logicBox.web;

import java.io.IOException;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

public class RequestHandler
{
	public class LoginHandler implements RequestInterface
	{
		public void onRequestResponse(HttpResponse<JsonNode> response,
				Request request, status state) 
		{
			if(state == status.FAILED)
				System.out.println("Failed to connect!");
			else
			{
				if(request.hasErrors())
					for(String error : request.getErrors())
						System.out.println(error);
				else
				{
					System.out.println(response.getBody().toString());
					
					User u = new User(response.getBody().getObject());
					
					try 
					{
						Unirest.shutdown();
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
			}
		}
	}
}