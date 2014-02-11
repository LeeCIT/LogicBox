package logicBox.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Auth
{
	private WebClient wc = null;
	private ArrayList<String> errors = new ArrayList<String>();
	
	public Auth(String url)
	{
		this.wc = new WebClient(url);
	}

	public void register(String email, String password, AuthInterface ai)
	{
		Map<String, Object> map = new HashMap<String, Object>(); 
		
		map.put("email", email);
		map.put("password", password);
		
		wc.post("register", map, this, ai);
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
