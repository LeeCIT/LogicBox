package logicBox.web;

import java.io.File;
import java.io.IOException;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

public class RequestHandler
{
	public class LoginHandler implements RequestInterface
	{
		public void onRequestResponse(HttpResponse<JsonNode> response,
				Request request, status status) 
		{
			if(request.hasErrors())
				for(String error : request.getErrors())
					System.out.println(error);
			else
			{
				System.out.println("Successfully logged in, uploading file...");
				
				request.upload(new File("../BoxCloud/readme.md"));
				
				if(request.hasErrors())
					for(String error : request.getErrors())
						System.out.println(error);
				else
					System.out.println("File was uploaded!");
				
				try {
					Unirest.shutdown();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
