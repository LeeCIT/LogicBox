package logicBox.web;

public interface AuthInterface 
{
	public enum status
	{
		COMPLETED, CANCELLED, FAILED
	};
	
	void onRegisterResponse(Auth a, status s);
}
