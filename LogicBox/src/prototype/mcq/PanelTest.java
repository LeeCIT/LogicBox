


package prototype.mcq;



import javax.swing.JFrame;
import javax.swing.JScrollPane;

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
		
		
		String question = "Which one is Answer 4?";
		
		ArrayList<String> answers = new ArrayList<>();
		
		answers.add(ans1);
		answers.add(ans2);
		answers.add(ans3);
		answers.add(ans4);
		answers.add(ans5);
		
		
		McqQuestion q1  = new McqQuestion(question, answers, ans4);
		McqQuestion q2  = new McqQuestion(question, answers, ans4);
		McqQuestion q3  = new McqQuestion(question, answers, ans4);
		McqQuestion q4  = new McqQuestion(question, answers, ans4);
		McqQuestion q5  = new McqQuestion(question, answers, ans4);
		McqQuestion q6  = new McqQuestion(question, answers, ans4);
		McqQuestion q7  = new McqQuestion(question, answers, ans4);
		McqQuestion q8  = new McqQuestion(question, answers, ans4);
		McqQuestion q9  = new McqQuestion(question, answers, ans4);
		McqQuestion q10 = new McqQuestion(question, answers, ans4);
			
		
		ArrayList<McqQuestionPanel> questionPanels = new ArrayList<>();
		
		McqQuestionPanel p1  = new McqQuestionPanel(q1);
		McqQuestionPanel p2  = new McqQuestionPanel(q2);
		McqQuestionPanel p3  = new McqQuestionPanel(q3);
		McqQuestionPanel p4  = new McqQuestionPanel(q4);
		McqQuestionPanel p5  = new McqQuestionPanel(q5);
		McqQuestionPanel p6  = new McqQuestionPanel(q6);
		McqQuestionPanel p7  = new McqQuestionPanel(q7);
		McqQuestionPanel p8  = new McqQuestionPanel(q8);
		McqQuestionPanel p9  = new McqQuestionPanel(q9);
		McqQuestionPanel p10 = new McqQuestionPanel(q10);
		
		questionPanels.add(p1);
		questionPanels.add(p2);
		questionPanels.add(p3);
		questionPanels.add(p4);
		questionPanels.add(p5);
		questionPanels.add(p6);
		questionPanels.add(p7);
		questionPanels.add(p8);
		questionPanels.add(p9);
		questionPanels.add(p10);
		
		McqPanel panel = new McqPanel(questionPanels);
		
		JScrollPane scroll = new JScrollPane(panel);
		add(scroll, "w 100%, h 100%");
		
		
		setVisible(true);
		
	}
	
	
	public PanelTest( McqQuestionPanel panel ) {
		
	}
	
	
	public static void main(String[] args) 
	{
		
		PanelTest test = new PanelTest();
		
	}
}
