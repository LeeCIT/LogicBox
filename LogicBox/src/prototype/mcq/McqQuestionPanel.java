


package prototype.mcq;



import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.Timer;

import net.miginfocom.swing.MigLayout;



public class McqQuestionPanel extends JPanel
{
	
	
	private ArrayList<String> answers;
	private ArrayList<JRadioButton> buttons = new ArrayList<>();
	private String correctAnswer, question;
	private ButtonGroup options = new ButtonGroup();
	private int questionNum;
	
	
	
	
	public McqQuestionPanel( McqQuestion mcq ) {
		
		super();
		
		
		setLayout( new MigLayout() );
		setBorder( new LineBorder(new Color(0, 0, 0)) );
		
		
		this.answers = mcq.getAnswers();
		this.correctAnswer = mcq.getCorrectAnswer();
		this.question = mcq.getQuestion();
		this.questionNum = mcq.getQuestionNum();
		
		displayQuestion();
		
		startTimer();
	}
	
	
	
	/**
	 * Display the question to the panel.
	 */
	private void displayQuestion() {
		
		int j = 0;
		
		add( new JLabel("(Q" + questionNum + ")"), "wrap");
		add( new JLabel(question), "wrap" ); //Add the question to the panel.
		
		
		//Display each answer with a radio button across from it.
		for ( String ans: answers ) {
			buttons.add( new JRadioButton(ans) );
			options.add( buttons.get(j) );
			add ( buttons.get(j), "wrap" );
			j++;
		}
		
		JButton checkAnswer = new JButton("Check answer");
		add( checkAnswer );
		checkAnswer.addActionListener( new buttonListener() );
		
	}
	
	
	
	/**
	 * Choose weather the panel is visible or not.
	 * @param visible
	 */
	public void setPanelVisibility( boolean visible ) {
		setVisible(visible);
	}
	
	
	
	private void startTimer() {
		
	}
	
	
	class buttonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			
			for ( int i = 0; i < buttons.size(); i++ )
			{
				
				if ( buttons.get(i).isSelected() )
				{
					
					if ( answers.get(i).equals(correctAnswer) )
					{
						System.out.println("You are correct!");
					}
					else
						System.out.println("INCORRECT!");
					
				}
				
			}
			
		}
	}
	
	
	
	
}
