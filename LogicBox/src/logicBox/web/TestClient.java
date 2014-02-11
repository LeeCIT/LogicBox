package logicBox.web;

public class TestClient implements RequestInterface
{
	public static void main(String[] args) 
	{
		User a = new User("http://cloud.jatochnietdan.com/");
		
		a.register("robert", "easy", new TestClient());
	}

	@Override
	public void onRequestResponse(User user, RequestInterface.status status) 
	{
		if(status == RequestInterface.status.COMPLETED)
		{
			if(user.hasErrors())
			{
				for(String s : user.getErrors())
					System.out.println(s);
			}
		}
	}
}
