package rightClickPrototype;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import logicBox.gui.ContextMenu;
import frameShareProto.GetToolBarSelectionAsString;
import frameShareProto.ToolBar;
import frameShareProto.ToolSelectionEnum.ToolSelection;

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

