package prototypes.rightClickPrototype;

import javax.swing.*;
import logicBox.gui.ContextMenu;


public class DemoView extends JFrame{

	JPopupMenu popup;
	JMenuItem  addPin;
	JMenuItem  removePin;
	JLabel 	   demoGate;
	
	int pins = 2;

	String [] contextItems = {"Add pin", "Remove pin #-#", "crap"};
	
	
	public DemoView() {

		//Set the native look and feel, IT will be in a different class when finished
		setNativeLookAndFeel();

		//Setup
		popup    = new JPopupMenu();
		demoGate = new JLabel("And gate pins = " + pins);

		//Add the listener to the gate
		demoGate.addMouseListener(new ContextMenu(contextItems));

		//Add Label to the frame
		add(demoGate);

		//Frame stuff
		setTitle("Demo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);			//Set the frame to visible				
		setSize(700, 700);			//Set the size of the frame
		setLocationRelativeTo(null);//Loads the window in the center
	}



	//Set native look and feel
	private void setNativeLookAndFeel() {
		try 
		{
			//Set System L&F
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		} 
		catch (Exception e) {
			e.printStackTrace();		
		}
	}


	// I know, this is awful, but I want a small demo to be working
	private void resetLabel() {
		demoGate.setText("And gate pins = " + pins);
	}

}

