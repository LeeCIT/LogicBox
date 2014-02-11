package logicBox.web;

public class TestClient implements AuthInterface
{
	public static void main(String[] args) 
	{
		Auth a = new Auth("http://cloud.jatochnietdan.com/", new TestClient());
		
		a.register("robert", "easy");
	}

	@Override
	public void onRegisterResponse(Auth auth, AuthInterface.status status) 
	{
		if(status == AuthInterface.status.COMPLETED)
		{
			if(auth.hasErrors())
			{
				for(String s : auth.getErrors())
					System.out.println(s);
			}
		}
	}
}
