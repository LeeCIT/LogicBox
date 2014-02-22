


package prototype.mcq;



import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;

import java.util.*;



public class PanelTest extends JFrame
{
	
	 
	
	public PanelTest()
	{
		
		setSize(500, 500);
		setLayout( new MigLayout() );
		
		String ans1 = "Answer 1";
		String ans2 = "Answer 2";
		String ans3 = "Answer 3";
		String ans4 = "Answer 4";
		String ans5 = "Answer 5";
		String ans6 = "Answer 6";
		String ans7 = "Answer 7";
		
		
		String question = "Which one is Answer 4?";
		
		ArrayList<String> answers = new ArrayList<>();
		
		answers.add(ans1);
		answers.add(ans2);
		answers.add(ans3);
		answers.add(ans4);
		answers.add(ans5);
		answers.add(ans6);
		answers.add(ans7);
		
		McqQuestion q1 = new McqQuestion(question, answers, ans4);
		
		McqPanel panel = new McqPanel(q1);
		
		add( panel );
		
		setVisible(true);
	}
	
	
	
	public static void main(String[] args) 
	{
		
		PanelTest test = new PanelTest();
		
	}
}
