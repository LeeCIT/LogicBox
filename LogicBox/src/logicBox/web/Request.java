package logicBox.web;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Request
{
	private WebClient wc = null;
	private ArrayList<String> errors = new ArrayList<String>();
	
	public Request(String url)
	{
		this.wc = new WebClient(url);
	}

	public void register(String email, String password, RequestInterface ri)
	{
		Map<String, Object> map = new HashMap<String, Object>(); 
		
		map.put("email", email);
		map.put("password", password);
		
		wc.post("register", map, this, ri);
	}
	
	public void login(String email, String password, RequestInterface ri)
	{
		Map<String, Object> map = new HashMap<String, Object>(); 
		
		map.put("email", email);
		map.put("password", password);
		
		wc.post("login", map, this, ri);
	}
	
	public void upload(File f)
	{
		wc.upload(f, this);
	}
	
	public void requestInfo(RequestInterface ri)
	{
		wc.get("user/", this, ri);
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