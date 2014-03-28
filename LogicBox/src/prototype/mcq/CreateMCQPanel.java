


package prototype.mcq;



import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;



public class CreateMCQPanel extends JPanel
{
	
	
	
	private ArrayList<JTextField> answerFields = new ArrayList<>();
	private ArrayList<String> answers = new ArrayList<>();
	private JTextField questionField = new JTextField(); 
	private int numOfAnswers = 4;
	private JButton addQuestion = new JButton("Add Question");
	private ButtonGroup options = new ButtonGroup();
	private ArrayList<JRadioButton> correctAnswer = new ArrayList<>();
	
	
	
	public CreateMCQPanel() {
		
		
		super();
		setLayout( new MigLayout() );
		
		
		addToPanel();
		
		addQuestion.addActionListener( new addQuestion() );
	}
	
	
	
	/**
	 * Add the contents to the panel.
	 */
	private void addToPanel() {
		
		add ( new JLabel("Create MCQ Question"), "wrap" );
		add ( new JLabel("Question: "), "split 2" );
		
		questionField.setPreferredSize( new Dimension(400,20));
		add ( questionField, "wrap" );
		
		add ( new JLabel("Set answers to the question: "), "wrap" );
		
		addAnswerFields();
		
		add( addQuestion );
	}
	
	
	
	/**
	 * Add JTextFields to allow the user to fill in answers
	 * to the question.
	 */
	private void addAnswerFields() {
		
		for ( int i = 0; i < numOfAnswers; i++ )
		{
			answerFields.add( new JTextField() ); //Store the JTextField in the ArrayList.
			add( answerFields.get(i), "split 2" ); //Add the JTextField to the panel.
			answerFields.get(i).setPreferredSize( new Dimension(100, 10)); //Set the dimensions.
			correctAnswer.add( new JRadioButton() );
			options.add( correctAnswer.get(i) );
			add ( correctAnswer.get(i), "wrap" );
		}
		
		
	}
	
	
	
	/**
	 * Return the JTextFields holding the answers. 
	 * @return
	 */
	public ArrayList<JTextField> getAnswerFields() {
		return answerFields;
	}
	
	
	
	/**
	 * Return the JButton to submit a question.
	 * @return
	 */
	public JButton getAddQuestionButton() {
		return addQuestion;
	}
	
	
	
	/**
	 * Returns the correct answer which is assigned
	 * depending on the radio button selected.
	 * @return
	 */
	public String getCorrectAnswer() {
		for (int i = 0; i < answerFields.size(); i++ ) {
			if ( correctAnswer.get(i).isSelected() ) 
				return answerFields.get(i).getText();
		}
		return null;
	}
	
	
	
	/**
	 * Return the question specified.
	 * @return
	 */
	public String getQuestion() {
		return questionField.getText();
	}
	
	
	
	/**
	 * Return the answers set to the question.
	 * @return
	 */
	public ArrayList<String> getAnswers() {
		for ( JTextField ans: answerFields ) {
			answers.add( ans.getText() );
		}
		return answers;
	}
	
	
	
	class addQuestion implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			McqQuestionList list = McqQuestionList.getInstance();
			
			McqQuestion question = new McqQuestion(getQuestion(), getAnswers(), getCorrectAnswer());
			
			list.add(question);
		}
	}
}
