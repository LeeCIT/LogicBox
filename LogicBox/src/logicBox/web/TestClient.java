package logicBox.web;

public class TestClient
{
	public static void main(String[] args) 
	{
		RequestHandler rh = new RequestHandler();
		RequestHandler.LoginHandler lh = rh.new LoginHandler();
		
		Request r = new Request("http://lvh.me/");
		
		r.login("demo@demo.com", "dmo", lh);
	}
}
