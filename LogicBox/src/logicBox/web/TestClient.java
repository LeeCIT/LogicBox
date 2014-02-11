package logicBox.web;

public class TestClient 
{
	public static void main(String[] args) 
	{
		Auth a = new Auth("http://cloud.jatochnietdan.com/");
		
		if(!a.register("robert", ""))
		{
			for(String error : a.getErrors())
				System.out.println(error);
		}
	}
}
