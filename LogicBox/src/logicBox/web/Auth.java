package logicBox.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Auth
{
	private WebClient wc = null;
	private ArrayList<String> errors = new ArrayList<String>();
	
	public Auth(String url, Object implement)
	{
		this.wc = new WebClient(url, implement);
	}

	public void register(String email, String password)
	{
		Map<String, Object> map = new HashMap<String, Object>(); 
		
		map.put("email", email);
		map.put("password", password);
		
		wc.post("register", map, this);
	}
	
	public ArrayList<String> getErrors()
	{
		return errors;
	}
	
	public boolean hasErrors()
	{
		return errors.size() > 0;
	}
}
