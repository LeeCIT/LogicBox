


package prototype.mcq;



import java.util.ArrayList;



public class McqQuestionList extends ArrayList<McqQuestion>
{
	
	
	
	private static McqQuestionList instance = null;


	
	private McqQuestionList() {
		super();
	}
	
	
	
	public static McqQuestionList getInstance() {
		if ( instance == null ) {
			instance = new McqQuestionList();
		}
		return instance;
	}
}
