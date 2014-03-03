


package prototype.mcq;



import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.LineBorder;

import com.sun.xml.internal.messaging.saaj.packaging.mime.util.QEncoderStream;

import net.miginfocom.swing.MigLayout;


/**
 * Class used to display each McqQuestionPanel created.
 * @author Shaun
 *
 */
public class McqPanel extends JPanel
{
	
	
	
	private ArrayList<McqQuestionPanel> questionPanels;
	private JButton nextPage = new JButton("Next");
	private int displayed = 5;
	private String [] choices = {"5", "10", "15", "20"};
	private JComboBox<String> displayChoices = new JComboBox<>(choices);
 	private int pageNo = 1;
	
	
	
	public McqPanel( ArrayList<McqQuestionPanel> questionPanels ) {
		
		super();
		
		setSize(500, 500);
		setLayout( new MigLayout() );
		
		
		this.questionPanels = questionPanels;
		
		
		setPanels();
		
		displayChoices.addActionListener( new questionsDisplayed() );
		
		setDropDownList();
		
		
	}


	
	private void setPanels() {
		
		for ( int j = 0; j < questionPanels.size(); j++ ) {
			remove(questionPanels.get(j));
		}
		
		for ( int i = 0; i < displayed; i++ ) {
			add( questionPanels.get(i), "wrap" );
		}
		
	}
	
	
	
	private void setDropDownList() {
		
		//add( new JLabel("How many questiosn per page?") );
		remove( displayChoices );
		add( displayChoices );
		
	}
	
	
	
	class questionsDisplayed implements ActionListener	{
		@Override
		public void actionPerformed(ActionEvent e) {
			
			displayed = Integer.parseInt( choices[ displayChoices.getSelectedIndex() ] );
			
			setPanels();
			setDropDownList();
		}
	}
}
