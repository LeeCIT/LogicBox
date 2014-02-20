


package prototype.mcq;



import java.awt.Color;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.LineBorder;

import net.miginfocom.swing.MigLayout;



public class McqPanel extends JPanel
{
	
	
	private ArrayList<String> answers;
	private ArrayList<JRadioButton> buttons = new ArrayList<>();
	private String correctAnswer, question;
	private ButtonGroup options = new ButtonGroup();
	
	
	
	public McqPanel( ArrayList<String> answers, String correctAnswer, String question ) {
		
		super();
		
		
		setLayout( new MigLayout() );
		setSize(300, 300);
		setBorder(new LineBorder(new Color(0, 0, 0)));
		
		
		this.answers = answers;
		this.correctAnswer = correctAnswer;
		this.question = question;
		
		
		displayQuestion();
	}
	
	
	
	/**
	 * Display the question to the panel.
	 */
	private void displayQuestion() {
		
		int j = 0;
		
		add( new JLabel(question), "wrap" ); //Add the question to the panel.
		
		
		
		//Display each answer with a radio button across from it.
		for ( String ans: answers ) {
			add ( new JLabel(ans) );
			buttons.add( new JRadioButton() );
			options.add( buttons.get(j) );
			add ( buttons.get(j), "wrap" );
			j++;
		}
		
	}
	
	
	
}
