package logicBox.web;

import java.util.HashMap;
import java.util.Map;
import com.mashape.unirest.http.JsonNode;

public class TestClient 
{
	public static void main(String[] args) 
	{
		WebClient wc = new WebClient("http://cloud.jatochnietdan.com/");
		
		Map<String, Object> map = new HashMap<String, Object>(); 
		
		map.put("email", "wow");
		
		JsonNode result = wc.post("register", map);
		
		System.out.println(result.toString());
	}
}
