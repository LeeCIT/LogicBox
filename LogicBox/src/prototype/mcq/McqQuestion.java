


package prototype.mcq;



import java.util.ArrayList;



public class McqQuestion {
	
	
	
	private ArrayList<String> answers;
	private String question, correctAnswer;
	private static int questionNum = 0;
	private int thisQuestionNum = questionNum;
	
	
	public McqQuestion( String question, ArrayList<String> answers, String correctAnswer ) {
		this.answers = answers;
		this.correctAnswer = correctAnswer;
		this.question = question;
		questionNum++; //Increase the number of questions for every new instance.
	}
	
	
	
	/**
	 * Return the ArrayList of possible answers.
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
	
	
	
	/**
	 * Return the number of questions added.
	 * @return
	 */
	public int getQuestionNum() {
		return thisQuestionNum;
	}
}
