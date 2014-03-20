package logicBox.web;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class User 
{
	private int id;
	private String email;
	private ArrayList<String> files = new ArrayList<String>();
	
	public User(JSONObject jo)
	{
		try 
		{
			JSONObject userdata = jo.getJSONObject("user");
			
			email = userdata.getString("email");
			id = userdata.getInt("id");

			JSONArray filesarray = jo.getJSONArray("files");
			
			for(int i = 0; i < filesarray.length(); i++)
				files.add(filesarray.getString(i));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public int getId() 
	{
		return id;
	}
	
	public String getEmail() 
	{
		return email;
	}
	
	public ArrayList<String> getFiles()
	{
		return files;
	}
}