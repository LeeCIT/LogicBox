


package prototype.mcq;



import java.util.ArrayList;



public class McqQuestion {
	
	
	
	private ArrayList<String> answers;
	private String question, correctAnswer;
	
	
	
	public McqQuestion( String question, ArrayList<String> answers, String correctAnswer ) {
		this.answers = answers;
		this.correctAnswer = correctAnswer;
		this.question = question;
		
	}
	
	
	
	/**
	 * Return the ArrayList of possible answers
	 * to the question.
	 * @return
	 */
	public ArrayList<String> getAnswers() {
		return answers;
	}
	
	
	
	/**
	 * Return the question.
	 * @return
	 */
	public String getQuestion() {
		return question;
	}
	
	
	
	/**
	 * Return the correct answer.
	 * @return
	 */
	public String getCorrectAnswer() {
		return correctAnswer;
	}
	
	
}
