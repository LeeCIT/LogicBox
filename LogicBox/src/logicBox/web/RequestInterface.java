package logicBox.web;

public interface RequestInterface 
{
	public enum status
	{
		COMPLETED, CANCELLED, FAILED
	};
	
	void onRequestResponse(User a, status s);
}
