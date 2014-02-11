package logicBox.web;

public class TestClient implements AuthInterface
{
	public static void main(String[] args) 
	{
		Auth a = new Auth("http://cloud.jatochnietdan.com/");
		
		a.register("robert", "easy", new TestClient());
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
