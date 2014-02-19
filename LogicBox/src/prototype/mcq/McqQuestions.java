


package prototype.mcq;



import java.util.ArrayList;



public class McqQuestions {
	
	
	
	private ArrayList<String> answers;
	private String question;
	
	
	
	public McqQuestions( String question, ArrayList<String> answers ) {
		this.answers = answers;
		this.question = question;
		
	}
	
	
	
	/**
	 * Return the ArrayList of possible answers
	 * to the question.
	 * @return
	 */
	public ArrayList<String> getAnswers(){
		return answers;
	}
	
	
	
	/**
	 * Return the question.
	 * @return
	 */
	public String getQuestion(){
		return question;
	}
	
	
	
}
