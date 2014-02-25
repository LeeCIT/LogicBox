


package prototype.mcq;



import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;



public class CreateMCQPanel extends JPanel
{
	
	
	
	private ArrayList<JTextField> quesionFields = new ArrayList<>();
	private JTextField questionField = new JTextField(); 
	private int numOfAnswers = 4;
	
	
	public CreateMCQPanel() {
		
		
		super();
		setLayout( new MigLayout() );
		
		
		addToPanel();
	}
	
	
	
	/**
	 * Add the contents to the panel.
	 */
	private void addToPanel(){
		
		add ( new JLabel("Create MCQ Question"), "wrap" );
		add ( new JLabel("Question: "), "split 2");
		
		questionField.setPreferredSize( new Dimension(400,20));
		add ( questionField, "wrap" );
		
		add ( new JLabel("Set answers to the question: "), "wrap" );
		
		addAnswerFields();
		
		
	}
	
	
	
	private void addAnswerFields() {
		
		for ( int i = 0; i < numOfAnswers; i++ )
		{
			quesionFields.add( new JTextField() );
			add( quesionFields.get(i), "wrap" );
			quesionFields.get(i).setPreferredSize( new Dimension(100, 10));
		}
		
	}
	
}
